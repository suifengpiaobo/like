package com.aladdin.like.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.event.ShareEvent;
import com.aladdin.like.http.WChatHttpClient;
import com.aladdin.like.model.LoginStateEvent;
import com.aladdin.utils.ContextUtils;
import com.aladdin.utils.LogUtil;
import com.aladdin.utils.SharedPreferencesUtil;
import com.aladdin.utils.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

/**
 * Description 微信登录，分享
 * Created by zxl on 2017/4/28 下午5:21.
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    public static final String WX_APPID = ContextUtils.getInstance().getApplicationContext().getApplicationContext().getResources().getString(R.string.wx_appid);
    public static final String WX_SECRET = ContextUtils.getInstance().getApplicationContext().getApplicationContext().getString(R.string.wx_secret);
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, WX_APPID, true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("WXEntryActivity","onNewIntent");
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e("WXEntryActivity","onReq");
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:

                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(goToShowMsg((ShowMessageFromWX.Req)req));
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.e("onResp");
        switch (resp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                if (Constant.IS_AUTH_WCHAT) {
                    String code = ((SendAuth.Resp) resp).code;
                    WChatHttpClient.requestGetWChatToken(code);
                    LikeAgent.getInstance().setCode(code);
                } else {
                    EventBus.getDefault().post(new ShareEvent(ShareEvent.SHARE_SUCCESS));
                    int shareCount = SharedPreferencesUtil.INSTANCE.getInt(Constant.SHARE_TIMES,0);
                    SharedPreferencesUtil.INSTANCE.putInt(Constant.SHARE_TIMES,shareCount+1);
                    MobclickAgent.onEvent(WXEntryActivity.this,"Share");
                }
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:// 发送拒绝
            case BaseResp.ErrCode.ERR_SENT_FAILED:// 发送失败

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                EventBus.getDefault().post(new LoginStateEvent(LoginStateEvent.LOGIN_CANCEL));
                break;
            case BaseResp.ErrCode.ERR_BAN:

                break;
        }
        this.finish();
    }

    private String goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);
        return  msg.toString();
    }
}

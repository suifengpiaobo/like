package com.aladdin.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.UUID;

/**
 * Description 微信分享
 * Created by zxl on 2017/4/27 下午12:23.
 * Email:444288256@qq.com
 */
public class WXUtils {

    /**
     * 分享图片
     * @param context
     * @param bitmap
     * @param scene
     * @return
     */
    public static boolean shareBitmap(Context context,Bitmap bitmap,int scene){
        boolean result = false;
        String appid = "wx506f2c76b39d74fd";

        IWXAPI api = WXAPIFactory.createWXAPI(context, appid);
        api.registerApp(appid);

        if (!api.isWXAppInstalled()) {
            ToastUtil.sToastUtil.shortDuration("未安装微信客户端");
            return false;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtil.sToastUtil.shortDuration("当前的微信版本过低，请先更新");
            return false;
        }

        WXImageObject image = new WXImageObject(bitmap);
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = image;
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap,150,150,true);
        bitmap.recycle();
        message.thumbData = BitmapUtils.bmpToByteArray(thumbBitmap,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = UUID.randomUUID().toString();
        req.message = message;
        req.scene = scene;
        return api.sendReq(req);
    }

}

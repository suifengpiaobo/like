package com.aladdin.like.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.aladdin.utils.BitmapUtils;
import com.aladdin.utils.LogUtil;
import com.aladdin.utils.ToastUtil;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
     * @param bitmapUrl
     * @param scene
     * @return
     */
    public static boolean shareBitmap(Context context,String bitmapUrl,int scene){
        LogUtil.i("--bitmapUrl-->>>"+bitmapUrl);
        boolean result = false;
        String appid = "wxcd27b7e580be7978";

        IWXAPI api = WXAPIFactory.createWXAPI(context, appid);
        api.registerApp(appid);

        if (!api.isWXAppInstalled()) {
            ToastUtil.showToast("未安装微信客户端");
            return false;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtil.showToast("当前的微信版本过低，请先更新");
            return false;
        }
        Bitmap bitmap = getBitmap(bitmapUrl);
        LogUtil.i("--bitmap--"+bitmap);
        WXImageObject image = new WXImageObject(bitmap);
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = image;
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap,150,150,true);
        bitmap.recycle();
        message.thumbData = BitmapUtils.bmpToByteArray2(thumbBitmap,true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = UUID.randomUUID().toString();
        req.message = message;
        req.scene = scene;
        return api.sendReq(req);
    }

    public static boolean shareBitmap(Context context,Bitmap bitmap,int scene){
        boolean result = false;
        String appid = "wxcd27b7e580be7978";

        IWXAPI api = WXAPIFactory.createWXAPI(context, appid);
        api.registerApp(appid);

        if (!api.isWXAppInstalled()) {
            ToastUtil.showToast("未安装微信客户端");
            return false;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtil.showToast("当前的微信版本过低，请先更新");
            return false;
        }

        LogUtil.i("--bitmap--"+bitmap);
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

    public static Bitmap getBitmap(String url){
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(url.toString()));
        File file = resource.getFile();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file.getAbsoluteFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bitmap  = BitmapFactory.decodeStream(fis);

        return bitmap;
    }

}

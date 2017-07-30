package com.aladdin.like.http;

import android.text.TextUtils;

import com.aladdin.http.HttpResultProcess;
import com.aladdin.http.RequestType;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.model.UserInfo;
import com.aladdin.like.model.UserPojo;
import com.aladdin.utils.LogUtil;
import com.zxl.network_lib.HttpUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;
import com.zxl.network_lib.Inteface.HttpResultListener;
import com.zxl.network_lib.OkHttpHeadersInit;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;


/**
 * Description
 * Created by zxl on 2017/5/17 下午3:49.
 * Email:444288256@qq.com
 */
public enum HttpManager {
    INSTANCE;

    HttpManager() {
    }

    /**
     * 普通短连请求
     *
     * @param path               {@link HttpUrl}请求路径
     * @param param              请求参数
     * @param requestType        {@link RequestType}请求方式<em>post get</em>
     * @param httpResultCallback 结果回调
     */
    public <T> void shortConnectRequest(String path, String param, RequestType requestType, HttpResultCallback<T> httpResultCallback, Class<T> uClass) {

        StringBuilder url = new StringBuilder();
        if (path.contains("http") || path.contains("https")) {
            url.append(path);
        } else {
//            url.append(HttpUrl.API_URL).append(path);
        }

        OkHttpHeadersInit.initOkHttpHeaders("token","likeApp1qaz2wsx");
        if (requestType == RequestType.POST) {
            HttpUtil.getInstance().POST(url.toString(), param, HttpResultProcess.httpResultListener(httpResultCallback, uClass));

        } else {
            HttpUtil.getInstance().GET(url.toString(), param, HttpResultProcess.httpResultListener(httpResultCallback, uClass));

        }

    }

    public  <T> void shortConnectRequest(String path, Map param, RequestType requestType, HttpResultCallback<T> httpResultCallback, Class<T> uClass) {

        if(requestType == RequestType.POST){
            HttpUtil.getInstance().POST(path, param,HttpResultProcess.httpResultListener(httpResultCallback,uClass));

        }else{
            HttpUtil.getInstance().GET(path, param, HttpResultProcess.httpResultListener(httpResultCallback,uClass));
        }

    }

    /**
     * 用户登录
     * @param logtype 登录方式, 1: 微信, 2: 微博, 3: qq
     * @param name
     * @param avatar
     * @param openid 微信openid
     * @param unionid 微信unionid, 当 logtype = 1 时必传
     * @param callback
     */
    public void login(int logtype, String name, String avatar, String openid, String unionid, HttpResultCallback<UserPojo> callback){
        Map<String, Object> map = new HashMap<>();
        map.put("logtype",logtype);
        map.put("name",name);
        map.put("avatar",avatar);
        map.put("openid",openid);
        map.put("unionid",unionid);
        //device 设备  0: 未知, 1: 安卓, 2: 苹果
        map.put("device",1);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.USER_LOGIN,params,RequestType.POST,callback,UserPojo.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取全部主题(搜索)
     * @param openid
     * @param themeName 获取全部主题是传'null'
     * @param callback
     */
    public void getTheme(String openid, String themeName, HttpResultCallback<ThemeModes> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }

        if (!TextUtils.isEmpty(themeName)){
            map.put("themeName",themeName);
        }

        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_ALL_THEME,params,RequestType.POST,callback,ThemeModes.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  获取用户感兴趣的主题
     * @param openid
     * @param callback
     */
    public void getUserTheme(String openid,HttpResultCallback<ThemeModes> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_USER_THEME,params,RequestType.POST,callback,ThemeModes.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 添加用户感兴趣主题
     * @param openid
     * @param themeId
     * @param operateType 1、添加  2删除
     * @param callback
     */
    public void addUserTheme(String openid, List<String> themeId,int operateType, HttpResultCallback<String> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        map.put("operateType",operateType);

        if (themeId.size() >0){
            String tempThemeId = themeId.toString();
            tempThemeId = tempThemeId.replace("[", "");
            tempThemeId = tempThemeId.replace("]", "");
            map.put("themeId",tempThemeId);
        }
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.ADD_USER_THEME,params,RequestType.POST,callback,String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  查看专辑详情
     * @param openid
     * @param albymId 专辑id
     * @param page
     * @param page_num
     * @param callback
     */
    public void getThemeDetail(String openid, String albymId, int page, int page_num, HttpResultCallback<ThemeDetail> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        map.put("albymId",albymId);
        map.put("page",page);
        map.put("page_num",page_num);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_THEME_DETAIL,params,RequestType.POST,callback,ThemeDetail.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取专辑
     * @param openid
     * @param themeId 专辑id
     * @param page
     * @param page_num
     * @param callback
     */
    public void getAlbymDetail(String openid, String themeId, int page, int page_num, HttpResultCallback<AlbymModel> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        map.put("themeId",themeId);
        map.put("page",page);
        map.put("page_num",page_num);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_ALBYM_DETAIL,params,RequestType.POST,callback,AlbymModel.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 图片收藏
     * @param operateType 1、收藏  2、取消收藏
     * @param openid
     * @param imageId
     * @param callback
     */
    public void collectionImage(String openid,String imageId, int operateType,HttpResultCallback<String> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        map.put("imageId",imageId);
        map.put("operateType",operateType);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.USER_COLLECT_THEME,params,RequestType.POST,callback,String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 日记查看
     * @param openid
     * @param auditSign 0：未审核 1: 审核通过的
     * @param page
     * @param page_num
     * @param callback
     */
    public void getUserDiary(String openid, int auditSign,int page, int page_num, HttpResultCallback<DiaryDetail> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        map.put("auditSign",auditSign);
        map.put("page",page);
        map.put("page_num",page_num);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_DIARY_IMAGE,params,RequestType.POST,callback,DiaryDetail.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发布日志
     * @param openid
     * @param image
     * @param diaryTitle
     * @param diaryContent
     */
    public void addUserDiary(String openid, String image, File file, String diaryTitle, String diaryContent, HttpResultListener callback){
        HttpUtil.getInstance().upLoadPic(HttpUrl.USER_ADD_DIARY,image, Headers.of("token","likeApp1qaz2wsx"),
                file,callback,
                "openid",openid,"image",image,"diaryTitle",diaryTitle,"diaryContent",diaryContent);
    }

    /**
     * 更改用户背景图片
     * @param openid
     * @param file
     * @param callback
     */
    public void addUserImg(String openid, String image,File file, HttpResultListener callback){
        LogUtil.i("---addUserImg---");
        HttpUtil.getInstance().upLoadPic(HttpUrl.ADD_USER_IMG,image,Headers.of("token","likeApp1qaz2wsx"),
                file,callback,"openid",openid,"image",image);
    }

    /**
     * 获取用户背景图
     * @param openid
     * @param callback
     */
    public void getUserInfo(String openid, HttpResultCallback<UserInfo> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_USER_IMG,params,RequestType.POST,callback,UserInfo.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 用户收藏图片
     * @param openid
     * @param page
     * @param page_num
     * @param operateType 1、收藏 2、取消收藏
     * @param callback
     */
    public void getUserCollectionImage(String openid, int page, int page_num,int operateType, HttpResultCallback<CollectionImage> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        map.put("page",page);
        map.put("page_num",page_num);
        map.put("operateType",operateType);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_USER_DIARY,params,RequestType.POST,callback,CollectionImage.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  日记
     * @param openid
     * @param diaryId
     * @param operateType 1、收藏 2、取消收藏
     * @param callback
     */
    public void collectionDiary(String openid,String diaryId,int operateType,HttpResultCallback<String> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }
        map.put("diaryId",diaryId);
        map.put("operateType",operateType);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.COLLECTION_DIARY,params,RequestType.POST,callback,String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取用户收藏的日记
     * @param openid
     * @param page
     * @param page_num
     * @param callback
     */
    public void getCollectionDiary(String openid, int page, int page_num,HttpResultCallback<DiaryDetail> callback){
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(openid)){
            map.put("openid",openid);
        }

        map.put("page",page);
        map.put("page_num",page_num);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_COLLECTION_DIARY,params,RequestType.POST,callback,DiaryDetail.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * post params
     *
     * @param paramMap
     * @return
     */
    private String prepareParam(Map<String, Object> paramMap) {
        StringBuffer sb = new StringBuffer();
        if (paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                Object value = paramMap.get(key);
                if (null != value) {
                    if (sb.length() < 1) {
                        sb.append(key).append("=").append(value);
                    } else {
                        sb.append("&").append(key).append("=").append(value);
                    }
                }
            }
            return sb.toString();
        }
    }
}

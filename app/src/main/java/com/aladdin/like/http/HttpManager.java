package com.aladdin.like.http;

import android.text.TextUtils;

import com.aladdin.http.HttpResultProcess;
import com.aladdin.http.RequestType;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.model.UserPojo;
import com.aladdin.utils.LogUtil;
import com.zxl.network_lib.HttpUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        map.put("openid",openid);
        if (!TextUtils.isEmpty(themeName)){
            map.put("themeName",themeName);
        }

        String params = prepareParam(map);
        try{
            LogUtil.i("---url--->>>"+HttpUrl.GET_ALL_THEME);
            shortConnectRequest(HttpUrl.GET_ALL_THEME,params,RequestType.POST,callback,ThemeModes.class);
//            shortConnectRequest("http://www.mocky.io/v2/5933d9641300000e1ffa0bad","",RequestType.POST,callback,ThemeModes.class);
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
        map.put("openid",openid);
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
    public void addUserTheme(String openid, List<String> themeId,int operateType, HttpResultCallback<ThemeModes> callback){
        Map<String, Object> map = new HashMap<>();
        map.put("openid",openid);
        map.put("operateType",operateType);

        if (themeId.size() >0){
            String tempThemeId = themeId.toString();
            tempThemeId = tempThemeId.replace("[", "");
            tempThemeId = tempThemeId.replace("]", "");
            map.put("themeId",tempThemeId);
        }
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.ADD_USER_THEME,params,RequestType.POST,callback,ThemeModes.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  查看主题详情
     * @param openid
     * @param themeId
     * @param page
     * @param page_num
     * @param callback
     */
    public void getThemeDetail(String openid, String themeId, int page, int page_num, HttpResultCallback<ThemeDetail> callback){
        Map<String, Object> map = new HashMap<>();
        map.put("openid",openid);
        map.put("themeId",themeId);
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
     * 图片收藏
     * @param openid
     * @param imageId
     * @param callback
     */
    public void collectionImage(String openid,String imageId, HttpResultCallback<String> callback){
        Map<String, Object> map = new HashMap<>();
        map.put("openid",openid);
        map.put("imageId",imageId);
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
     * @param page
     * @param page_num
     * @param callback
     */
    public void getUserDiary(String openid, int page, int page_num, HttpResultCallback<DiaryDetail> callback){
        Map<String, Object> map = new HashMap<>();
        map.put("openid",openid);
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
    public void addUserDiary(String openid, String image, String diaryTitle, String diaryContent,HttpResultCallback<String> callback){

    }

    /**
     * 用户收藏图片
     * @param openid
     * @param page
     * @param page_num
     * @param callback
     */
    public void getUserCollectionImage(String openid, int page, int page_num, HttpResultCallback<CollectionImage> callback){
        Map<String, Object> map = new HashMap<>();
        map.put("openid",openid);
        map.put("page",page);
        map.put("page_num",page_num);
        String params = prepareParam(map);
        try{
            shortConnectRequest(HttpUrl.GET_USER_DIARY,params,RequestType.POST,callback,CollectionImage.class);
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

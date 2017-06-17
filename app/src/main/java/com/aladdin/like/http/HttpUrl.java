package com.aladdin.like.http;

/**
 * Description
 * Created by zxl on 2017/6/3 下午7:20.
 * Email:444288256@qq.com
 */
public class HttpUrl {
    public static final String WEIXIN_GET_CODE_FROM_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String API_URL = "http://103.228.131.203:8080/like-app/";
    //登录
    public static final String USER_LOGIN = API_URL+"wx/addUser";
    //获取全部主题
    public static final String GET_ALL_THEME = API_URL+"user/getTheme";

    //获取用户感兴趣的主题
    public static final String GET_USER_THEME = API_URL+"user/getUserTheme";

    //添加感兴趣的主题
    public static final String ADD_USER_THEME = API_URL+"user/addFollowTheme";

    //查看主题类详细图片
    public static final String GET_THEME_DETAIL = API_URL+"user/getThemeDetail";

    //图片收藏
    public static final String USER_COLLECT_THEME = API_URL+"user/collectImage";

    //日记查看
    public static final String GET_DIARY_IMAGE = API_URL+"user/getUserDiary";

    //发布日志
    public static final String USER_ADD_DIARY = API_URL+"user/addDiary";

    //用户收藏图片
    public static final String GET_USER_DIARY = API_URL+"user/getUserCollectImage";
}

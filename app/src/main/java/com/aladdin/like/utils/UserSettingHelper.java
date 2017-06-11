package com.aladdin.like.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.aladdin.utils.ContextUtils;

/**
 * Description
 * Created by zxl on 2017/6/8 下午8:13.
 * Email:444288256@qq.com
 */
public class UserSettingHelper {
    public static final String USER_PREFERENCE = "user_preference";

    private static UserSettingHelper instance = null;

    private SharedPreferences userSetting;

    private UserSettingHelper(Context context) {
        userSetting = context.getSharedPreferences(USER_PREFERENCE,
                Context.MODE_APPEND);
    }

    public static synchronized UserSettingHelper getInstance() {
        if (instance == null) {
            instance = new UserSettingHelper(ContextUtils.getInstance().getApplicationContext());
        }
        return instance;
    }

    public void setMauth(String mAuth){
        userSetting.edit().putString("Auth",mAuth).apply();
    }

    public String getMauth(){
        return userSetting.getString("Auth","");
    }
}

package com.aladdin.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.lang.reflect.Method;

/**
 * Description 封装工具
 * Created by zxl on 2017/4/26 上午10:01.
 * Email:444288256@qq.com
 */
public enum SharedPreferencesUtil {

    INSTANCE;

    private static final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ContextUtils.getInstance().getApplicationContext());

    /**
     * {@link SharedPreferences.Editor#putString(String, String)}
     */
    public void putString(String key, @Nullable String value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        SharedPreferencesCompat.apply(editor);
    }




    /**
     * {@link SharedPreferences.Editor#putInt(String, int)}
     */
    public void putInt(String key, int value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * {@link SharedPreferences.Editor#putLong(String, long)}
     */
    public void putLong(String key, long value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * {@link SharedPreferences.Editor#putFloat(String, float)}
     */
    public void putFloat(String key, float value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * {@link SharedPreferences.Editor#putBoolean(String, boolean)}
     */
    public void putBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * {@link SharedPreferences.Editor#getString(String, String)}
     */
    @Nullable
    public String getString(String key, @Nullable String defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * {@link SharedPreferences.Editor#getInt(String, int)}
     */
    public int getInt(String key, int defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * {@link SharedPreferences.Editor#getLong(String, long)}
     */
    public long getLong(String key, long defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * {@link SharedPreferences.Editor#getFloat(String, float)}
     */
    public float getFloat(String key, float defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * {@link SharedPreferences.Editor#getBoolean(String, boolean)}
     */
    public boolean getBoolean(String key, boolean defValue) {
        if (sharedPreferences == null) {
            return defValue;
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * {@link SharedPreferences#contains(String)}
     */
    public boolean contains(String key) {
        if (sharedPreferences == null) {
            return false;
        }
        return sharedPreferences.contains(key);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private static Method findApplyMethod() {
            try {
                Class<SharedPreferences.Editor> clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (Exception e) {
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        private static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
            }
            editor.commit();
        }
    }
}

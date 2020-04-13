package com.walker.core.store.sp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.tencent.mmkv.MMKV;
import com.walker.core.util.EditorUtils;
import com.walker.core.util.Utils;

import java.util.Map;

abstract class BasePreferences {
    protected static Application sApplication;

    protected SharedPreferences mPreference;

    protected MMKV mmkv;

    public BasePreferences() {
        if (Utils.isEmpty(getSPFileName())) {
            mPreference = PreferenceManager.getDefaultSharedPreferences(sApplication);
            mmkv = MMKV.defaultMMKV();
        } else {
            mPreference = sApplication.getSharedPreferences(getSPFileName(), Context.MODE_PRIVATE);
            mmkv = MMKV.mmkvWithID(getSPFileName());
        }
    }

    protected abstract String getSPFileName();

    public String getString(String key, String defValue) {
        if (mmkv != null) {
            return mmkv.getString(key, defValue);
        }
        return mPreference.getString(key, defValue);
    }

    public Boolean getBoolean(String key, boolean defBool) {
        if (mmkv != null) {
            return mmkv.getBoolean(key, defBool);
        }
        return mPreference.getBoolean(key, defBool);
    }

    public void setBoolean(String key, boolean bool) {
        if (mmkv != null) {
            mmkv.putBoolean(key, bool);
        } else {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(key, bool);
            EditorUtils.fastCommit(editor);
        }
    }

    public void setLong(String key, long value) {
        if (mmkv != null) {
            mmkv.putLong(key, value);
        } else {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putLong(key, value);
            EditorUtils.fastCommit(editor);
        }
    }

    public long getLong(String key, long defValue) {
        if (mmkv != null) {
            return mmkv.getLong(key, defValue);
        }
        return mPreference.getLong(key, defValue);
    }

    public String getString(String key) {
        if (mmkv != null) {
            return mmkv.getString(key, "");
        } else {
            return mPreference.getString(key, "");
        }
    }

    public void setString(String key, String value) {
        if (mmkv != null) {
            mmkv.putString(key, value);
        } else {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putString(key, value);
            EditorUtils.fastCommit(editor);
        }
    }

    public int getInt(String key, int defaultVal) {
        if (mmkv != null) {
            return mmkv.getInt(key, defaultVal);
        } else {
            return mPreference.getInt(key, defaultVal);
        }
    }

    public void setInt(String key, int value) {
        if (mmkv != null) {
            mmkv.putInt(key, value);
        } else {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(key, value);
            EditorUtils.fastCommit(editor);
        }
    }

    public void remove(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (mmkv != null) {
            if (mmkv.contains(key)) {
                mmkv.remove(key);
            }
        } else {
            if (mPreference.contains(key)) {
                SharedPreferences.Editor editor = mPreference.edit();
                editor.remove(key);
                EditorUtils.fastCommit(editor);
            }
        }
    }

    public boolean contains(String key) {
        if (mmkv != null) {
            return mmkv.contains(key);
        }
        return mPreference.contains(key);
    }

    public void clearAll() {
        if (mmkv != null) {
            mmkv.clearAll();
        } else {
            SharedPreferences.Editor editor = mPreference.edit();
            editor.clear();
            EditorUtils.fastCommit(editor);
        }
    }

    public Map<String, ?> getAll() {
        if (mmkv != null) {
            return mmkv.getAll();
        }
        return mPreference.getAll();
    }
}

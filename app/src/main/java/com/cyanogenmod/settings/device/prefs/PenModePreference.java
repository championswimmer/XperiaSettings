package com.cyanogenmod.settings.device.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

import com.cyanogenmod.settings.device.R;
import com.cyanogenmod.settings.device.Utils;

/**
 * Created by championswimmer on 10/2/14.
 */
public class PenModePreference extends CheckBoxPreference implements CheckBoxPreference.OnPreferenceChangeListener {

    public static String TAG = "PenModePreference";

    public static String SYSFS_PATH = null;
    public static String ENABLED_VALUE;
    public static String DISABLED_VALUE;
    public static Boolean SUPPORTED;

    private Context CONTEXT;

    public PenModePreference(final Context context, final AttributeSet attrst) {
        super(context, attrst);
        CONTEXT = context;
        SYSFS_PATH = context.getString(R.string.penmode_sysfs_file);
        ENABLED_VALUE = context.getString(R.string.penmode_enabled_value);
        DISABLED_VALUE = context.getString(R.string.penmode_enabled_value);
        SUPPORTED = context.getResources().getBoolean(R.bool.has_penmode);
    }

    @Override
    protected void onBindView(final View v) {
        super.onBindView(v);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        Boolean state = o.toString().equalsIgnoreCase("true");
        String val = getValueFromState(state);
        Utils.writeValue(SYSFS_PATH, val);
        return true;

    }

    public static void restore(Context context) {
        SYSFS_PATH = context.getString(R.string.penmode_sysfs_file);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String value = settings.getString("penmode", DISABLED_VALUE);
        Utils.writeValue(SYSFS_PATH, value);
    }

    public String getValueFromState(Boolean state) {
        if (state) {
            return ENABLED_VALUE;
        } else {
            return DISABLED_VALUE;
        }
    }

    public Boolean checkSupport() {
        Boolean fileExists = Utils.fileExists(SYSFS_PATH);
        //Log.d(TAG, "File exists : " + fileExists);
        //Log.d(TAG, "Enabled via config : " + isEnabledInConfig);
        if ((SUPPORTED && fileExists)) {
            return true;
        } else {
            setSummary(R.string.summary_unsupported);
            setEnabled(false);
            return false;
        }
    }

}

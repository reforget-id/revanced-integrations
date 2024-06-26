package app.revanced.integrations.twitter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import app.revanced.integrations.shared.settings.Setting;
import app.revanced.integrations.shared.settings.preference.SharedPrefCategory;
import app.revanced.integrations.twitter.settings.Settings;
import app.revanced.integrations.twitter.settings.BackupPrefFragment;
import app.revanced.integrations.twitter.settings.RestorePrefFragment;
import org.json.JSONObject;
import java.util.*;

import java.util.Arrays;

@SuppressWarnings("unused")
public class Utils {
    @SuppressLint("StaticFieldLeak")
    private static final Context ctx = app.revanced.integrations.shared.Utils.getContext();
    private static SharedPrefCategory sp = new SharedPrefCategory(Settings.SHARED_PREF_NAME);

    private static void startActivity(Class cls) {
        Intent intent = new Intent(ctx, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void startActivityFromClassName(String className){
        try {
            Class<?> clazz = Class.forName(className);
            startActivity(clazz);
        }catch (Exception ex) {
            toast(ex.toString());
        }
    }

    public static void startUndoPostActivity(){
        String className = "com.twitter.feature.subscriptions.settings.undotweet.UndoTweetSettingsActivity";
        startActivityFromClassName(className);
    }

    public static void startAppIconNNavIconActivity(){
        String className = "com.twitter.feature.subscriptions.settings.extras.ExtrasSettingsActivity";
        startActivityFromClassName(className);
    }

    public static void startBackupActivity(boolean featureFlag){
        Intent intent = new Intent(ctx, BackupPrefFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("featureFlag", featureFlag);
        ctx.startActivity(intent);
    }
    public static void startRestoreActivity(boolean featureFlag){
        Intent intent = new Intent(ctx, RestorePrefFragment.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("featureFlag", featureFlag);
        ctx.startActivity(intent);
    }

    public static Boolean setBooleanPerf(String key,Boolean val) {
        try{
            sp.saveBoolean(key, val);
            return true;
        }
        catch(Exception ex){
            toast(ex.toString());
        }
        return false;
    }

    public static Boolean setStringPref(String key,String val) {
        try{
            sp.saveString(key,val);
            return true;
        }
        catch(Exception ex){
            toast(ex.toString());
        }
        return false;
    }

    public static String getStringPref(Setting<String> setting) {
        String value = sp.getString(setting.key, setting.defaultValue);
        if (value.isBlank()) {
            return setting.defaultValue;
        }
        return value;
    }

    public static Boolean getBooleanPerf(Setting<Boolean> setting) {
        return sp.getBoolean(setting.key, setting.defaultValue);
    }
    public static String getAll(boolean no_flags){
        JSONObject prefs = sp.getAll();
        if (no_flags) {
            prefs.remove(Settings.MISC_FEATURE_FLAGS.key);
        }
        return prefs.toString();
    }
    public static Set<String> getSetPerf(String key,Set<String> defaultValue) {
        return sp.getSet(key, defaultValue);
    }

    public static Boolean setSetPerf(String key,Set<String> defaultValue) {
        try{
            sp.saveSet(key, defaultValue);
            return true;
        }
        catch(Exception ex){
            toast(ex.toString());
        }
        return false;
    }
    public static boolean setAll(String jsonString){
        boolean sts = false;
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);
                if(value instanceof Boolean){
                    setBooleanPerf(key,(Boolean)value);
                } else if (value instanceof String) {
                    setStringPref(key,(String)value);
                }
            }
            sts = true;
        }
        catch (Exception ex){
            toast(ex.toString());
        }
        return sts;
    }

    public static String[] addPref(String[] prefs, String pref) {
        String[] bigger = Arrays.copyOf(prefs, prefs.length+1);
        bigger[prefs.length] = pref;
        return bigger;
    }

    public static void toast(String msg){
        app.revanced.integrations.shared.Utils.showToastShort(msg);
    }

}

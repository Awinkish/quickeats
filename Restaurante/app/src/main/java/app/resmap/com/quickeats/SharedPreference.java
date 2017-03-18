package app.resmap.com.quickeats;

/**
 * Created by arwin on 3/18/17.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreference {

    public SharedPreference() {
        super();
    }

    public void save(Context context, String PREFS_NAME, String KEY, String firstname) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.putString(KEY, firstname);

        editor.apply();
    }

    public String getValue(Context context, String PREFS_NAME, String category) {
        SharedPreferences settings;
        String value;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        value = settings.getString(category, null);
        return value;
    }

    public void clearSharedPreference(Context context, String PREFS_NAME) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.apply();
    }

    public void removeValue(Context context, String PREFS_NAME,String category) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(category);
        editor.apply();
    }

}
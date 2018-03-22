package fr.g123k.fluttermixpanel;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public final class Utils {

    private Utils() {}

    public static JSONObject mapToJSONObject(Map<String, Object> map) {
        JSONObject json = new JSONObject();

        if (map != null) {
            for (String key : map.keySet()) {
                try {
                    json.put(key, map.get(key));
                } catch (JSONException e) {
                    Log.e("JSON", "Map to JSON error", e);
                }
            }
        }

        return json;
    }

}

package peterfajdiga.sszj.logic.requests;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;

public final class Constants {

    public static final String url = "https://sszj.herokuapp.com/";

    public static String buildUrl(final String feature, final String query) {
        try {
            return url + feature + "?q=" + URLEncoder.encode(query, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static RequestQueue queue = null;

    public static RequestQueue initQueue(@NonNull Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        return queue;
    }

    public static RequestQueue getQueue() {
        if (queue == null) {
            throw new RuntimeException("Queue has not yet been initialized.");
        }
        return queue;
    }
}

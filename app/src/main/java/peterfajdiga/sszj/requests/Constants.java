package peterfajdiga.sszj.requests;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class Constants {

    public static final String url = "https://sszj.herokuapp.com/";

    public static String buildUrl(String feature, String query) {
        return url + feature + "?q=" + query;
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

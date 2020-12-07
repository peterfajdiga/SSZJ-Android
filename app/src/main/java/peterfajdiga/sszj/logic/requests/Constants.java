package peterfajdiga.sszj.logic.requests;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class Constants {
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

    public static void openWebsite(final Context context, final String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}

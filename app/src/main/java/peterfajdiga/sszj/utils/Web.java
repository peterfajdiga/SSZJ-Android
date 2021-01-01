package peterfajdiga.sszj.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public final class Web {
    private Web() {}

    public static void openWebsite(final Context context, final String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}

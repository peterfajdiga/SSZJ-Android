package peterfajdiga.sszj.elements.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.material.appbar.AppBarLayout;

public class ToggleableAppBarLayout extends AppBarLayout {

    public ToggleableAppBarLayout(Context context) {
        super(context);
    }

    public ToggleableAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        System.err.println("sadf");
        return false;
    }
}

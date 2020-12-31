package peterfajdiga.sszj.elements.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ToggleableToolbarLayout extends CollapsingToolbarLayout {

    public ToggleableToolbarLayout(Context context) {
        super(context);
    }

    public ToggleableToolbarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToggleableToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}

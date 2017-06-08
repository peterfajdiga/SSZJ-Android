package peterfajdiga.sszj.elements.views;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
//        System.err.println("sadf");
        return false;
    }
}

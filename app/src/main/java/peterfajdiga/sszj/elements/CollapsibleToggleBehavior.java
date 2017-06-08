package peterfajdiga.sszj.elements;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class CollapsibleToggleBehavior extends AppBarLayout.Behavior {

    private boolean expandingEnabled = true;

    public CollapsibleToggleBehavior() {
        super();
    }

    public CollapsibleToggleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        return expandingEnabled && super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public boolean onNestedFling(final CoordinatorLayout coordinatorLayout,
                                 final AppBarLayout child, View target, float velocityX, float velocityY,
                                 boolean consumed) {
//        System.err.println("full: " + child.getTotalScrollRange());
//        System.err.println("y: " + target.getScrollY());
        final int y = target.getScrollY();
        if (y > 0 && -velocityY > y * 2.5 && expandingEnabled) {
            child.setExpanded(true);
        }
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    public void enableExpanding() {
        expandingEnabled = true;
        setDragCallback(null);
    }

    public void disableExpanding() {
        expandingEnabled = false;
        setDragCallback(disabledDragCallback);
    }



    private static final class DisabledDragCallback extends DragCallback {
        @Override
        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
            return false;
        }
    }
    private static final DisabledDragCallback disabledDragCallback = new DisabledDragCallback();
}

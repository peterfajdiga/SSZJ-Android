package peterfajdiga.sszj.elements;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class WeightedLinearLayoutManager extends LinearLayoutManager {

    private float[] weights = null;

    public WeightedLinearLayoutManager(Context context) {
        super(context);
    }
    public WeightedLinearLayoutManager(Context context, int[] weights) {
        super(context);
        setWeights(weights);
    }

    public WeightedLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
    public WeightedLinearLayoutManager(Context context, int orientation, boolean reverseLayout, int[] weights) {
        super(context, orientation, reverseLayout);
        setWeights(weights);
    }

    public WeightedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setWeights(int[] weights) {
        float total = 0.0f;
        for (int weight : weights) {
            total += weight;
        }

        if (total <= 0) {
            this.weights = null;
        } else {
            this.weights = new float[weights.length];
            for (int i = 0; i < weights.length; i++) {
                this.weights[i] = weights[i] / total;
            }
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int n = getChildCount();
        if (getOrientation() == LinearLayoutManager.HORIZONTAL) {
            final int parentWidth = getWidth();
            for (int i = 0; i < n; i++) {
                final View child = getChildAt(i);
                child.setMinimumWidth((int)(parentWidth * getWeightForChildAt(i))
                        - getLeftDecorationWidth(child)
                        - getRightDecorationWidth(child));
            }
        } else {  // orientation is vertical
            final int parentHeight = getHeight();
            for (int i = 0; i < n; i++) {
                final View child = getChildAt(i);
                child.setMinimumHeight((int)(parentHeight * getWeightForChildAt(i))
                        - getTopDecorationHeight(child)
                        - getBottomDecorationHeight(child));
            }
        }
        super.onLayoutChildren(recycler, state);
    }

    private float getWeightForChildAt(int index) {
        final int n = getChildCount();
        if (weights == null) {
            return 1.0f / n;
        }
        if (weights.length != n) {
            System.err.println("WeightedLinearLayoutManager: number of weights does not match number of children");
            return 1.0f / n;
        }
        return weights[index];
    }
}

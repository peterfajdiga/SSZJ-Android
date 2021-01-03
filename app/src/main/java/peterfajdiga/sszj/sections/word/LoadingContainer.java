package peterfajdiga.sszj.sections.word;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import peterfajdiga.sszj.R;

// TODO: add loading message
public class LoadingContainer extends FrameLayout {
    private String retry = "Retry";
    private int hiddenVisibility = View.GONE;

    private ProgressBar progressIndicator;
    private Button retryButton;
    private View content;

    public LoadingContainer(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LoadingContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LoadingContainer(@NonNull Context context, @Nullable AttributeSet attrs,
                       @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public LoadingContainer(@NonNull Context context, @Nullable AttributeSet attrs,
                            @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(final @NonNull Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingContainer);
        boolean reserveSpace = a.getBoolean(R.styleable.LoadingContainer_reserveSpace, false);
        boolean seeThrough = a.getBoolean(R.styleable.LoadingContainer_seeThrough, false);
        a.recycle();

        if (seeThrough) {
            hiddenVisibility = VISIBLE;
        } else if (reserveSpace) {
            hiddenVisibility = View.INVISIBLE;
        }

        init(context);
    }

    private void init(final @NonNull Context context) {

        // TODO: Move outside
        retry = context.getString(R.string.retry);
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                (int)context.getResources().getDimension(R.dimen.activity_horizontal_margin),
                (int)context.getResources().getDimension(R.dimen.activity_vertical_margin),
                (int)context.getResources().getDimension(R.dimen.activity_horizontal_margin),
                (int)context.getResources().getDimension(R.dimen.activity_vertical_margin)
        );
        params.gravity = Gravity.CENTER;

        progressIndicator = new LinearProgressIndicator(context);
        progressIndicator.setLayoutParams(params);
        progressIndicator.setIndeterminate(true);
        addView(progressIndicator);

        retryButton = new Button(context);
        retryButton.setLayoutParams(params);
        retryButton.setText(retry);
        addView(retryButton);
    }

    public void onLoading() {
        progressIndicator.setIndeterminate(true);

        progressIndicator.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.GONE);
        getContent().setVisibility(hiddenVisibility);
    }

    public void onLoading(final int progress, final int max) {
        progressIndicator.setIndeterminate(false);
        progressIndicator.setProgress(progress);
        progressIndicator.setMax(max);

        progressIndicator.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.GONE);
        getContent().setVisibility(hiddenVisibility);
    }

    public void onLoaded() {
        progressIndicator.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        getContent().setVisibility(View.VISIBLE);
    }

    public void onFailed() {
        progressIndicator.setVisibility(View.GONE);
        retryButton.setVisibility(View.VISIBLE);
        getContent().setVisibility(hiddenVisibility);
    }

    public void setOnRetryClickedListener(Button.OnClickListener listener) {
        retryButton.setOnClickListener(listener);
    }

    private @NonNull View getContent() {
        ensureCorrectOrder();
        return content;
    }

    public void ensureCorrectOrder() {
        if (getChildCount() != 3) {
            throw new RuntimeException("LoadingContainer must have exactly one content child");
        }
        if (getChildAt(0) == content) {
            // already OK
            return;
        }
        progressIndicator.bringToFront();
        retryButton.bringToFront();
        content = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ensureCorrectOrder();
        super.onLayout(changed, left, top, right, bottom);
    }

    public State getState() {
        if (progressIndicator.getVisibility() == View.VISIBLE) {
            return State.Loading;
        }
        if (retryButton.getVisibility() == View.VISIBLE) {
            return State.Failed;
        }
        if (getContent().getVisibility() == View.VISIBLE) {
            return State.Loaded;
        }
        return State.Invalid;
    }

    public enum State{
        Invalid,
        Loading,
        Loaded,
        Failed,
    }
}

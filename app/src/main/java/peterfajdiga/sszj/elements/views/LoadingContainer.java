package peterfajdiga.sszj.elements.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import peterfajdiga.sszj.R;

public class LoadingContainer extends FrameLayout {

    private String retry = "Retry";

    private ProgressBar view_spinner;
    private Button retryButton;

    public LoadingContainer(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LoadingContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingContainer(@NonNull Context context, @Nullable AttributeSet attrs,
                       @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoadingContainer(@NonNull Context context, @Nullable AttributeSet attrs,
                            @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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

        view_spinner = new ProgressBar(context);
        view_spinner.setLayoutParams(params);
        addView(view_spinner, 0);

        retryButton = new Button(context);
        retryButton.setLayoutParams(params);
        retryButton.setText(retry);
        addView(retryButton, 0);
    }

    public void onLoading() {
        view_spinner.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.GONE);
        getContent().setVisibility(View.GONE);
    }

    public void onLoaded() {
        view_spinner.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
        getContent().setVisibility(View.VISIBLE);
    }

    public void onFailed() {
        view_spinner.setVisibility(View.GONE);
        retryButton.setVisibility(View.VISIBLE);
        getContent().setVisibility(View.GONE);
    }

    public void setOnRetryClickedListener(Button.OnClickListener listener) {
        retryButton.setOnClickListener(listener);
    }



    private @NonNull View getContent() {
        if (getChildCount() != 3) {
            throw new RuntimeException("LoadingContainer must have exactly one child");
        }
        return getChildAt(2);
    }
}

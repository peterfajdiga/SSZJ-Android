package peterfajdiga.sszj.sections;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.pojo.Word;
import peterfajdiga.sszj.requests.Constants;
import peterfajdiga.sszj.requests.WordRequest;


public class WordFragment extends SectionFragment implements WordRequest.Owner {

    public static final String BUNDLE_KEY_WORD = "BUNDLE_KEY_WORD";

    private String word;
    private View self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = this.getArguments();
        if (args != null) {
            word = args.getString(BUNDLE_KEY_WORD);
        }

        final RequestQueue queue = Constants.initQueue(getContext());
        final WordRequest request = new WordRequest(this, word);
        // TODO: Cancel request when closing fragment
        queue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        self = inflater.inflate(R.layout.fragment_word, container, false);
        return self;
    }

    @Override
    protected String getTitle() {
        final Bundle args = this.getArguments();
        if (args != null) {
            word = args.getString(BUNDLE_KEY_WORD);
        }
        return word;
    }

    @Override
    public void onWordLoaded(Word word) {
        final TextView definitionView = (TextView)self.findViewById(R.id.definition_view);
        definitionView.setText(word.definition);
    }

    @Override
    public void onWordFailed() {
        removeLoadingSpinner();
    }

    @Override
    public void onWordAnimationLoaded(AnimationDrawable animation) {
        removeLoadingSpinner();

        final ImageView animationView = (ImageView)self.findViewById(R.id.animation_view);
        animationView.setImageDrawable(animation);
        animation.start();
    }

    @Override
    public void onWordAnimationFailed() {
        removeLoadingSpinner();
        final ImageView loadingFailedIcon = (ImageView)self.findViewById(R.id.loading_failed_icon);
        loadingFailedIcon.setVisibility(View.VISIBLE);
    }

    private void removeLoadingSpinner() {
        final ProgressBar spinner = (ProgressBar)self.findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);
    }
}

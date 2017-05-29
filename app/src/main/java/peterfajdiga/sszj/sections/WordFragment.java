package peterfajdiga.sszj.sections;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.AppCompatTextView;

import com.android.volley.RequestQueue;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.WordButton;
import peterfajdiga.sszj.pojo.Word;
import peterfajdiga.sszj.requests.Constants;
import peterfajdiga.sszj.requests.DefinitionRequest;
import peterfajdiga.sszj.requests.WordRequest;


public class WordFragment extends SectionFragment implements
        WordRequest.Owner,
        DefinitionRequest.Owner {

    public static final String BUNDLE_KEY_WORD = "BUNDLE_KEY_WORD";

    private String word;
    private View self;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        self = inflater.inflate(R.layout.fragment_word, container, false);
        return self;
    }

    @Override
    protected void init() {
        final Bundle args = this.getArguments();
        if (args != null) {
            word = args.getString(BUNDLE_KEY_WORD);
        }

        final RequestQueue queue = Constants.initQueue(getContext());
        final WordRequest request = new WordRequest(this, word);
        final DefinitionRequest request_def = new DefinitionRequest(this, word);
        queue.add(request);
        queue.add(request_def);
        // TODO: Cancel requests when closing fragment
    }

    @Override
    protected String getTitle() {
        return word;
    }

    @Override
    public void onWordLoaded(Word word) {
        final AppCompatTextView baseText = (AppCompatTextView)self.findViewById(R.id.word_base_text);
        switch (word.base.length) {
            case 0: {
                baseText.setText(getString(R.string.word_base_0));
                break;
            }
            case 1: {
                baseText.setText(getString(R.string.word_base_1));
                break;
            }
            default: {
                baseText.setText(getString(R.string.word_base_2));
                break;
            }
        }

        final LinearLayoutCompat container = (LinearLayoutCompat)self.findViewById(R.id.container_main);
        int insertionIndex = container.indexOfChild(baseText) + 1;
        for (String baseWord : word.base) {
            final WordButton wordButton = new WordButton(getContext());
            wordButton.setText(baseWord);
            container.addView(wordButton, insertionIndex);
            insertionIndex++;
        }
    }

    @Override
    public void onWordFailed() {
        removeSpinnerAnimation();
        final AppCompatTextView baseText = (AppCompatTextView)self.findViewById(R.id.word_base_text);
        baseText.setText(getString(R.string.word_base_error));
    }

    @Override
    public void onWordDefinitionLoaded(Spanned definition) {
        removeSpinnerDefinition();
        final AppCompatTextView definitionView = (AppCompatTextView)self.findViewById(R.id.definition_view);
        definitionView.setText(definition);
        definitionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWordDefinitionFailed() {
        removeSpinnerDefinition();
        final AppCompatTextView definitionView = (AppCompatTextView)self.findViewById(R.id.definition_view);
        definitionView.setText(getString(R.string.definition_error));
        definitionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWordAnimationLoaded(AnimationDrawable animation) {
        removeSpinnerAnimation();
        final ImageView animationView = (ImageView)self.findViewById(R.id.animation_view);
        animationView.setImageDrawable(animation);
        animation.start();
    }

    @Override
    public void onWordAnimationFailed() {
        removeSpinnerAnimation();
        final ImageView loadingFailedIcon = (ImageView)self.findViewById(R.id.loading_failed_icon);
        loadingFailedIcon.setVisibility(View.VISIBLE);
    }

    private void removeSpinnerAnimation() {
        final View spinner = self.findViewById(R.id.loading_spinner_animation);
        spinner.setVisibility(View.GONE);
    }

    private void removeSpinnerDefinition() {
        final View spinner = self.findViewById(R.id.loading_spinner_definition);
        spinner.setVisibility(View.GONE);
    }
}

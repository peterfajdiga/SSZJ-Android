package peterfajdiga.sszj.sections;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.views.LoadingContainer;
import peterfajdiga.sszj.views.WordButton;
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
        loadWord();
        loadDefinition();

        // setup retry buttons
        final View.OnClickListener retryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWord();
            }
        };
        final LoadingContainer loadingContainerMain = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainerMain.setOnRetryClickedListener(retryListener);
        final LoadingContainer loadingContainerAnimation = (LoadingContainer)self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.setOnRetryClickedListener(retryListener);

        // setup retry button for definition
        final LoadingContainer loadingContainerDefinition = (LoadingContainer)self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.setOnRetryClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefinition();
            }
        });
    }

    private void loadWord() {
        final LoadingContainer loadingContainerMain = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainerMain.onLoading();
        final LoadingContainer loadingContainerAnimation = (LoadingContainer)self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onLoading();

        final RequestQueue queue = Constants.initQueue(getContext());
        final WordRequest request = new WordRequest(this, word);
        queue.add(request);
    }

    private void loadDefinition() {
        final LoadingContainer loadingContainerDefinition = (LoadingContainer)self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.onLoading();

        final RequestQueue queue = Constants.initQueue(getContext());
        final DefinitionRequest request = new DefinitionRequest(this, word);
        queue.add(request);
    }

    @Override
    protected String getTitle() {
        return word;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Constants.initQueue(getContext()).cancelAll(this);
    }

    @Override
    public void onWordLoaded(Word word) {
        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.onLoaded();

        final TextView baseText = (TextView)self.findViewById(R.id.word_base_text);
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

        final LinearLayout container = (LinearLayout)self.findViewById(R.id.container_main);
        int insertionIndex = container.indexOfChild(baseText) + 1;
        for (String baseWord : word.base) {
            container.addView(new WordButton(getContext(), baseWord), insertionIndex);
            insertionIndex++;
        }
    }

    @Override
    public void onWordFailed() {
        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.onFailed();
    }

    @Override
    public void onWordAnimationLoaded(AnimationDrawable animation) {
        final LoadingContainer loadingContainerAnimation = (LoadingContainer)self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onLoaded();

        final ImageView animationView = (ImageView)self.findViewById(R.id.animation_view);
        animationView.setImageDrawable(animation);
        animation.start();
    }

    @Override
    public void onWordAnimationFailed() {
        final LoadingContainer loadingContainerAnimation = (LoadingContainer)self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onFailed();
    }

    @Override
    public void onWordDefinitionLoaded(Spanned definition) {
        final LoadingContainer loadingContainerDefinition = (LoadingContainer)self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.onLoaded();

        final TextView definitionView = (TextView)self.findViewById(R.id.definition_view);
        definitionView.setText(definition);
        definitionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWordDefinitionFailed() {
        final LoadingContainer loadingContainerDefinition = (LoadingContainer)self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.onFailed();
    }
}

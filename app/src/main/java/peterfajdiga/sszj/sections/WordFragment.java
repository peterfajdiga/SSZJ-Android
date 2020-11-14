package peterfajdiga.sszj.sections;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import peterfajdiga.sszj.elements.adapters.LettersAdapter;
import peterfajdiga.sszj.elements.adapters.OnWordClickedListener;
import peterfajdiga.sszj.R;
import peterfajdiga.sszj.logic.AnimationBuilder;
import peterfajdiga.sszj.logic.ReportingAnimationDrawable;
import peterfajdiga.sszj.elements.DividerItemDecorationNoLast;
import peterfajdiga.sszj.elements.views.LoadingContainer;
import peterfajdiga.sszj.logic.words.Word;
import peterfajdiga.sszj.logic.requests.Constants;
import peterfajdiga.sszj.logic.requests.DefinitionRequest;
import peterfajdiga.sszj.elements.WeightedLinearLayoutManager;
import peterfajdiga.sszj.elements.adapters.WordsAdapter;
import peterfajdiga.sszj.obb.InvalidStateException;
import peterfajdiga.sszj.obb.ObbLoader;

public class WordFragment extends SectionFragment implements
        DefinitionRequest.Owner,
        ReportingAnimationDrawable.OnFrameListener {

    public static final String BUNDLE_KEY_WORD = "BUNDLE_KEY_WORD";

    private Word word;
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
            word = Word.wordMap.get(args.getString(BUNDLE_KEY_WORD));
        }

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

        // setup word_base_container
        final RecyclerView wordBaseContainer = (RecyclerView)self.findViewById(R.id.word_base_container);
        wordBaseContainer.setLayoutManager(new WeightedLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        final DividerItemDecorationNoLast dividerItemDecoration = new DividerItemDecorationNoLast(getContext(), LinearLayoutManager.HORIZONTAL);
        wordBaseContainer.addItemDecoration(dividerItemDecoration);

        // setup spelling container
        final RecyclerView spellingContainer = (RecyclerView)self.findViewById(R.id.spelling_container);
        spellingContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadWord();
        loadDefinition();
    }

    private void loadWord() {
        final LoadingContainer loadingContainerMain = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainerMain.onLoading();
        final LoadingContainer loadingContainerAnimation = (LoadingContainer)self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onLoading();

        onWordLoaded(word);

        try {
            final ObbLoader obbLoader = new ObbLoader(getContext());
            final String[] gestureFiles = word.getGestureFiles();
            final Bitmap[] bitmaps = new Bitmap[gestureFiles.length];
            for (int i = 0; i < gestureFiles.length; i++) {
                bitmaps[i] = obbLoader.getBitmap(gestureFiles[i]);
            }
            onWordAnimationLoaded(AnimationBuilder.build(bitmaps), AnimationBuilder.getFrameCounts(bitmaps));
        } catch (final InvalidStateException e) {
            e.printStackTrace();
            onWordAnimationFailed();
        }
    }

    private void loadDefinition() {
        final LoadingContainer loadingContainerDefinition = (LoadingContainer)self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.onLoading();

        final RequestQueue queue = Constants.initQueue(getContext());
        final DefinitionRequest request = new DefinitionRequest(this, word.word);
        queue.add(request);
    }

    @Override
    protected String getTitle() {
        return word.word;
    }

    @Override
    protected int getDrawerItemId() {
        return 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Constants.initQueue(getContext()).cancelAll(this);
    }

    // TODO: refactor
    public void onWordLoaded(final Word word) {
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
                showBaseWords(word.base);
                break;
            }
            default: {
                baseText.setText(getString(R.string.word_base_2));
                showBaseWords(word.base);
                break;
            }
        }

        if (word.word.length() > 1) {
            // cast context
            final Context context = getContext();
            final OnWordClickedListener mainActivity;
            if (context instanceof OnWordClickedListener) {
                mainActivity = (OnWordClickedListener)context;
            } else {
                throw new RuntimeException(context.toString()
                        + "must implement OnWordClickedListener");
            }

            // show letters
            final RecyclerView spellingContainer = (RecyclerView)self.findViewById(R.id.spelling_container);
            spellingContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            final LettersAdapter adapter = new LettersAdapter(R.layout.card_letter_fixed, word.word);
            adapter.setOnWordClickedListener(mainActivity);
            spellingContainer.setAdapter(adapter);
            spellingContainer.setVisibility(View.VISIBLE);
        }
    }

    private void showBaseWords(String[] words) {
        // cast context
        final Context context = getContext();
        final OnWordClickedListener mainActivity;
        if (context instanceof OnWordClickedListener) {
            mainActivity = (OnWordClickedListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnWordClickedListener");
        }

        // set visibility
        final View animationProgress = self.findViewById(R.id.animation_progress);
        animationProgress.setVisibility(View.VISIBLE);
        final RecyclerView wordBaseContainer = (RecyclerView)self.findViewById(R.id.word_base_container);
        wordBaseContainer.setVisibility(View.VISIBLE);

        // show base words
        final WordsAdapter adapter = new WordsAdapter(R.layout.item_word_horizontal, words);
        adapter.setOnWordClickedListener(mainActivity);
        wordBaseContainer.setAdapter(adapter);
    }

    // TODO: refactor
    public void onWordAnimationLoaded(ReportingAnimationDrawable animation, int[] frameCounts) {
        final LoadingContainer loadingContainerAnimation = (LoadingContainer)self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onLoaded();

        final ImageView animationView = (ImageView)self.findViewById(R.id.animation_view);
        animationView.setImageDrawable(animation);
        animation.setOneShot(false);
        animation.start();

        if (frameCounts.length > 1) {
            animation.setOnFrameListener(this);
            final ProgressBar animationProgress = (ProgressBar)self.findViewById(R.id.animation_progress);
            animationProgress.setMax(animation.getNumberOfFrames() - 1);
        }

        final RecyclerView wordBaseContainer = (RecyclerView)self.findViewById(R.id.word_base_container);
        final WeightedLinearLayoutManager layoutManager = (WeightedLinearLayoutManager)wordBaseContainer.getLayoutManager();
        layoutManager.setWeights(frameCounts);
        layoutManager.requestLayout();
    }

    // TODO: refactor
    public void onWordAnimationFailed() {
        final LoadingContainer loadingContainerAnimation = (LoadingContainer)self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onFailed();
    }

    @Override
    public void onFrame(int index) {
        final ProgressBar animationProgress = (ProgressBar)self.findViewById(R.id.animation_progress);
        animationProgress.setProgress(index);
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

package peterfajdiga.sszj.sections.word;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import peterfajdiga.sszj.recycler.adapters.LettersAdapter;
import peterfajdiga.sszj.recycler.adapters.OnWordClickedListener;
import peterfajdiga.sszj.R;
import peterfajdiga.sszj.recycler.DividerItemDecorationNoLast;
import peterfajdiga.sszj.words.Words;
import peterfajdiga.sszj.words.CombinedWord;
import peterfajdiga.sszj.words.Word;
import peterfajdiga.sszj.recycler.WeightedLinearLayoutManager;
import peterfajdiga.sszj.recycler.adapters.WordsAdapter;
import peterfajdiga.sszj.obb.ObbLoader;
import peterfajdiga.sszj.obb.ObbMounter;
import peterfajdiga.sszj.sections.SectionFragment;

public class WordFragment extends SectionFragment implements
        DefinitionRequest.Owner,
        ReportingAnimationDrawable.OnFrameListener {

    public static final String BUNDLE_KEY_WORD = "BUNDLE_KEY_WORD";

    private RequestQueue volleyQueue;

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
            word = Words.WORD_MAP.get(args.getString(BUNDLE_KEY_WORD).toLowerCase());
        }

        // setup retry buttons
        final View.OnClickListener retryListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWord();
            }
        };
        final LoadingContainer loadingContainerMain = self.findViewById(R.id.loading_container_main);
        loadingContainerMain.setOnRetryClickedListener(retryListener);
        final LoadingContainer loadingContainerAnimation = self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.setOnRetryClickedListener(retryListener);

        // setup retry button for definition
        final LoadingContainer loadingContainerDefinition = self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.setOnRetryClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefinition();
            }
        });

        // setup word_base_container
        final RecyclerView wordBaseContainer = self.findViewById(R.id.word_base_container);
        wordBaseContainer.setLayoutManager(new WeightedLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        final DividerItemDecorationNoLast dividerItemDecoration = new DividerItemDecorationNoLast(getContext(), LinearLayoutManager.HORIZONTAL);
        wordBaseContainer.addItemDecoration(dividerItemDecoration);

        // setup spelling container
        final RecyclerView spellingContainer = self.findViewById(R.id.spelling_container);
        spellingContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        loadWord();
        loadDefinition();
    }

    private void loadWord() {
        final LoadingContainer loadingContainerMain = self.findViewById(R.id.loading_container_main);
        loadingContainerMain.onLoaded();

        showBaseText();
        showSpelling();
    }

    private void loadAnimation() {
        final LoadingContainer loadingContainerAnimation = self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onLoading();

        final Context context = getContext();
        if (!(context instanceof Activity)) {
            throw new RuntimeException("Context must be an Activity");
        }
        final Activity activity = (Activity)context;

        final ObbMounter obbMounter = new ObbMounter(context);
        obbMounter.init(new ObbMounter.OnObbMountedListener() {
            @Override
            public void onObbMounted(final ObbLoader obbLoader) {
                final String[] gestureFiles = word.getGestureFiles();
                final Bitmap[] bitmaps = new Bitmap[gestureFiles.length];
                for (int i = 0; i < gestureFiles.length; i++) {
                    bitmaps[i] = obbLoader.getBitmap(gestureFiles[i]);
                }
                showWordAnimation(AnimationBuilder.build(bitmaps), AnimationBuilder.getFrameCounts(bitmaps));
            }

            @Override
            public void onObbFailure() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingContainerAnimation.onFailed();
                    }
                });
            }

            @Override
            public void onObbDownloadProgress(final int bytesDownloaded, final int bytesTotal) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingContainerAnimation.onLoading(bytesDownloaded, bytesTotal);
                    }
                });
            }

            @Override
            public boolean shouldKeepListening() {
                return loadingContainerAnimation.isShown();
            }
        });
    }

    private void loadDefinition() {
        final LoadingContainer loadingContainerDefinition = self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.onLoading();

        final String definitionUrl = getDefinitionUrl();

        if (volleyQueue == null) {
            volleyQueue = Volley.newRequestQueue(getContext());
        }
        final DefinitionRequest request = new DefinitionRequest(this, definitionUrl + "&View=3");
        volleyQueue.add(request);

        final View definitionContainer = self.findViewById(R.id.card_definition);
        definitionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent definitionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(definitionUrl));
                startActivity(definitionIntent);
            }
        });
    }

    @Override
    protected String getTitle() {
        return word.getHeadword();
    }

    @Override
    protected int getDrawerItemId() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAnimation();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (volleyQueue != null) {
            volleyQueue.cancelAll(this);
        }
    }

    private void showBaseText() {
        final TextView baseText = self.findViewById(R.id.word_base_text);
        if (word instanceof CombinedWord) {
            final String[] base = ((CombinedWord)word).getBaseHeadwords();
            switch (base.length) {
                case 0: {
                    assert false;
                    baseText.setText(getString(R.string.word_base_0));
                    break;
                }
                case 1: {
                    baseText.setText(getString(R.string.word_base_1));
                    showBaseWords(base);
                    break;
                }
                default: {
                    baseText.setText(getString(R.string.word_base_2));
                    showBaseWords(base);
                    break;
                }
            }
        } else {
            baseText.setText(getString(R.string.word_base_0));
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
        final RecyclerView wordBaseContainer = self.findViewById(R.id.word_base_container);
        wordBaseContainer.setVisibility(View.VISIBLE);

        // show base words
        final WordsAdapter adapter = new WordsAdapter(R.layout.item_word_horizontal, words);
        adapter.setOnWordClickedListener(mainActivity);
        wordBaseContainer.setAdapter(adapter);
    }

    private void showSpelling() {
        if (word.getHeadword().length() > 1) {
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
            final RecyclerView spellingContainer = self.findViewById(R.id.spelling_container);
            spellingContainer.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            final LettersAdapter adapter = new LettersAdapter(R.layout.card_letter_fixed, word.getHeadword());
            adapter.setOnWordClickedListener(mainActivity);
            spellingContainer.setAdapter(adapter);
            spellingContainer.setVisibility(View.VISIBLE);
        }
    }

    private void showWordAnimation(ReportingAnimationDrawable animation, int[] frameCounts) {
        final LoadingContainer loadingContainerAnimation = self.findViewById(R.id.loading_container_animation);
        loadingContainerAnimation.onLoaded();

        final ImageView animationView = self.findViewById(R.id.animation_view);
        animationView.setImageDrawable(animation);
        animation.setOneShot(false);
        animation.start();

        if (frameCounts.length > 1) {
            animation.setOnFrameListener(this);
            final ProgressBar animationProgress = self.findViewById(R.id.animation_progress);
            animationProgress.setMax(animation.getNumberOfFrames() - 1);
        }

        final RecyclerView wordBaseContainer = self.findViewById(R.id.word_base_container);
        final WeightedLinearLayoutManager layoutManager = (WeightedLinearLayoutManager)wordBaseContainer.getLayoutManager();
        layoutManager.setWeights(frameCounts);
        layoutManager.requestLayout();
    }

    @Override
    public void onFrame(int index) {
        final ProgressBar animationProgress = self.findViewById(R.id.animation_progress);
        animationProgress.setProgress(index);
    }

    @Override
    public void onWordDefinitionLoaded(CharSequence definition) {
        final LoadingContainer loadingContainerDefinition = self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.onLoaded();

        final TextView definitionView = self.findViewById(R.id.definition_view);
        definitionView.setText(definition);
        definitionView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onWordDefinitionFailed() {
        final LoadingContainer loadingContainerDefinition = self.findViewById(R.id.loading_container_definition);
        loadingContainerDefinition.onFailed();
    }

    private String getDefinitionUrl() {
        return "https://www.fran.si/iskanje?FilteredDictionaryIds=133&Headword=" + word.getHeadword();
    }
}

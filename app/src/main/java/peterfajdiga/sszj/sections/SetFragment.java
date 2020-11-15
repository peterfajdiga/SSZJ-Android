package peterfajdiga.sszj.sections;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;

import peterfajdiga.sszj.elements.adapters.OnWordClickedListener;
import peterfajdiga.sszj.R;
import peterfajdiga.sszj.elements.DividerItemDecorationNoLast;
import peterfajdiga.sszj.elements.adapters.WordsAdapter;
import peterfajdiga.sszj.logic.sets.Set;
import peterfajdiga.sszj.logic.requests.Constants;
import peterfajdiga.sszj.logic.requests.SetRequest;
import peterfajdiga.sszj.elements.views.LoadingContainer;
import peterfajdiga.sszj.logic.sets.Sets;

public class SetFragment extends SectionFragment implements SetRequest.Owner {

    public static final String BUNDLE_KEY_SET = "BUNDLE_KEY_SET";

    private Set set;
    private View self;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        self = inflater.inflate(R.layout.fragment_set, container, false);
        return self;
    }

    @Override
    protected void init() {
        final Bundle args = this.getArguments();
        if (args != null) {
            final String setLabel = args.getString(BUNDLE_KEY_SET);
            set = Sets.SET_MAP.get(setLabel);
        }
        loadSet();

        // setup retry button
        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.setOnRetryClickedListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSet();
            }
        });

        // setup container
        final RecyclerView container = (RecyclerView)self.findViewById(R.id.container_main);
        container.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        final DividerItemDecorationNoLast dividerItemDecoration = new DividerItemDecorationNoLast(getContext(), LinearLayoutManager.VERTICAL);
        container.addItemDecoration(dividerItemDecoration);
    }

    private void loadSet() {
        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.onLoading();

        final RequestQueue queue = Constants.initQueue(getContext());
        final SetRequest request = new SetRequest(this, set.getKeyword());
        queue.add(request);
    }

    @Override
    protected String getTitle() {
        return set.label;
    }

    @Override
    protected int getDrawerItemId() {
        return R.id.nav_sets;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Constants.initQueue(getContext()).cancelAll(this);
    }


    @Override
    public void onSetLoaded(String[] words) {
        // cast context
        final Context context = getContext();
        final OnWordClickedListener mainActivity;
        if (context instanceof OnWordClickedListener) {
            mainActivity = (OnWordClickedListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnWordClickedListener");
        }

        // show loaded words
        final RecyclerView container = (RecyclerView)self.findViewById(R.id.container_main);
        final WordsAdapter adapter = new WordsAdapter(R.layout.item_word_vertical, words);
        adapter.setOnWordClickedListener(mainActivity);
        container.setAdapter(adapter);

        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.onLoaded();
    }

    @Override
    public void onSetFailed() {
        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.onFailed();
    }
}

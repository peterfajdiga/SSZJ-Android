package peterfajdiga.sszj.sections;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import peterfajdiga.sszj.elements.recycler.adapters.OnWordClickedListener;
import peterfajdiga.sszj.R;
import peterfajdiga.sszj.elements.recycler.DividerItemDecorationNoLast;
import peterfajdiga.sszj.elements.recycler.adapters.WordsAdapter;
import peterfajdiga.sszj.logic.sets.Set;
import peterfajdiga.sszj.logic.sets.Sets;

public class SetFragment extends SectionFragment {

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

        // setup container
        final RecyclerView container = self.findViewById(R.id.container_main);
        container.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        final DividerItemDecorationNoLast dividerItemDecoration = new DividerItemDecorationNoLast(getContext(), LinearLayoutManager.VERTICAL);
        container.addItemDecoration(dividerItemDecoration);
    }

    private void loadSet() {
        final Context context = getContext();
        final OnWordClickedListener mainActivity;
        if (context instanceof OnWordClickedListener) {
            mainActivity = (OnWordClickedListener)context;
        } else {
            throw new RuntimeException(context.toString()
                + "must implement OnWordClickedListener");
        }

        final RecyclerView container = self.findViewById(R.id.container_main);
        final WordsAdapter adapter = new WordsAdapter(R.layout.item_word_vertical, set.words);
        adapter.setOnWordClickedListener(mainActivity);
        container.setAdapter(adapter);
    }

    @Override
    protected String getTitle() {
        return set.label;
    }

    @Override
    protected int getDrawerItemId() {
        return R.id.nav_sets;
    }
}

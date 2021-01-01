package peterfajdiga.sszj.sections;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.recycler.GridAutofitLayoutManager;
import peterfajdiga.sszj.recycler.adapters.SetsAdapter;
import peterfajdiga.sszj.logic.sets.Sets;

public class SetsFragment extends SectionFragment {
    private View self;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        self = inflater.inflate(R.layout.fragment_sets, container, false);
        return self;
    }

    @Override
    protected void init() {
        // cast context
        final Context context = getContext();
        final SetsAdapter.OnSetClickedListener mainActivity;
        if (context instanceof SetsAdapter.OnSetClickedListener) {
            mainActivity = (SetsAdapter.OnSetClickedListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement SetsAdapter.OnSetClickedListener");
        }

        // setup container
        final RecyclerView container = (RecyclerView)self.findViewById(R.id.container_main);
        final int setWidth = getResources().getDimensionPixelSize(R.dimen.set_image_size);
        container.setLayoutManager(new GridAutofitLayoutManager(getContext(), setWidth));
        final SetsAdapter adapter = new SetsAdapter(Sets.SETS);
        adapter.setOnSetClickedListener(mainActivity);
        container.setAdapter(adapter);
    }

    @Override
    protected String getTitle() {
        return getContext().getString(R.string.nav_sets);
    }

    @Override
    protected int getDrawerItemId() {
        return R.id.nav_sets;
    }
}

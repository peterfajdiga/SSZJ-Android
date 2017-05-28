package peterfajdiga.sszj.sections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peterfajdiga.sszj.R;


public class SpellingFragment extends SectionFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spelling, container, false);
    }

    @Override
    protected String getTitle() {
        return getContext().getString(R.string.nav_spelling);
    }
}

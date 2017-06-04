package peterfajdiga.sszj.sections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.pojo.Set;

public class SetFragment extends SectionFragment {

    public static final String BUNDLE_KEY_SET = "BUNDLE_KEY_SET";

    private Set set;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    protected void init() {
        final Bundle args = this.getArguments();
        if (args != null) {
            set = (Set)args.getSerializable(BUNDLE_KEY_SET);
        }
    }

    @Override
    protected String getTitle() {
        return set.label;
    }
}

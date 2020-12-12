package peterfajdiga.sszj.sections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.logic.requests.Constants;

public class AboutFragment extends SectionFragment {

    private View self;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        self = inflater.inflate(R.layout.fragment_about, container, false);
        return self;
    }

    @Override
    protected void init() {
        final Button aboutWebLink = self.findViewById(R.id.about_web_link);
        aboutWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.openWebsite(getContext(), "http://sszj.fri.uni-lj.si/?stran=staticno.prva");
            }
        });
    }

    @Override
    protected String getTitle() {
        return getContext().getString(R.string.nav_about);
    }

    @Override
    protected int getDrawerItemId() {
        return R.id.nav_about;
    }
}

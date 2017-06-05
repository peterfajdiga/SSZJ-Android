package peterfajdiga.sszj.sections;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.pojo.Set;
import peterfajdiga.sszj.requests.Constants;
import peterfajdiga.sszj.requests.SetRequest;
import peterfajdiga.sszj.views.LoadingContainer;
import peterfajdiga.sszj.views.WordButton;

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
            set = (Set)args.getSerializable(BUNDLE_KEY_SET);
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
    public void onDetach() {
        super.onDetach();
        Constants.initQueue(getContext()).cancelAll(this);
    }


    @Override
    public void onSetLoaded(String[] words) {
        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.onLoaded();

        final ViewGroup wordsContainer = (ViewGroup)self.findViewById(R.id.container_main);
        for (String word : words) {
            wordsContainer.addView(new WordButton(getContext(), word));
        }
    }

    @Override
    public void onSetFailed() {
        final LoadingContainer loadingContainer = (LoadingContainer)self.findViewById(R.id.loading_container_main);
        loadingContainer.onFailed();
    }
}

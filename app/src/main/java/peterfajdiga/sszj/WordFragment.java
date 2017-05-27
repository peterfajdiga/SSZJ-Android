package peterfajdiga.sszj;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;

import peterfajdiga.sszj.pojo.Word;
import peterfajdiga.sszj.volley.Constants;
import peterfajdiga.sszj.volley.WordRequest;


public class WordFragment extends SectionFragment implements WordRequest.Owner {

    public static final String BUNDLE_KEY_WORD = "BUNDLE_KEY_WORD";

    private String word;
    private View self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle args = this.getArguments();
        if (args != null) {
            word = args.getString(BUNDLE_KEY_WORD);
        }

        final RequestQueue queue = Constants.initQueue(getContext());
        final WordRequest request = new WordRequest(this, word);
        // TODO: Cancel request when closing fragment
        queue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        self = inflater.inflate(R.layout.fragment_word, container, false);
        return self;
    }

    @Override
    protected String getTitle() {
        final Bundle args = this.getArguments();
        if (args != null) {
            word = args.getString(BUNDLE_KEY_WORD);
        }
        return word;
    }

    @Override
    public void onWordLoaded(Word word) {
        final ImageView animationView = (ImageView)self.findViewById(R.id.animation_view);
        animationView.setImageDrawable(word.animation);
        word.animation.start();
    }
}

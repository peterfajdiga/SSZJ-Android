package peterfajdiga.sszj;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class WordFragment extends SectionFragment {

    public static final String BUNDLE_KEY_WORD = "BUNDLE_KEY_WORD";

    private String word;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            word = bundle.getString(BUNDLE_KEY_WORD);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false);
    }

    @Override
    protected String getTitle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            word = bundle.getString(BUNDLE_KEY_WORD);
        }
        return word;
    }
}

package peterfajdiga.sszj.sections;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.elements.GridAutofitLayoutManager;
import peterfajdiga.sszj.elements.adapters.LettersAdapter;
import peterfajdiga.sszj.elements.adapters.OnWordClickedListener;
import peterfajdiga.sszj.elements.adapters.WordsAdapter;

import static peterfajdiga.sszj.sections.WordFragment.BUNDLE_KEY_WORD;


public class SpellingFragment extends SectionFragment {

    private View self;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        self = inflater.inflate(R.layout.fragment_spelling, container, false);
        return self;
    }

    @Override
    protected void init() {
        // cast context
        final Context context = getContext();
        final OnWordClickedListener mainActivity;
        if (context instanceof OnWordClickedListener) {
            mainActivity = (OnWordClickedListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnWordClickedListener");
        }

        final RecyclerView letterContainer = (RecyclerView)self.findViewById(R.id.letter_container);
        final int letterWidth = getResources().getDimensionPixelSize(R.dimen.letter_image_width);
        letterContainer.setLayoutManager(new GridAutofitLayoutManager(context, letterWidth));
        final LettersAdapter lettersAdapter = new LettersAdapter();
        letterContainer.setAdapter(lettersAdapter);

        final TextView input = (TextView)self.findViewById(R.id.editText);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lettersAdapter.setWord(s.toString());
                lettersAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Bundle args = this.getArguments();
        if (args != null) {
            final String word = args.getString(BUNDLE_KEY_WORD);
            input.setText(word.toUpperCase().replaceAll("\\s+",""));
        }
    }

    @Override
    protected String getTitle() {
        return getContext().getString(R.string.nav_spelling);
    }
}

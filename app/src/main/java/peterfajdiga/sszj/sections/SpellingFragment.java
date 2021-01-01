package peterfajdiga.sszj.sections;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.elements.recycler.GridAutofitLayoutManager;
import peterfajdiga.sszj.elements.recycler.adapters.LettersAdapter;
import peterfajdiga.sszj.elements.recycler.adapters.OnWordClickedListener;

import static peterfajdiga.sszj.sections.WordFragment.BUNDLE_KEY_WORD;


public class SpellingFragment extends SectionFragment implements OnWordClickedListener {

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
        final RecyclerView letterContainer = (RecyclerView)self.findViewById(R.id.letter_container);
        final int letterWidth = getResources().getDimensionPixelSize(R.dimen.letter_image_size);
        letterContainer.setLayoutManager(new GridAutofitLayoutManager(getContext(), letterWidth));
        final LettersAdapter lettersAdapter = new LettersAdapter(R.layout.card_letter_scaled);
        lettersAdapter.setOnWordClickedListener(this);
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

    @Override
    protected int getDrawerItemId() {
        return R.id.nav_spelling;
    }

    @Override
    public void onWordClicked(String word) {
        // cast context
        final Context context = getContext();
        final OnWordClickedListener mainActivity;
        if (context instanceof OnWordClickedListener) {
            mainActivity = (OnWordClickedListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnWordClickedListener");
        }

        // hide soft keyboard
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(self.getWindowToken(), 0);

        // load word
        mainActivity.onWordClicked(word);
    }
}

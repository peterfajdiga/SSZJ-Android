package peterfajdiga.sszj.sections;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import peterfajdiga.sszj.R;


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
        final TextView input = (TextView)self.findViewById(R.id.editText);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layCards(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected String getTitle() {
        return getContext().getString(R.string.nav_spelling);
    }

    private void layCards(String word) {
        final LinearLayout container = (LinearLayout)self.findViewById(R.id.letter_container);
        container.removeAllViews();
        final int n = word.length();
        for (int i = 0; i < n; i++) {
            final View card = buildCard(word.charAt(i));
            container.addView(card);
        }
    }

    private CardView buildCard(char letter) {
        final CardView card = (CardView)LayoutInflater.from(getActivity()).inflate(R.layout.letter_card, null);

        final ImageView cardImage = (ImageView)card.findViewById(R.id.card_letter_image);
        cardImage.setImageDrawable(getLetterImage(letter));

        final TextView cardText = (TextView)card.findViewById(R.id.card_letter_text);
        cardText.setText(new char[]{letter}, 0, 1);

        return card;
    }

    private Drawable getLetterImage(char letter) {
        int id;
        switch (letter) {
            case 'A': id = R.drawable.abeceda_a; break;
            case 'B': id = R.drawable.abeceda_b; break;
            case 'C': id = R.drawable.abeceda_c; break;
            case 'Č': id = R.drawable.abeceda_cc; break;
            case 'D': id = R.drawable.abeceda_d; break;
            case 'E': id = R.drawable.abeceda_e; break;
            case 'F': id = R.drawable.abeceda_f; break;
            case 'G': id = R.drawable.abeceda_g; break;
            case 'H': id = R.drawable.abeceda_h; break;
            case 'I': id = R.drawable.abeceda_i; break;
            case 'J': id = R.drawable.abeceda_j; break;
            case 'K': id = R.drawable.abeceda_k; break;
            case 'L': id = R.drawable.abeceda_l; break;
            case 'M': id = R.drawable.abeceda_m; break;
            case 'N': id = R.drawable.abeceda_n; break;
            case 'O': id = R.drawable.abeceda_o; break;
            case 'P': id = R.drawable.abeceda_p; break;
            case 'R': id = R.drawable.abeceda_r; break;
            case 'S': id = R.drawable.abeceda_s; break;
            case 'Š': id = R.drawable.abeceda_ss; break;
            case 'T': id = R.drawable.abeceda_t; break;
            case 'U': id = R.drawable.abeceda_u; break;
            case 'V': id = R.drawable.abeceda_v; break;
            case 'Z': id = R.drawable.abeceda_z; break;
            case 'Ž': id = R.drawable.abeceda_zz; break;
            default: throw new RuntimeException(letter + " is not a valid letter of the Slovenian alphabet");
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(id, null);
        } else {
            return getResources().getDrawable(id);
        }
    }
}

package peterfajdiga.sszj.elements.adapters;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.logic.Words;

public class LettersAdapter extends RecyclerView.Adapter<LettersAdapter.LetterViewHolder> {

    private String word = "";
    private OnWordClickedListener onWordClickedListener = null;

    public LettersAdapter() {}

    public LettersAdapter(String word) {
        setWord(word);
    }

    public void setWord(String word) {
        if (Words.isValidWordSpelling(word)) {
            this.word = word.toUpperCase();
        } else {
            this.word = "";
        }
    }

    public void setOnWordClickedListener(OnWordClickedListener listener) {
        onWordClickedListener = listener;
    }

    @Override
    public int getItemCount() {
        return word.length();
    }

    @Override
    public LetterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_letter, parent, false);
        view.setOnClickListener(new ItemClickListener());
        return new LetterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LetterViewHolder holder, int position) {
        final char letter = word.charAt(position);
        final String letter_str = Character.toString(letter);

        holder.view.setTag(letter_str);

        final ImageView cardImage = (ImageView)holder.view.findViewById(R.id.card_letter_image);
        cardImage.setImageDrawable(getLetterImage(holder.view.getResources(), letter));

        final TextView cardText = (TextView)holder.view.findViewById(R.id.card_letter_text);
        cardText.setText(letter_str);
    }

    private Drawable getLetterImage(final Resources res, final char letter) {
        final int id;
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
            return res.getDrawable(id, null);
        } else {
            return res.getDrawable(id);
        }
    }



    class LetterViewHolder extends RecyclerView.ViewHolder {

        View view;

        LetterViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (onWordClickedListener == null) {
                return;
            }
            final String word = (String)view.getTag();
            onWordClickedListener.onWordClicked(word);
        }
    }
}

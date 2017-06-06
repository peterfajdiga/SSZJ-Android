package peterfajdiga.sszj.elements.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {

    private String[] words;
    private int layoutResource;  // must be a TextView

    public WordsAdapter(String[] words, int layoutResource) {
        this.words = words;
        this.layoutResource = layoutResource;
    }

    @Override
    public int getItemCount() {
        return words.length;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResource, parent, false);

        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.view.setText(words[position]);
    }



    public class WordViewHolder extends RecyclerView.ViewHolder {

        TextView view;

        WordViewHolder(View itemView) {
            super(itemView);
            view = (TextView)itemView;
        }
    }
}

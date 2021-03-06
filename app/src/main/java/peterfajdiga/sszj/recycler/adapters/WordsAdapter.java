package peterfajdiga.sszj.recycler.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {

    private String[] words;
    private int layoutResource;  // must be a TextView
    private OnWordClickedListener onWordClickedListener = null;

    public WordsAdapter(int layoutResource, String[] words) {
        this.words = words;
        this.layoutResource = layoutResource;
    }

    public void setOnWordClickedListener(OnWordClickedListener listener) {
        onWordClickedListener = listener;
    }

    @Override
    public int getItemCount() {
        return words.length;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResource, parent, false);
        view.setOnClickListener(new ItemClickListener());
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.view.setText(words[position]);
    }



    class WordViewHolder extends RecyclerView.ViewHolder {
        TextView view;
        WordViewHolder(View itemView) {
            super(itemView);
            view = (TextView)itemView;
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (onWordClickedListener == null) {
                return;
            }
            final String word = (String)((TextView)view).getText();
            onWordClickedListener.onWordClicked(word);
        }
    }
}

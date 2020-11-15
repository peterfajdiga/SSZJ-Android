package peterfajdiga.sszj.elements.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.logic.sets.Set;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.SetViewHolder> {

    private Set[] sets;
    private OnSetClickedListener onSetClickedListener = null;

    public SetsAdapter(Set[] sets) {
        this.sets = sets;
    }

    public void setOnSetClickedListener(OnSetClickedListener listener) {
        onSetClickedListener = listener;
    }

    @Override
    public int getItemCount() {
        return sets.length;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_set_scaled, parent, false);
        view.setOnClickListener(new ItemClickListener());
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        final TextView labelView = (TextView)holder.view.findViewById(R.id.card_text);
        final ImageView imageView = (ImageView)holder.view.findViewById(R.id.card_image);
        final Set set = sets[position];
        final Context context = holder.view.getContext();

        labelView.setText(set.label);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(context.getDrawable(set.imageResource));
        } else {
            imageView.setImageDrawable(context.getResources().getDrawable(set.imageResource));
        }

        holder.view.setTag(set);
    }



    class SetViewHolder extends RecyclerView.ViewHolder {
        View view;
        SetViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    private class ItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (onSetClickedListener == null) {
                return;
            }
            Set set = (Set)view.getTag();
            onSetClickedListener.onSetClicked(set);
        }
    }

    public interface OnSetClickedListener {
        void onSetClicked(Set set);
    }
}

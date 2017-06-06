package peterfajdiga.sszj.elements.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import peterfajdiga.sszj.R;
import peterfajdiga.sszj.logic.pojo.Set;

public class SetsAdapter extends ArrayAdapter<Set> {

    public SetsAdapter(Context context) {
        super(context, R.layout.card_set, sets);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.card_set, null);
        }
        final TextView labelView = (TextView)convertView.findViewById(R.id.card_text);
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.card_image);
        final Set set = getItem(position);

        labelView.setText(set.label);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageDrawable(getContext().getDrawable(set.imageResource));
        } else {
            imageView.setImageDrawable(getContext().getResources().getDrawable(set.imageResource));
        }

        return convertView;
    }

    private final static Set[] sets = new Set[] {
            new Set("Osebe",    R.drawable.sets_1),
            new Set("Delo",     R.drawable.sets_2),
            new Set("Opravila", R.drawable.sets_3)
    };
}

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
            new Set("Osebe",                        R.drawable.sets_1),
            new Set("Delo",                         R.drawable.sets_2),
            new Set("Opravila",                     R.drawable.sets_3),
            new Set("Poklici",                      R.drawable.sets_4),
            new Set("Vprašalnice",                  R.drawable.sets_5),
            new Set("Osebni zaimki s pomožnikom",   R.drawable.sets_6),
            new Set("Čas",                          R.drawable.sets_7),
            new Set("Mišljenje",                    R.drawable.sets_8),
            new Set("Čustva, volja in občutki",     R.drawable.sets_9),
            new Set("Telo",                         R.drawable.sets_10),
            new Set("Medicina in bolezni",          R.drawable.sets_11),
            new Set("Nega",                         R.drawable.sets_12),
            new Set("Narava",                       R.drawable.sets_13),
            new Set("Živali",                       R.drawable.sets_14),
            new Set("Gibanje",                      R.drawable.sets_15),
            new Set("Smer",                         R.drawable.sets_16),
            new Set("Potovanje",                    R.drawable.sets_17),
            new Set("Bivalni prostori in pohištvo", R.drawable.sets_18),
            new Set("Oblačila",                     R.drawable.sets_19),
            new Set("Hrana in pijača",              R.drawable.sets_20),
            new Set("Komunikacija",                 R.drawable.sets_21),
            new Set("Izobraževanje",                R.drawable.sets_22),
            new Set("Družbena ureditev",            R.drawable.sets_23),
            new Set("Sodstvo",                      R.drawable.sets_24),
            new Set("Vojna",                        R.drawable.sets_25),
            new Set("Denar",                        R.drawable.sets_26),
            new Set("Trgovina",                     R.drawable.sets_27),
            new Set("Religija",                     R.drawable.sets_28),
            new Set("Prosti čas",                   R.drawable.sets_29),
            new Set("Šport",                        R.drawable.sets_30),
            new Set("Števila",                      R.drawable.sets_31),
            new Set("Količina, velikost in stopnja",R.drawable.sets_32),
            new Set("Lastnost, vrsta in stanje",    R.drawable.sets_33),
            new Set("Države",                       R.drawable.sets_34),
            new Set("Mesta",                        R.drawable.sets_35),
            new Set("Položaj, lega",                R.drawable.sets_36),
            new Set("Abeceda",                      R.drawable.sets_37),
            new Set("Neopredeljeno",                R.drawable.sets_38)
    };
}

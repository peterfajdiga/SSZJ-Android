package peterfajdiga.sszj;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class WordButton extends android.support.v7.widget.AppCompatTextView {
    public WordButton(Context context) {
        super(context);
        init();
    }

    public WordButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WordButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final int paddingHorizontal = (int)getResources().getDimension(R.dimen.wordbutton_horizontal_padding);
        final int paddingVertical   = (int)getResources().getDimension(R.dimen.wordbutton_vertical_padding);
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.wordbutton_textsize));

        final TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setBackgroundResource(outValue.resourceId);
        setClickable(true);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Owner owner = castToOwner(getContext());
                owner.onWordClicked((String)getText());
            }
        });
    }


    private Owner castToOwner(final Context context) {
        if (context instanceof Owner) {
            return (Owner)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement WordButton.Owner");
        }
    }


    public interface Owner {
        void onWordClicked(String word);
    }
}

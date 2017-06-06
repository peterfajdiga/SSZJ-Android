package peterfajdiga.sszj.elements.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import peterfajdiga.sszj.logic.pojo.Set;
import peterfajdiga.sszj.elements.adapters.SetsAdapter;

public class SetsGridView extends GridView {

    public SetsGridView(Context context) {
        super(context);
        init(context);
    }

    public SetsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SetsGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SetsGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        setNumColumns(GridView.AUTO_FIT);
        setAdapter(new SetsAdapter(context));
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Set set = (Set)getItemAtPosition(position);
                castToOwner(context).onSetClicked(set);
            }
        });
    }


    private Owner castToOwner(final Context context) {
        if (context instanceof Owner) {
            return (Owner)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SetsGridView.OnRetryClickListener");
        }
    }


    public interface Owner {
        void onSetClicked(Set set);
    }
}

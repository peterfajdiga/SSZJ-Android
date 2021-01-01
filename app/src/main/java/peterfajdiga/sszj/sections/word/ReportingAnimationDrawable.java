package peterfajdiga.sszj.sections.word;

import android.graphics.drawable.AnimationDrawable;

public class ReportingAnimationDrawable extends AnimationDrawable {

    private OnFrameListener listener;

    public ReportingAnimationDrawable() {
        super();
    }

    public void setOnFrameListener(OnFrameListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean selectDrawable(int index) {
        boolean retval = super.selectDrawable(index);
        if (listener != null) {
            listener.onFrame(index);
        }
        return retval;
    }


    public interface OnFrameListener {
        void onFrame(int index);
    }
}

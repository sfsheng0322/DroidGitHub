package com.sunfusheng.github.widget.multistate;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.LoadingState;

/**
 * @author sunfusheng on 2018/4/19.
 */
public class MultiStateView extends FrameLayout {

    private View loadingView;
    private View normalView;
    private View errorView;
    private View emptyView;

    @LoadingState
    private int loadingState;
    private LoadingStateDelegate delegate;

    private LayoutInflater inflater;

    public MultiStateView(@NonNull Context context) {
        this(context, null);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView, defStyleAttr, 0);

        int loadingResId = typedArray.getResourceId(R.styleable.MultiStateView_loadingLayout, R.layout.layout_loading_default);
        if (loadingResId > -1) {
            loadingView = inflater.inflate(loadingResId, this, false);
            addView(loadingView);
        }

        int normalResId = typedArray.getResourceId(R.styleable.MultiStateView_normalLayout, -1);
        if (normalResId > -1) {
            normalView = inflater.inflate(normalResId, this, false);
            addView(normalView);
        }

        int errorResId = typedArray.getResourceId(R.styleable.MultiStateView_errorLayout, R.layout.layout_error_default);
        if (errorResId > -1) {
            errorView = inflater.inflate(errorResId, this, false);
            addView(errorView);
        }

        int emptyResId = typedArray.getResourceId(R.styleable.MultiStateView_emptyLayout, R.layout.layout_empty_default);
        if (emptyResId > -1) {
            emptyView = inflater.inflate(emptyResId, this, false);
            addView(emptyView);
        }

        loadingState = typedArray.getInt(R.styleable.MultiStateView_loadingState, LoadingState.SUCCESS);
        typedArray.recycle();

        delegate = new LoadingStateDelegate(loadingView, normalView, errorView, emptyView);
        setLoadingState(loadingState);
    }

    public void setLoadingState(@LoadingState int loadingState) {
        this.loadingState = loadingState;
        delegate.setLoadingState(loadingState);
        if (onStateChangedListener != null) {
            onStateChangedListener.onStateChanged(loadingState);
        }
    }

    public void setLoadingState(@LoadingState int loadingState, Runnable onSuccess) {
        setLoadingState(loadingState, onSuccess, null);
    }

    public void setLoadingState(@LoadingState int loadingState, Runnable onSuccess, Runnable onError) {
        setLoadingState(loadingState, onSuccess, onError, null);
    }

    public void setLoadingState(@LoadingState int loadingState, Runnable onSuccess, Runnable onError, Runnable onEmpty) {
        setLoadingState(loadingState, null, onSuccess, onError, onEmpty);
    }

    public void setLoadingState(@LoadingState int loadingState, Runnable onLoading, Runnable onSuccess, Runnable onError, Runnable onEmpty) {
        setLoadingState(loadingState);
        switch (loadingState) {
            case LoadingState.LOADING:
                if (onLoading != null) {
                    onLoading.run();
                }
                break;
            case LoadingState.SUCCESS:
                if (onSuccess != null) {
                    onSuccess.run();
                }
                break;
            case LoadingState.ERROR:
                if (onError != null) {
                    onError.run();
                }
                break;
            case LoadingState.EMPTY:
                if (onEmpty != null) {
                    onEmpty.run();
                }
                break;
        }
    }

    public View setContentView(@LayoutRes int layoutResID) {
        if (layoutResID > -1) {
            normalView = inflater.inflate(layoutResID, this, false);
            addView(normalView);
            setNormalView(normalView);
            setLoadingState(loadingState);
        }
        return normalView;
    }

    public void setLoadingView(View loadingView) {
        delegate.setLoadingView(loadingView);
    }

    public void setNormalView(View successView) {
        delegate.setSuccessView(successView);
    }

    public void setErrorView(View errorView) {
        delegate.setErrorView(errorView);
    }

    public void setEmptyView(View emptyView) {
        delegate.setEmptyView(emptyView);
    }

    public void setLoadingTip(String tip) {
        TextView textView = loadingView.findViewById(R.id.loading_tip);
        if (textView != null) {
            textView.setText(tip);
        }
    }

    public void setErrorTip(String tip) {
        TextView textView = errorView.findViewById(R.id.error_tip);
        if (textView != null) {
            textView.setText(tip);
        }
    }

    public void setEmptyTip(String tip) {
        TextView textView = emptyView.findViewById(R.id.empty_tip);
        if (textView != null) {
            textView.setText(tip);
        }
    }

    public void setErrorViewListener(OnClickListener listener) {
        if (errorView != null) {
            errorView.setOnClickListener(listener);
        }
    }

    public void setErrorButtonListener(OnClickListener listener) {
        View view = errorView.findViewById(R.id.error_button);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public void setEmptyViewListener(OnClickListener listener) {
        if (emptyView != null) {
            emptyView.setOnClickListener(listener);
        }
    }

    @Nullable
    public View getView(@LoadingState int loadingState) {
        switch (loadingState) {
            case LoadingState.LOADING:
                return getLoadingView();
            case LoadingState.SUCCESS:
                return getNormalView();
            case LoadingState.ERROR:
                return getErrorView();
            case LoadingState.EMPTY:
                return getEmptyView();
            default:
                return null;
        }
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getNormalView() {
        return delegate.getSuccessView();
    }

    public View getErrorView() {
        return errorView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public int getLoadingState() {
        return loadingState;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.loadingState = loadingState;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        if (ss != null) {
            setLoadingState(ss.loadingState);
            super.onRestoreInstanceState(ss.getSuperState());
        }
    }

    static class SavedState extends BaseSavedState {
        int loadingState;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.loadingState = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.loadingState);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    private OnStateChangedListener onStateChangedListener;

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    public interface OnStateChangedListener {
        void onStateChanged(@LoadingState int loadingState);
    }
}

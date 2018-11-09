package com.sunfusheng.github.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.R;
import com.sunfusheng.multistate.LoadingState;
import com.sunfusheng.wrapper.RecyclerViewWrapper;

/**
 * @author sunfusheng on 2018/4/18.
 */
public class DiscoverFragment extends BaseFragment {

    private RecyclerViewWrapper recyclerViewWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        if (getView() == null) return;
        toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("Discover repositories");
    }

    private void initView() {
        View view = getView();
        if (view == null) return;
        recyclerViewWrapper = view.findViewById(R.id.recyclerViewWrapper);
        recyclerViewWrapper.setErrorLayout(R.layout.layout_error_default);
        recyclerViewWrapper.setLoadingState(LoadingState.ERROR);
    }

}

package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.ProgressState;
import com.sunfusheng.github.util.FileUtil;
import com.sunfusheng.github.util.HtmlUtil;
import com.sunfusheng.github.viewmodel.ContributionsViewModel;
import com.sunfusheng.github.viewmodel.vm.VM;
import com.sunfusheng.github.viewmodel.vm.VMProvider;

/**
 * @author sunfusheng on 2018/7/28.
 */
public class UserContributionsView extends LinearLayout {
    private ContributionsWebView webView;
    private TextView vTip;

    public UserContributionsView(Context context) {
        this(context, null);
    }

    public UserContributionsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserContributionsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_user_contributions, this);
        webView = findViewById(R.id.contributions);
        vTip = findViewById(R.id.tip);

        webView.setVisibility(GONE);
        vTip.setVisibility(VISIBLE);
        vTip.setText("Loading...");
    }

    private ContributionsViewModel vm;

    private ContributionsViewModel getViewModel() {
        if (vm == null) {
            vm = VMProvider.of(getContext(), ContributionsViewModel.class);
        }
        return vm;
    }

    public void setUsername(String username) {
        getViewModel().request(username);
        getViewModel().liveData.observe(VM.getActivity(getContext()), it -> {
            switch (it.progressState) {
                case ProgressState.START:
                    break;
                case ProgressState.PROGRESS:
                    break;
                case ProgressState.PAUSE:
                    vTip.setText("Paused");
                    break;
                case ProgressState.CANCEL:
                    vTip.setText("Canceled");
                    break;
                case ProgressState.SUCCESS:
                case ProgressState.ERROR:
                    String filePath = ContributionsViewModel.getContributionsFilePath(username);
                    String htmlText = HtmlUtil.getContributionsData(FileUtil.convertFileToString(filePath));
                    if (!TextUtils.isEmpty(htmlText)) {
                        vTip.setVisibility(GONE);
                        webView.setVisibility(VISIBLE);
                        webView.loadData(htmlText);
                    } else {
                        webView.setVisibility(GONE);
                        vTip.setVisibility(VISIBLE);
                        vTip.setText("Load Failed!");
                    }
                    break;
            }
        });
    }
}

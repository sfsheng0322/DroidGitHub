package com.sunfusheng.github.widget.app;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.sunfusheng.github.R;
import com.sunfusheng.github.util.DisplayUtil;

/**
 * @author sunfusheng on 2018/4/20.
 */
public class ContributionsWebView extends WebView {

    public ContributionsWebView(Context context) {
        this(context, null);
    }

    public ContributionsWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContributionsWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(getResources().getColor(R.color.white));
        setOnLongClickListener(v -> true);

        getSettings().setDefaultFontSize(12);
        getSettings().setSupportZoom(false);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        scrollTo(DisplayUtil.getWindowWidth(getContext()), 0);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, 0, clampedX, clampedY);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, 0);
    }

}

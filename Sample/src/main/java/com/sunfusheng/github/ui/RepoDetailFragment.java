package com.sunfusheng.github.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.R;
import com.sunfusheng.github.annotation.ProgressState;
import com.sunfusheng.github.util.StatusBarUtil;
import com.sunfusheng.github.util.ToastUtil;
import com.sunfusheng.github.viewmodel.ReadmeViewModel;
import com.sunfusheng.github.viewmodel.base.VmProvider;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author by sunfusheng on 2018/11/14
 */
public class RepoDetailFragment extends BaseFragment {

    private TextView vReadMe;

    private String repoFullName;

    public static RepoDetailFragment instance(String repoFullName) {
        RepoDetailFragment fragment = new RepoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.REPO_FULL_NAME, repoFullName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        super.onViewCreated(view, savedInstanceState);
        initView();
        initReadmeView();
    }

    private void initData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            repoFullName = arguments.getString(Constants.Bundle.REPO_FULL_NAME);
        }
    }

    private void initView() {
        toolbar.setTitle(repoFullName);

        vReadMe = getView().findViewById(R.id.vReadMe);
//        vMarkdownView.addStyleSheet(new Github());
    }

    private void initReadmeView() {
        ReadmeViewModel vm = VmProvider.of(getContext(), ReadmeViewModel.class);
        vm.setRequestParams(repoFullName);

        vm.liveData.observe(this, it -> {
            switch (it.progressState) {
                case ProgressState.SUCCESS:
                    RichText.fromMarkdown(getFileString())
                            .scaleType(ImageHolder.ScaleType.center_crop)
                            .size(300, 200)
                            .into(vReadMe);
                    break;
                case ProgressState.ERROR:
                    ToastUtil.toast(it.errorMsg);
                    break;
            }
        });
    }

    public String getFileString() {
        byte[] strBuffer = null;
        int flen = 0;
        File file = new File(ReadmeViewModel.getReadmeFilePath(repoFullName.split("/")[0]));
        try {
            InputStream in = new FileInputStream(file);
            flen = (int) file.length();
            strBuffer = new byte[flen];
            in.read(strBuffer, 0, flen);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(strBuffer);
    }
}

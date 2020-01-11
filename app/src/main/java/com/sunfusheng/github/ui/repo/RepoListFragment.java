package com.sunfusheng.github.ui.repo;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.model.Repo;
import com.sunfusheng.github.ui.NavigationManager;
import com.sunfusheng.github.ui.base.BaseListFragment;
import com.sunfusheng.github.viewbinder.RepoBinder;
import com.sunfusheng.github.viewmodel.RepoListViewModel;

/**
 * @author sunfusheng
 * @since 2020-01-07
 */
public class RepoListFragment extends BaseListFragment<RepoListViewModel, Repo> {

    public static RepoListFragment newFragment(String username) {
        RepoListFragment fragment = new RepoListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.Bundle.USERNAME, username);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Class<RepoListViewModel> getViewModelClass() {
        return RepoListViewModel.class;
    }

    @Override
    protected void registerViewBinders() {
        RepoBinder repoBinder = new RepoBinder();
        repoBinder.showFullName(false);
        repoBinder.showExactNum(true);
        repoBinder.showUpdateTime(true);
        vRecyclerViewWrapper.register(Repo.class, repoBinder);
    }

    @Override
    protected void onItemClick(Repo item) {
        NavigationManager.toRepoDetailActivity(getContext(), item.full_name);
    }
}

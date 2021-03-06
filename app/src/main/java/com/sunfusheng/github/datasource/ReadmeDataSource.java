package com.sunfusheng.github.datasource;

import com.sunfusheng.github.Constants;
import com.sunfusheng.github.cache.disklrucache.ReadmeDiskLruCache;
import com.sunfusheng.github.http.Api;
import com.sunfusheng.github.http.response.ResponseData;
import com.sunfusheng.github.viewmodel.params.RepoFullNameParams;
import com.sunfusheng.multistate.LoadingState;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @author by sunfusheng on 2019-05-26
 */
public class ReadmeDataSource extends BaseDataSource<RepoFullNameParams, String> {
    private String mRepoFullName;
    private int mFetchMode;
    private ReadmeDiskLruCache mReadmeDiskLruCache = new ReadmeDiskLruCache();

    @Override
    public void setParams(RepoFullNameParams params) {
        this.mRepoFullName = params.repoFullName;
        this.mFetchMode = params.fetchMode;
    }

    @Override
    public Observable<ResponseData<String>> localObservable() {
        return Observable.defer(() -> {
            return Observable.create((ObservableOnSubscribe<ResponseData<String>>) emitter -> {
                DataSourceHelper.emitLocalResponseData(emitter, mReadmeDiskLruCache.get(mRepoFullName));
            });
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<ResponseData<String>> remoteObservable() {
        return Api.getCommonService().fetchReadme(mRepoFullName, mFetchMode, Constants.Time.MINUTES_10)
                .subscribeOn(Schedulers.io())
                .compose(DataSourceHelper.applyRemoteTransformer())
                .map(it -> {
                    String readme = null;
                    if (it.loadingState == LoadingState.SUCCESS) {
                        readme = it.data.string();
                        mReadmeDiskLruCache.put(mRepoFullName, readme);
                    }

                    ResponseData<String> response = new ResponseData<String>(it.code, it.msg, readme, it.loadingState);
                    response.setFetchMode(it.fetchMode);
                    response.url = it.url;
                    response.localCacheValidateTime = it.localCacheValidateTime;
                    response.lastAccessTime = it.lastAccessTime;
                    return response;
                });
    }
}

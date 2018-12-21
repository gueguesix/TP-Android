package com.android.florent.tpandroid18;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.android.florent.tpandroid18.News;
import com.android.florent.tpandroid18.NewsLiveData;

import java.util.List;

public class WelcomeScreenViewModel extends ViewModel {

    private LiveData<List<News>> news;

    public LiveData<List<News>> getNews() {
        if (news == null) {
            news = new NewsLiveData();
        }
        return news;
    }


}
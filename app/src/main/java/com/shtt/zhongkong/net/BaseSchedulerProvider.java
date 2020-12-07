package com.shtt.zhongkong.net;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

public interface BaseSchedulerProvider {
    public Scheduler computation();

    Scheduler io();
    Scheduler ui();
    <T> ObservableTransformer<T, T> applySchedulers();
}

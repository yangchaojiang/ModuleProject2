package com.module;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yangc on 2017/8/9.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated: 夫rx 封装
 */

public class RxSchedulers {


    /****
     * Api.getInstance().movieService
     * .getGankData("Android",1)
     * .compose(RxSchedulers.io_main());
     ***/
    public static <T> ObservableTransformer<T, T> io_main() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
    }

}

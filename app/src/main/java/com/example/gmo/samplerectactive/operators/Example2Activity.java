package com.example.gmo.samplerectactive.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gmo.samplerectactive.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class Example2Activity extends AppCompatActivity {
    /*Buffer và Debounce*/
    Observable<Integer> integerObservable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9);
    private static final String TAG = Example2Activity.class.getSimpleName();

    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.tv_result1)
    TextView tvResult1;
    @BindView(R.id.tv_result_max_count)
    TextView tvResultMaxCount;

    private Disposable disposable;
    private Unbinder unbinder;
    private int maxTaps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example2);
        unbinder = ButterKnife.bind(this);
        /*Buffer Khi buffer = 3 thì phát ra 3 giá trị integer*/
        integerObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .buffer(3)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "onNext: ");
                        for (Integer integer : integers) {
                            Log.d(TAG, "Item: " + integer);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

        /*Sự kiện click cho button*/
        RxView.clicks(btnClick)
                .map(new Function<Object, Integer>() {
                    @Override
                    public Integer apply(Object o) throws Exception {
                        return 1;
                    }
                })
                .buffer(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "onNext: " + integers.size() + " taps received");
                        if (integers.size() > 0){
                            maxTaps = integers.size() > maxTaps ? integers.size() : maxTaps;
                            tvResult1.setText(String.format("Received %d taps in secs",integers.size()));
                            tvResultMaxCount.setText(String.format("Maximum of %d taps received in this session", maxTaps));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

    }

    @OnClick(R.id.btn_click)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}

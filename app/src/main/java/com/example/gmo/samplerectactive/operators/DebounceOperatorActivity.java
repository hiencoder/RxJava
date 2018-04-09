package com.example.gmo.samplerectactive.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gmo.samplerectactive.R;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DebounceOperatorActivity extends AppCompatActivity {
    private static final String TAG = DebounceOperatorActivity.class.getSimpleName();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Unbinder unbinder;

    @BindView(R.id.tv_query)
    TextView tvQuery;
    @BindView(R.id.ed_query)
    EditText edQuery;

    /*Bắt sự kiện thay đổi text trong edittext cập nhật lên textview*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debounce_operator);
        unbinder = ButterKnife.bind(this);

        compositeDisposable.add(RxTextView.textChangeEvents(edQuery)
        .skipInitialValue()
        .debounce(300, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeWith(searchQuery()));

        tvQuery.setText("Search query will be accumulated every 300 milli sec");
    }

    private DisposableObserver<TextViewTextChangeEvent> searchQuery() {
        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                Log.d(TAG, "Search string: " + textViewTextChangeEvent.text().toString());
                tvQuery.setText("Query: " + textViewTextChangeEvent.text().toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        compositeDisposable.clear();
    }
}

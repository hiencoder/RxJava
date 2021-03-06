package com.example.gmo.samplerectactive.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gmo.samplerectactive.R;
import com.example.gmo.samplerectactive.operators.model.Address;
import com.example.gmo.samplerectactive.operators.model.People;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ConcatOperatorActivity extends AppCompatActivity {
    /*Output tương tự như FlatMap nhưng thứ tự phát ra data thay đổi.
    * ConcatMap duy trì việc đặt hàng các item và chờ đợi Observable hiện tại hoàn thành công việc của nó trước
    * khi phát ra item tiếp theo*/
    private static final String TAG = ConcatOperatorActivity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concat_operator);

        /*Create Observable*/
        Observable<People> peopleObservable = getPeopleObservable();

        //Subscribe
        peopleObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .concatMap(new Function<People, ObservableSource<People>>() {
                    @Override
                    public ObservableSource<People> apply(People people) throws Exception {
                        return getAddressObservable(people);
                    }
                })
                .subscribe(new Observer<People>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                        disposable = d;
                    }

                    @Override
                    public void onNext(People people) {
                        Log.d(TAG, "onNext: " + people.getName() + ", Email: " + people.getEmail() + ", Address: " + people.getAddress().getAddress());
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

    private ObservableSource<People> getAddressObservable(final People people) {
        /*Mang address*/
        return Observable.create(new ObservableOnSubscribe<People>() {
            @Override
            public void subscribe(ObservableEmitter<People> emitter) throws Exception {
                Address address = new Address();
                address.setAddress((new Random().nextInt(100)) + " TDH, Hà Nội");
                if (!emitter.isDisposed()) {
                    people.setAddress(address);
                    //Thread sleep
                    int sleepTime = new Random().nextInt(1000) + 500;
                    Thread.sleep(sleepTime);
                    emitter.onNext(people);
                    emitter.onComplete();
                }
            }
        });
    }

    private Observable<People> getPeopleObservable() {
        final List<People> people = preparePeople();
        return Observable.create(new ObservableOnSubscribe<People>() {
            @Override
            public void subscribe(ObservableEmitter<People> emitter) throws Exception {
                for (People people1 : people) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(people1);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    private List<People> preparePeople() {
        List<People> people = new ArrayList<>();
        people.add(new People("Trump", "Trump@gmail.com", "Male", new Address("Mẽo")));
        people.add(new People("Putin", "Putin@gmail.com", "Male", new Address("Liên Xô")));
        people.add(new People("Tap", "Tap@gmail.com", "Female", new Address("Tàu")));
        people.add(new People("Maochuxi", "Mao@gmail.com", "Male", new Address("Tàu")));
        people.add(new People("KimUn", "KimUn@gmail.com", "Female", new Address("Triều Tiên")));
        return people;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}

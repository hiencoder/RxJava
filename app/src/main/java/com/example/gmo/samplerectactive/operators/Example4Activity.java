package com.example.gmo.samplerectactive.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gmo.samplerectactive.R;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import rx.Observable;
import rx.Subscriber;
import rx.observables.MathObservable;


public class Example4Activity extends AppCompatActivity {
    /*Max: Tìm giá trị lớn nhất trong các item của Observable và phát ra nó*/
    Integer[] numbers = {5, 20, 10, 45, 50};
    private static final String TAG = Example4Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example4);
        Observable<Integer> observable = Observable.from(numbers);
        /*Max*/
        MathObservable.max(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Max value: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }
                });

        /*Min*/
        MathObservable.min(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Min value: " + integer);
                    }
                });

        /*Sum*/
        MathObservable
                .sumInteger(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Sum: " + integer);
                    }
                });

        /*Average*/
        MathObservable
                .averageInteger(observable)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Average: " + integer);
                    }
                });

        /*Count: Đếm số item được phát ra.
        * Đếm số user male */
        io.reactivex.Observable<User> userObservable = getUserObservable();

        userObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        return user.getGender().equalsIgnoreCase("male");
                    }
                })
                .count()
                .subscribeWith(new SingleObserver<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Long aLong) {
                        Log.d(TAG, "onSuccess: " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        /*Reduce*/

    }

    /**/
    private io.reactivex.Observable<User> getUserObservable() {
        final List<User> users = createUser();
        return io.reactivex.Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                for (User user : users) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(user);
                    }
                }
                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private List<User> createUser() {
        List<User> users = new ArrayList<>();
        users.add(new User(1, "A", "Male"));
        users.add(new User(2, "B", "Female"));
        users.add(new User(3, "C", "Male"));
        users.add(new User(4, "D", "Female"));
        users.add(new User(4, "D", "Female"));
        users.add(new User(5, "E", "Male"));
        users.add(new User(6, "F", "Male"));
        users.add(new User(6, "F", "Male"));
        users.add(new User(7, "G", "Female"));
        users.add(new User(8, "H", "Male"));
        users.add(new User(3, "C", "Male"));
        users.add(new User(9, "I", "Female"));
        return users;
    }
}

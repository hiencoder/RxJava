package com.example.gmo.samplerectactive.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gmo.samplerectactive.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class Example3Activity extends AppCompatActivity {
    /*1. Filter() Cho phép Observable phát ra data qua test
    * filter các số chẵn của mảng number*/
    private static final String TAG = Example3Activity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example3);
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new DisposableObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
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
                

        /*Filter User male*/
        Observable<User> userObservable = getUserObservable();

        userObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) throws Exception {
                        return user.getGender().equalsIgnoreCase("female");
                    }
                }).subscribe(new DisposableObserver<User>() {
            @Override
            public void onNext(User user) {
                Log.d(TAG, user.toString());
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

        /*Sử dụng skip*/
        /*Bỏ qua user có id 1,2,3,4(bỏ 4 thằng đầu)*/
        Observable<User> skipObservable = getUserObservable();
        //Subscribe
        skipObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .skip(4)
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext Skip: " + user.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        //SkipLast(n) Bỏ qua n thằng cuối trong Observable
        Observable<User> skipLastObservable = getUserObservable();
        //Subscribe
        skipLastObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .skipLast(4)
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext SkipLast: " + user.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });

        /*Take(n) Lấy ra n phần tử đầu tiên bỏ qua các phần tử còn lại
        * TakeLast(n) Lấy ra n phần tử cuối */
        Observable<User> takeObservable = getUserObservable();
        takeObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(4)
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "Take: " + user.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        /*Take last*/
        Observable<User> takeLastObservable = getUserObservable();
        takeLastObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeLast(4)
                .subscribe(new DisposableObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "TakeLast: " + user.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


        /*Distinct: Lọc phần tử phát ra bởi Observable bị trùng
        * Làm việc tốt với các dữ liệu kiểu primative data.
        * Để sử dụng vs kiểu dữ liệu custom thì phải override equals() và hashCode()*/
        Observable.just(10,10,20,30,40,50,20,100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinct()
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: ");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "Distinct: " + integer);
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

        /*Distinct user*/
        Observable<User> distinctUserObservable = getUserObservable();

        DisposableObserver<User> disposableObserver = getDisposableObserver();

        distinctUserObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .distinct()
                .subscribe(disposableObserver);
    }

    //Create DisposableObserver
    private DisposableObserver<User> getDisposableObserver() {
        return new DisposableObserver<User>() {
            @Override
            public void onNext(User user) {
                Log.d(TAG, "Distinct User: " + user.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }


    //Create Observable
    private Observable<User> getUserObservable() {
        final List<User> users = createListUsers();
        return Observable.create(new ObservableOnSubscribe<User>() {
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
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<User> createListUsers() {
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

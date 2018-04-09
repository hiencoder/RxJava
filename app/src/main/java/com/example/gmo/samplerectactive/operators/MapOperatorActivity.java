package com.example.gmo.samplerectactive.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gmo.samplerectactive.R;
import com.example.gmo.samplerectactive.operators.model.Address;
import com.example.gmo.samplerectactive.operators.model.People;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapOperatorActivity extends AppCompatActivity {
    private static final String TAG = MapOperatorActivity.class.getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_operator);
        Observable<People> userObservable = getUserObservable();

        //Subscribe
        userObservable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<People, People>() {
                    @Override
                    public People apply(People people) throws Exception {
                        //Chỉnh lại useremail
                        //Trả về tên với các ký tự hoa
                        people.setEmail(String.format("%s@rxjava.sida",people.getName()));
                        people.setName(people.getName().toUpperCase());
                        return people;
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
                Log.d(TAG, "onNext: " + people.toString());
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

    /**
     * @return
     */
    private Observable<People> getUserObservable() {
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
        people.add(new People("Trump","Trump@gmail.com","Male",new Address("Mẽo")));
        people.add(new People("Putin","Putin@gmail.com","Male",new Address("Liên Xô")));
        people.add(new People("Tap","Tap@gmail.com","Female",new Address("Tàu")));
        people.add(new People("Maochuxi","Mao@gmail.com","Male",new Address("Tàu")));
        people.add(new People("KimUn","KimUn@gmail.com","Female",new Address("Triều Tiên")));
        return people;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}

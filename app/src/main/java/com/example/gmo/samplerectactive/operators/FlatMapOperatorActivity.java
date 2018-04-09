package com.example.gmo.samplerectactive.operators;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gmo.samplerectactive.R;
import com.example.gmo.samplerectactive.operators.model.Address;
import com.example.gmo.samplerectactive.operators.model.People;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FlatMapOperatorActivity extends AppCompatActivity {
    /*1 Network lấy về thông tin username, email, gender.
    * 1 network khác lấy về address
    * Y/C: Tạo 1 Observable phát ra name, gender và address*/
    private static final String TAG = FlatMapOperatorActivity.class.getSimpleName();
    private Disposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_map_operator);
        //Create observable people
        Observable<People> peopleObservable = getPeopleObservable();

        //Subscribe
        peopleObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<People, ObservableSource<People>>() {
                    @Override
                    public ObservableSource<People> apply(People people) throws Exception {
                        //Lấy về thông tin Address
                        return getAddressObservable(people);
                    }
                })
                .subscribe(new Observer<People>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(People people) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /*Lấy về thông tin address user*/
    private ObservableSource<People> getAddressObservable(People people) {
        return null;
    }

    /*Create Observable*/
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
        people.add(new People("Trump","Trump@gmail.com","Male",new Address("Mẽo")));
        people.add(new People("Putin","Putin@gmail.com","Male",new Address("Liên Xô")));
        people.add(new People("Tap","Tap@gmail.com","Female",new Address("Tàu")));
        people.add(new People("Maochuxi","Mao@gmail.com","Male",new Address("Tàu")));
        people.add(new People("KimUn","KimUn@gmail.com","Female",new Address("Triều Tiên")));
        return people;
    }
}

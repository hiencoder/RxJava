package com.example.gmo.samplerectactive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable< Note> noteObservable = getNoteObservable();

        DisposableObserver<Note> noteObserver = getNoteObserver();

        //Subscribe
        compositeDisposable.add(noteObservable
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map(new Function<Note, Note>() {
            @Override
            public Note apply(Note note) throws Exception {
                //Chuyển sang chữ hoa
                note.setNote(note.getNote().toUpperCase());
                return note;
            }
        }).subscribeWith(noteObserver));
    }

    private DisposableObserver<Note> getNoteObserver() {
        return new DisposableObserver<Note>() {
            @Override
            public void onNext(Note note) {
                Log.d(TAG, "Note: " + note.getNote());
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

    private Observable<Note> getNoteObservable() {
        final List<Note> notes = createNotes();
        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notes) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });

    }

    private List<Note> createNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note(1,"Note 1"));
        notes.add(new Note(2,"Note 2"));
        notes.add(new Note(3,"Note 3"));
        notes.add(new Note(4,"Note 4"));
        notes.add(new Note(5,"Note 5"));
        return notes;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}

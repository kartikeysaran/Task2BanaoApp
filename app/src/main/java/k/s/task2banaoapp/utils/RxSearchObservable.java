package k.s.task2banaoapp.utils;

import androidx.appcompat.widget.SearchView;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxSearchObservable {
    public static Observable<String> fromView(SearchView searchView) {
        final PublishSubject<String> subject = PublishSubject.create();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener ()
        {
            @Override
            public boolean onQueryTextSubmit(String s) {
                subject.onComplete();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                subject.onNext(text);
                return true;
            }
        });

        return subject;
    }
}

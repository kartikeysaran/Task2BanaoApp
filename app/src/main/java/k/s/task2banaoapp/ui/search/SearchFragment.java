package k.s.task2banaoapp.ui.search;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import k.s.task2banaoapp.R;
import k.s.task2banaoapp.dagger.AppComponent;
import k.s.task2banaoapp.dagger.AppModule;
import k.s.task2banaoapp.dagger.DaggerAppComponent;
import k.s.task2banaoapp.dagger.NetworkModule;
import k.s.task2banaoapp.model.PhotoResBean;
import k.s.task2banaoapp.retro.PhotosAPI;
import k.s.task2banaoapp.utils.CustomAdapter;
import k.s.task2banaoapp.utils.RxSearchObservable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    @Inject
    Retrofit retrofit;

    private PhotosAPI photosAPI;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CustomAdapter customAdapter;
    private GridLayoutManager layoutManager;
    SearchView searchView;
    private AppComponent appComponent;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        setUpViews(view);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("https://api.flickr.com"))
                .build();
        appComponent.inject(this);
        photosAPI = retrofit.create(PhotosAPI.class);
        searchViewObservaable();
        return view;
    }

    private void setUpViews(View view) {
        searchView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.recycle_view);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    public void searchViewObservaable() {
        RxSearchObservable.fromView(searchView)
                .debounce(200, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String text) {
                        if (text.isEmpty() || text.length()<3) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .distinctUntilChanged()
                .switchMap(s-> Observable.just(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) {
                        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.VISIBLE);
                        searchImages(result);
                    }
                }, throwable -> {
                    Log.d("RXJAVA", throwable.getMessage());
                });
    }

    public void searchImages(String text) {
        photosAPI.searchPhotos("flickr.photos.getRecent",
                "6f102c62f41998d151e5a1b48713cf13",
                "json",
                1,
                "url_s",
                text
        ).enqueue(new Callback<PhotoResBean>() {
            @Override
            public void onResponse(Call<PhotoResBean> call, Response<PhotoResBean> response) {
                Log.d("RETROFIT", response.message().toString());
                customAdapter = new CustomAdapter(response.body().getPhotos().getPhoto(), getContext());
                layoutManager=new GridLayoutManager(getContext(),2);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(customAdapter);
            }

            @Override
            public void onFailure(Call<PhotoResBean> call, Throwable t) {
                Log.d("RETROFIT", t.toString());
                showSnackBar(getView(), "ERROR! Please try again", Snackbar.LENGTH_SHORT);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void showSnackBar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }

}
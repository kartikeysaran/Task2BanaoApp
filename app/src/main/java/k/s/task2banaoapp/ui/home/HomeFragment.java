package k.s.task2banaoapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.inject.Inject;

import k.s.task2banaoapp.R;
import k.s.task2banaoapp.dagger.AppComponent;
import k.s.task2banaoapp.dagger.AppModule;
import k.s.task2banaoapp.dagger.DaggerAppComponent;
import k.s.task2banaoapp.dagger.NetworkModule;
import k.s.task2banaoapp.databinding.FragmentHomeBinding;
import k.s.task2banaoapp.model.PhotoResBean;
import k.s.task2banaoapp.model.PhotosBean;
import k.s.task2banaoapp.retro.PhotosAPI;
import k.s.task2banaoapp.utils.CustomAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    @Inject
    Retrofit retrofit;

    private PhotosAPI photosAPI;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private int curr_Page = 1;
    private int curr_limit = 50;
    private ArrayList<PhotosBean> photosBeanArrayList;
    private CustomAdapter customAdapter;
    private GridLayoutManager layoutManager;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = false;
    private FragmentHomeBinding binding;
    private AppComponent appComponent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setUpViews(root);
        photosBeanArrayList = new ArrayList<>();
        customAdapter = new CustomAdapter(photosBeanArrayList, getContext());
        layoutManager=new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(customAdapter);
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule("https://api.flickr.com"))
                .build();
        appComponent.inject(this);
        photosAPI = retrofit.create(PhotosAPI.class);
        getImagesFromAPI(curr_Page, curr_limit);
        loadImages();
        return root;
    }

    private void loadImages() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!loading && totalItemCount <= (lastVisibleItem + 1)) {
                    loading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    getImagesFromAPI(curr_Page+1, curr_limit);
                }
            }
        });

    }
    private void setUpViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void getImagesFromAPI(int page, int limit) {
        if(page > limit) {
            Toast.makeText(getContext(), "That's all the data..", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        } else {

            photosAPI.getPhotos("flickr.photos.getRecent",
                    20,
                    page,
                    "6f102c62f41998d151e5a1b48713cf13",
                    "json",
                    1,
                    "url_s"
            ).enqueue(new Callback<PhotoResBean>() {
                @Override
                public void onResponse(Call<PhotoResBean> call, Response<PhotoResBean> response) {
                    Log.d("RETROFIT", response.message().toString());
                    curr_Page = response.body().getPhotos().getPage();
                    curr_limit = response.body().getPhotos().getPages();
                    for(PhotosBean photosBean: response.body().getPhotos().getPhoto()) {
                        photosBeanArrayList.add(photosBean);
                    }
                    customAdapter.notifyDataSetChanged();
                    loading = false;
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<PhotoResBean> call, Throwable t) {
                    Log.d("RETROFIT", t.toString());
                    showSnackBar(getView(), "ERROR! Please try again", Snackbar.LENGTH_SHORT);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showSnackBar(View view, String message, int duration)
    {
        Snackbar.make(view, message, duration).show();
    }
}
package k.s.task2banaoapp.retro;

import k.s.task2banaoapp.model.PhotoResBean;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotosAPI {
    @GET("/services/rest")
    Call<PhotoResBean> getPhotos(@Query("method") String method, @Query("per_page") int per_page, @Query("page") int page, @Query("api_key") String api_key,
                                 @Query("format") String format, @Query("nojsoncallback") int nojsoncallback, @Query("extras") String extras);

    @GET("/services/rest")
    Call<PhotoResBean> searchPhotos(@Query("method") String method, @Query("api_key") String api_key,
                                    @Query("format") String format, @Query("nojsoncallback") int nojsoncallback, @Query("extras") String extras, @Query("text") String text);
}

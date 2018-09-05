package me.blog.hwani6736.lezhincomics.search;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by NarZa on 2018. 9. 3..
 */
public interface SearchApiInterface {

    @GET("/v2/search/image")
    Call<JsonObject> search(
            @Header("Authorization") String authorization
            , @Query(value = "query",encoded = true) String keyword
            , @Query(value = "sort", encoded = true) String sort
            , @Query(value = "page", encoded = true) int page
            , @Query(value = "size", encoded = true) int size
    );


}

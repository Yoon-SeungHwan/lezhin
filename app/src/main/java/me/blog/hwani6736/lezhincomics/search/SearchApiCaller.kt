package me.blog.hwani6736.lezhincomics.search

import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS



/**
 * Created by NarZa on 2018. 9. 6..
 */


object SearchApiCaller {
    const val KAKAO_AUTHORIZATION = "KakaoAK 3842b3757e881205f9d36842f35316d3"
    const val DEFAULT_PAGE = 1
    const val DEFAULT_SIZE = 30
    const val DEFAULT_SORT = "accuracy"

    private lateinit var retrofit: Retrofit
    init {
        OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
                .apply {
                    retrofit = Retrofit.Builder()
                            .baseUrl("https://dapi.kakao.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(OkHttpClient())
                            .build()
                }

    }

    fun searchImage(keyword: String, callback: Callback<JsonObject>) {
        searchImage(keyword, DEFAULT_SORT, DEFAULT_PAGE, DEFAULT_SIZE, callback)
    }

    fun searchImage(keyword: String, page: Int, callback: Callback<JsonObject>) {

        searchImage(keyword, DEFAULT_SORT, page, DEFAULT_SIZE, callback)
    }

    fun searchImage(keyword: String, sort: String?, page: Int, size: Int, callback: Callback<JsonObject>) {

        retrofit.create(SearchApiInterface::class.java)
                .search(KAKAO_AUTHORIZATION, keyword, sort, page, size)
                .enqueue(callback)
    }
}
package me.blog.hwani6736.lezhincomics.main

import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.google.gson.JsonObject
import me.blog.hwani6736.lezhincomics.R
import me.blog.hwani6736.lezhincomics.search.SearchApiCaller
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by NarZa on 2018. 9. 6..
 */
const val SEARCH_DELAY = 1_000L
class MainPresenter(private val mainView: Contract.View) : Contract.Presenter {

    private var lastResult: JsonObject? = null
    private var keyword:String = ""
    private val delayHandler = Handler()
    private val searchRunnable = Runnable {
        if (!TextUtils.isEmpty(keyword)) {
            onSearch(keyword)
        }
    }

    override fun onSearchKeywordInput(keyword: String) {
        delayHandler.removeCallbacks(searchRunnable)
        this@MainPresenter.keyword = keyword
        delayHandler.postDelayed(searchRunnable, SEARCH_DELAY)
    }

    override fun onSearch(keyword: String) {

        SearchApiCaller.searchImage(keyword, object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                lastResult = response.body()

                if (lastResult != null) {
                    if (lastResult?.getAsJsonArray("documents")?.size() == 0)
                        mainView.showErrorMessage(R.string.error_no_result)
                    else {
                        mainView.startResultActivity(lastResult!!)
                    }
                } else
                    mainView.showErrorMessage(R.string.error_no_result)
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                mainView.showErrorMessage(R.string.error_connection_status)
            }
        })
    }
}
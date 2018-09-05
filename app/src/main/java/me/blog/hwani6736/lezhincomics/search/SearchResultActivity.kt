package me.blog.hwani6736.lezhincomics.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_search_result.*
import me.blog.hwani6736.lezhincomics.R
import me.blog.hwani6736.lezhincomics.main.BaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by NarZa on 2018. 9. 4..
 */

class SearchResultActivity : BaseActivity(), SearchResultAdapter.OnItemClickListener, OnPageClickListener {
    companion object {
        const val EXTRAS_RESULT = "result_json"
        const val EXTRAS_KEYWORD = "keyword"
    }

    private lateinit var viewAdapter: SearchResultAdapter
    private var isEnd: Boolean = false
    private var totalCount: Int = 0
    private var pageableCount: Int = 0
    private var lastPageIndex = 1
    private lateinit var pageData: PageData
    private lateinit var keyword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        super.setHomeKey(R.drawable.baseline_arrow_back_24)

        initViews()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        initViews()
    }

    private fun initViews() {

        val jsonString = intent.extras.getString(EXTRAS_RESULT, "")
        keyword = intent.extras.getString(EXTRAS_KEYWORD, "")
        val searchResponse = SearchResponse.parseJson(JsonParser().parse(jsonString).asJsonObject)

        PageData(lastPageIndex, searchResponse.metaData.isEnd, searchResponse.metaData.pageableCount, this@SearchResultActivity)
                .apply {
                    pageData = this
                    viewAdapter = SearchResultAdapter(this@SearchResultActivity, this@SearchResultActivity, pageData)
                }


        findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchResultActivity)
            adapter = viewAdapter
        }

        displayList(searchResponse)
    }

    private fun displayList(searchResponse: SearchResponse) {
        isEnd = searchResponse.metaData.isEnd
        totalCount = searchResponse.metaData.totalCount
        pageableCount = searchResponse.metaData.pageableCount

        viewAdapter.setItem(searchResponse)
    }

    override fun onItemClick(document: SearchResponse.Document) {
        val intent = Intent(this, SearchDetailActivity::class.java)

        intent.putExtra(SearchDetailActivity.EXTRA_SEARCH_RESULT, document)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.startActivityWithExplode(intent, iv_thumbnail)
        } else {
            startActivity(intent)
        }
    }

    override fun onPageClick(number: Int) {

        if (pageData.page != number) {
            lastPageIndex = number
            SearchApiCaller.searchImage(keyword, lastPageIndex, object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val lastResult = response.body()

                    if (lastResult != null) {
                        if (lastResult.getAsJsonArray("documents")?.size() == 0)
                            showErrorMessage(R.string.error_no_result)
                        else {
                            intent.putExtra(SearchResultActivity.EXTRAS_RESULT, lastResult.toString())
                            intent.putExtra(SearchResultActivity.EXTRAS_KEYWORD, keyword)
                            initViews()
                        }
                    } else
                        showErrorMessage(R.string.error_no_result)
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    showErrorMessage(R.string.error_connection_status)
                }
            })
        }
    }

    fun showErrorMessage(resId: Int) {

        runOnUiThread {
            Snackbar.make(window.decorView.findViewById(android.R.id.content),
                    getString(resId),
                    Snackbar.LENGTH_SHORT).show() }
    }
}

interface OnPageClickListener {
    fun onPageClick(number: Int)
}
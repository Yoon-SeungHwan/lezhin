package me.blog.hwani6736.lezhincomics.search

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*
import me.blog.hwani6736.lezhincomics.main.BaseActivity
import me.blog.hwani6736.lezhincomics.R

/**
 * Created by NarZa on 2018. 9. 5..
 */
class SearchDetailActivity: BaseActivity() {

    companion object { const val EXTRA_SEARCH_RESULT = "detail_image_result" }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)

        super.setHomeKey(R.drawable.baseline_arrow_back_24)

        intent.getParcelableExtra<SearchResponse.Document>(EXTRA_SEARCH_RESULT)
                .let {
                    detail_image.aspectRatio = (it.width.toFloat() / it.height.toFloat())
                    detail_image.setImageURI(it.imageUrl)
                }
    }

}
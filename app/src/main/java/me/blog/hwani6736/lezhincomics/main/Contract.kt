package me.blog.hwani6736.lezhincomics.main

import android.view.MenuItem
import com.google.gson.JsonObject

/**
 * Created by NarZa on 2018. 9. 6..
 */
interface Contract {
    interface View {
        fun showErrorMessage(resId: Int)
        fun swapToolbar(item: MenuItem)
        fun startResultActivity(json:JsonObject)
    }

    interface Presenter {
        fun onSearchKeywordInput(keyword: String)
        fun onSearch(keyword:String)
    }
}
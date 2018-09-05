package me.blog.hwani6736.lezhincomics.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.facebook.common.util.UriUtil
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import me.blog.hwani6736.lezhincomics.R
import me.blog.hwani6736.lezhincomics.search.SearchResultActivity
import me.blog.hwani6736.lezhincomics.util.LeZhinUtil


/**
 * Created by NarZa on 2018. 9. 6..
 */
class MainActivity: BaseActivity(), Contract.View {

    private val TAG = MainActivity::class.java.simpleName
    private val presenter = MainPresenter(this)
    private var toast:Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setHomeKey(R.drawable.baseline_menu_24)

        initViews()
    }

    private fun initViews() {
        input_search_keyword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null)
                    presenter.onSearchKeywordInput(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

        input_search_keyword.onFocusChangeListener = object :View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (!p1)
                    LeZhinUtil.hideKeyboard(this@MainActivity, p0!!)
            }
        }

        val uri = Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(R.drawable.body.toString())
                .build()

        body_image.setImageURI(uri.toString())
        body_image.aspectRatio = 0.348387096774194f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            swapToolbar(item)
            return true
        } else if (id == android.R.id.home) {
            toast?.cancel()
            toast = Toast.makeText(this, R.string.greeting, Toast.LENGTH_SHORT)
            toast?.show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showErrorMessage(resId: Int) {

        runOnUiThread {
            Snackbar.make(window.decorView.findViewById(android.R.id.content),
                    getString(resId),
                    Snackbar.LENGTH_SHORT).show() }
    }

    override fun swapToolbar(item: MenuItem) {
        val title = item.title.toString()

        if (getString(R.string.action_search) == title) {
            search_frame.visibility = View.VISIBLE
            input_search_keyword.requestFocus()
            LeZhinUtil.showKeyboard(this)
            item.setIcon(R.drawable.baseline_close_24)
            item.setTitle(R.string.action_close)
        } else {
            search_frame.visibility = View.GONE
            input_search_keyword.setText("")
            LeZhinUtil.hideKeyboard(this, input_search_keyword)
            item.setIcon(R.drawable.baseline_search_24)
            item.setTitle(R.string.action_search)
        }
    }

    override fun startResultActivity(json: JsonObject) {
        LeZhinUtil.hideKeyboard(this, input_search_keyword)

        if (!isFinishing) {
            val intent = Intent(this, SearchResultActivity::class.java)
            intent.putExtra(SearchResultActivity.EXTRAS_RESULT, json.toString())
            intent.putExtra(SearchResultActivity.EXTRAS_KEYWORD, input_search_keyword.text.toString())
            startActivity(intent)
        }
    }
}
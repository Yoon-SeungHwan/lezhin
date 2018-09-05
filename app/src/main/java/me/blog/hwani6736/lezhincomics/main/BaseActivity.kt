package me.blog.hwani6736.lezhincomics.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import me.blog.hwani6736.lezhincomics.R

/**
 * Created by NarZa on 2018. 9. 5..
 */
open class BaseActivity:AppCompatActivity() {

    override fun setContentView(layoutResID: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            supportPostponeEnterTransition()
            supportStartPostponedEnterTransition()
        }
        super.setContentView(layoutResID)
    }

    protected fun setHomeKey(resId: Int) {
        if (supportActionBar == null)
            setToolbar(R.id.toolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(resId)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setToolbar(resId: Int) {
        val toolbar = findViewById<Toolbar>(resId)
        if (toolbar != null)
            setSupportActionBar(toolbar)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startActivityWithExplode(intent: Intent, element: View) {

        window.enterTransition = Explode()
        window.exitTransition = Explode()

        ActivityOptions
                .makeSceneTransitionAnimation(this, element, ViewCompat.getTransitionName(element))
                .apply { startActivity(intent, this.toBundle()) }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

}
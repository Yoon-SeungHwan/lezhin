package me.blog.hwani6736.lezhincomics.util

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.graphics.PointF
import android.view.WindowManager
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * Created by NarZa on 2018. 9. 5..
 */
class LeZhinUtil {
    companion object {

        fun showKeyboard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        fun hideKeyboard(context: Context, view: View) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun getScreenSize(context: Context): Point {
            val dm = DisplayMetrics()
            val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(dm)

            return Point(dm.widthPixels, dm.heightPixels)
        }
    }
}
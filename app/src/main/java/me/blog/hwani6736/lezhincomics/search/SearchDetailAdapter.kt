package me.blog.hwani6736.lezhincomics.search

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.controller.ControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.view_search_detail.view.*
import me.blog.hwani6736.lezhincomics.R

/**
 * Created by NarZa on 2018. 9. 5..
 */
@Deprecated("SearchDetailActivity#RecyclerView 에서 NestedScrollView 로 전환하면서 미사용")
class SearchDetailAdapter(
        private val activity:AppCompatActivity
        , private val document: SearchResponse.Document)
    : RecyclerView.Adapter<SearchDetailAdapter.ViewHolder>() {

    private val inflater = LayoutInflater.from(activity)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDetailAdapter.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.view_search_detail, parent, false),
                activity)
    }

    override fun onBindViewHolder(holder: SearchDetailAdapter.ViewHolder, position: Int) {
        holder.onBind(document)
    }

    override fun getItemCount() = if (TextUtils.isEmpty(document.imageUrl)) 0 else 1

    class ViewHolder internal constructor(
            itemView: View
            , private val activity: AppCompatActivity)
        :RecyclerView.ViewHolder(itemView)  {

        fun onBind(document: SearchResponse.Document) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.detail_image.transitionName = "image_search_result"
                activity.supportStartPostponedEnterTransition()
            }

            Fresco.newDraweeControllerBuilder()
                    .setControllerListener(BaseControllerListener<ImageInfo>())
                    .build()
                    .apply { itemView.detail_image.controller = this }

            itemView.detail_image.setImageURI(document.imageUrl)
            itemView.detail_image.aspectRatio = (document.width.toFloat() / document.height.toFloat())
        }
    }
}
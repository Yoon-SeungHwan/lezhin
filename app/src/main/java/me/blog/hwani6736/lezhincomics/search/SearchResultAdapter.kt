package me.blog.hwani6736.lezhincomics.search

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.view_search_result_page.view.*
import me.blog.hwani6736.lezhincomics.R

/**
 * Created by NarZa on 2018. 9. 4..
 */
const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_PAGER = 1
class SearchResultAdapter (private val context: Context, private val clickListener: OnItemClickListener, private val pageData: PageData)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var itemList = ArrayList<Any>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType) {
            VIEW_TYPE_ITEM -> return SearchResultViewHolder(context, inflater.inflate(R.layout.view_search_result, parent, false))
            else -> return PagingViewHolder(inflater.inflate(R.layout.view_search_result_page, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {

        with(itemList.get(position)) {
            if (this is SearchResponse.Document)
                return VIEW_TYPE_ITEM
            else if (this is PageData)
                return VIEW_TYPE_PAGER
            else
                return super.getItemViewType(position)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchResultViewHolder) {
            with(itemList[position] as SearchResponse.Document) {
                holder.onBind(this )
                holder.itemView.setOnClickListener { clickListener.onItemClick(this) }
            }
        } else if (holder is PagingViewHolder) {
            holder.onBind(itemList[position] as PageData)
        }
    }

    fun setItem(searchResult: SearchResponse) {

        itemList.clear()
        itemList.addAll(searchResult.documents)
        itemList.add(pageData)

        notifyDataSetChanged()
    }

    override fun getItemCount() = itemList.size

    interface OnItemClickListener {
        fun onItemClick(document: SearchResponse.Document)
    }
}

data class PageData(var page:Int, val isEnd:Boolean, val pageableCount:Int, val listener: OnPageClickListener)

class SearchResultViewHolder internal constructor(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal fun onBind(document: SearchResponse.Document) {
        itemView.findViewById<SimpleDraweeView>(R.id.iv_thumbnail).setImageURI(document.thumbnailUrl)
        itemView.findViewById<AppCompatTextView>(R.id.tv_doc_url).text = document.docUrl.trim()

        val imgSize = context.resources.getString(R.string.format_image_size, document.width, document.height)
        itemView.findViewById<AppCompatTextView>(R.id.tv_img_size).text = imgSize

        itemView.setOnClickListener {  }
    }

}

class PagingViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val btnArray = arrayOf(itemView.btn_page_1, itemView.btn_page_2, itemView.btn_page_3, itemView.btn_page_4, itemView.btn_page_5)
    private var lastIndex: Int = 1;
    internal fun onBind(pageData: PageData) {

        val maxPage:Int = pageData.pageableCount.div(SearchApiCaller.DEFAULT_SIZE)
                .let {
                    if(pageData.pageableCount % SearchApiCaller.DEFAULT_SIZE > 0) it + 1
                    else it
                }

        val startPage = pageData.page.minus((pageData.page - 1) % btnArray.size )

        for (i in 0..4 ) {
            if ( (startPage + i).compareTo(maxPage) > 0 ) {
                btnArray[i].visibility = View.GONE
            } else {
                var pageIndex: Int = pageData.page

                with(btnArray[i]) {
                    visibility = View.VISIBLE
                    pageIndex = startPage.plus(i)
                    text = pageIndex.toString()

                    isSelected =  (pageData.page == pageIndex)
                    setOnClickListener{kotlin.run { pageData.listener.onPageClick(pageIndex) }}
                }
                lastIndex = pageIndex
            }
        }

        itemView.btn_next.setOnClickListener{
            kotlin.run {
                if (btnArray[btnArray.size-1].visibility == View.VISIBLE)
                    pageData.listener.onPageClick(lastIndex + 1)
                else
                    Toast.makeText(itemView.context, R.string.alert_last_page, Toast.LENGTH_SHORT).show()
            }
        }

        itemView.btn_prev.setOnClickListener {
            kotlin.run {
                if (lastIndex > btnArray.size) {
                    val startPage = pageData.page.minus((lastIndex - btnArray.size - 1) % btnArray.size )
                    pageData.listener.onPageClick(startPage)
                } else if (lastIndex > 1)
                    pageData.listener.onPageClick(lastIndex - 1)
                else
                    Toast.makeText(itemView.context, R.string.alert_first_page, Toast.LENGTH_SHORT).show()
            }
        }
    }

}

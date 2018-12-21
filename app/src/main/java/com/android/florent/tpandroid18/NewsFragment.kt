package com.android.florent.tpandroid18

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.util.DiffUtil
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.list_item_news.view.*


class NewsFragment : Fragment(), NewsAdapter.OnNewsItemClickListener {
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsAdapter(listener = this)

        news_list.setHasFixedSize(true)
        news_list.layoutManager = LinearLayoutManager(context)
        news_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        news_list.adapter = adapter

        ViewModelProviders
                .of(this).get(WelcomeScreenViewModel::class.java)
                .news.observe(this, object: Observer<List<News>> {
            override fun onChanged(t: List<News>?) {
                if (t != null)
                    adapter.updateContent(t)
                news_progress_bar.visibility = View.GONE
            }

        })
    }
    override fun onNewsClicked(news: News) {
        // Ouvrir la deuxième activité

        val url = news.link

        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
}

private class NewsAdapter(private val list: MutableList<News> = mutableListOf(),
                          private var listener: OnNewsItemClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun updateContent(list: List<News>) {
        this.list.replaceContent(list)
        notifyDataSetChanged()
    }

    fun updateContentWithDiffUtil(list: List<News>) {
        val oldList = this.list.toMutableList()
        this.list.replaceContent(list)
        DiffUtil.calculateDiff(NewsDiff(oldList = oldList, newList = list)).dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsItemViewHolder(
                LayoutInflater.from(container.context)
                        .inflate(R.layout.list_item_news, container, false)
        )
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsItemViewHolder) {
            val news = list[position]
            holder.bindNews(news)

            holder.itemView.setOnClickListener { listener?.onNewsClicked(news) }
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnNewsItemClickListener {
        fun onNewsClicked(news: News)
    }

}

class NewsItemViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val thumbnail: AppCompatImageView = v.news_item_thumbnail
    private val title: AppCompatTextView = v.news_item_title
    private val subtitle: AppCompatTextView = v.news_item_subtitle
    private val excerpt: AppCompatTextView = v.news_item_excerpt

    @SuppressLint("SetTextI18n")
    fun bindNews(newsItem: News) {
        Picasso.get().load(newsItem.picture).into(thumbnail)
        title.text = newsItem.title
        subtitle.text = "${newsItem.author} ${newsItem.date}" // TODO Replace with a resource
        excerpt.text = newsItem.summary
    }

}

private class NewsDiff(val oldList: List<News>, val newList: List<News>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(p0: Int, p1: Int): Boolean = oldList[p0] == newList[p1]

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean = oldList[p0] == newList[p1]

}

fun <T> MutableList<T>.replaceContent(list: List<T>) {
    clear()
    addAll(list)
}

fun <T> MutableList<T>.addNonNull(item: T?) {
    if (item != null) this.add(item)
}
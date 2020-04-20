package com.imceits.android.dbsbeeceptortask.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imceits.android.dbsbeeceptortask.data.Article
import com.imceits.android.dbsbeeceptortask.databinding.ArticleItemBinding

class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.DataViewHolder>() {
    private var dataList : MutableList<Article> = mutableListOf()

    fun setData(data: List<Article>?) {
        data?.let {
            dataList = it as MutableList<Article>
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemBinding = ArticleItemBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return DataViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.onBind(dataList[position])
    }

    class DataViewHolder(private val itemBinding: ArticleItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(article: Article) {
            itemBinding.data = article
            itemBinding.executePendingBindings()
        }
    }
}
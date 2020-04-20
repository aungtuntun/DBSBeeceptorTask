package com.imceits.android.dbsbeeceptortask.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.imceits.android.dbsbeeceptortask.data.Article
import com.imceits.android.dbsbeeceptortask.databinding.ArticleItemBinding

class ArticleAdapter: RecyclerView.Adapter<ArticleAdapter.DataViewHolder>() {
    private var dataList : MutableList<Article> = mutableListOf()

    fun setData(data: List<Article>?) {
        data?.let {
            dataList = it as MutableList<Article>
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemBinding = ArticleItemBinding.inflate(LayoutInflater.from(parent.context),
        parent, false)
        return DataViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.onBind(dataList[position])
        val directions = ArticleFragmentDirections.actionNavArticleToNavDetail()
        directions.articleId = dataList[position].id
        holder.itemBinding.cardArticle.setOnClickListener(Navigation.createNavigateOnClickListener(directions))
       /* holder.itemBinding.cardArticle.setOnClickListener{
            it.findNavController().navigate(directions)
        }*/
    }

    class DataViewHolder(val itemBinding: ArticleItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(article: Article) {
            itemBinding.data = article
            itemBinding.executePendingBindings()
        }
    }
}
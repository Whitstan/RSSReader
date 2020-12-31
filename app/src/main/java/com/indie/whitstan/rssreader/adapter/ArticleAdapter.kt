package com.indie.whitstan.rssreader.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

import com.chauthai.swipereveallayout.ViewBinderHelper

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.ArticleAdapter.*
import com.indie.whitstan.rssreader.databinding.RowArticleBinding
import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

class ArticleAdapter(private val itemViewModel: ItemViewModel) : RecyclerView.Adapter<ArticleViewHolder>() {
    private var binding: RowArticleBinding? = null
    private val viewBinderHelper = ViewBinderHelper()

    private var articlesList: List<Article> = arrayListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            RowArticleBinding.inflate(LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item =  articlesList[position]
        holder.bind(item)
        if (binding == null){
            Log.d("asd","It was null bruh.")
        }
        viewBinderHelper.bind(binding!!.srlArticleRow, item.hashCode().toString())
        viewBinderHelper.setOpenOnlyOne(true)
    }

    fun setItems(itemsList: List<Article>){
        this.articlesList = itemsList
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(private val binding: RowArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        private var btnAddToFavorites: Button = binding.btnAddToFavorites

        fun bind(item: Article) {
            this@ArticleAdapter.binding = binding.apply {
                article = item
                executePendingBindings()
            }
        }

        init {
            btnAddToFavorites.setOnClickListener {
                val article = binding.article!!
                article.setFavorite(!article.isFavorite())
                itemViewModel.updateArticle(article)
            }


            binding.mainlayout.setOnClickListener{ view ->
                val args = bundleOf(
                    Pair("title",  binding.article!!.title),
                    Pair("description", binding.article!!.description),
                    Pair("pubDate", binding.article!!.pubDate),
                    Pair("link", binding.article!!.link)
                )
                view.findNavController().navigate(R.id.navigate_to_item_details_from_list, args)
            }
        }
    }

    override fun getItemCount(): Int {
        return articlesList.size
    }

}
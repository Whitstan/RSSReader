package com.indie.whitstan.rssreader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.chauthai.swipereveallayout.ViewBinderHelper

import org.koin.java.KoinJavaComponent.inject

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.ArticleAdapter.*
import com.indie.whitstan.rssreader.databinding.RowArticleBinding
import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.persistence.ItemRepository
import com.indie.whitstan.rssreader.util.Converters

class ArticleAdapter : RecyclerView.Adapter<ArticleViewHolder>() {
    private var binding: RowArticleBinding? = null
    private val viewBinderHelper = ViewBinderHelper()
    val repository : ItemRepository by inject(ItemRepository::class.java)

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
        viewBinderHelper.bind(binding!!.swipereveallayout, item.guid)
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
                GlobalScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        binding.article?.setFavorite(!binding.article?.isFavorite()!!)
                        repository.updateArticle(binding.article!!)
                        repository.insertFavoriteArticle(Converters.convertArticleToFavoriteArticle(binding.article!!))
                        notifyDataSetChanged()
                    }
                }
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
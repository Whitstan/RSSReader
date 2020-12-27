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
import com.indie.whitstan.rssreader.adapter.FavoriteArticleAdapter.*
import com.indie.whitstan.rssreader.databinding.RowFavoriteArticleBinding
import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.persistence.ItemRepository

class FavoriteArticleAdapter : RecyclerView.Adapter<FavoriteArticleViewHolder>() {
    private var binding: RowFavoriteArticleBinding? = null
    private val viewBinderHelper = ViewBinderHelper()
    val repository : ItemRepository by inject(ItemRepository::class.java)

    private var favoritesList: List<Article> = arrayListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): FavoriteArticleViewHolder {
        return FavoriteArticleViewHolder(
            RowFavoriteArticleBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FavoriteArticleViewHolder, position: Int) {
        val item =  favoritesList[position]
        holder.bind(item)
        viewBinderHelper.bind(binding!!.swipereveallayout, item.guid)
        viewBinderHelper.setOpenOnlyOne(true)
    }

    fun setItems(list: List<Article>){
        favoritesList = list
        notifyDataSetChanged()
    }

    fun closeAllSwipeRevealLayouts(){
        if (binding != null){
            binding!!.swipereveallayout.close(true)
        }
    }

    inner class FavoriteArticleViewHolder(private val binding: RowFavoriteArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        private var btnDeleteFromFavorites: Button = binding.btnDeleteFromFavorites

        fun bind(item: Article) {
            this@FavoriteArticleAdapter.binding = binding.apply {
                favorite = item
                executePendingBindings()
            }
        }

        init {
            btnDeleteFromFavorites.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        binding.favorite?.setFavorite(false)
                        repository.updateArticle(binding.favorite!!)

                    }
                }
            }

            binding.mainlayout.setOnClickListener{ view ->
                val args = bundleOf(
                    Pair("title",  binding.favorite!!.title),
                    Pair("description", binding.favorite!!.description),
                    Pair("pubDate", binding.favorite!!.pubDate),
                    Pair("link", binding.favorite!!.link)
                )
                view.findNavController().navigate(R.id.navigate_to_item_details_from_favorites, args)
            }
        }
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

}
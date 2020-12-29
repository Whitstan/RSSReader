package com.indie.whitstan.rssreader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

import com.chauthai.swipereveallayout.ViewBinderHelper

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.FavoriteArticleAdapter.*
import com.indie.whitstan.rssreader.databinding.RowFavoriteArticleBinding
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle
import com.indie.whitstan.rssreader.util.Converters
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

class FavoriteArticleAdapter(private val itemViewModel: ItemViewModel) : RecyclerView.Adapter<FavoriteArticleViewHolder>() {
    private var binding: RowFavoriteArticleBinding? = null
    private val viewBinderHelper = ViewBinderHelper()

    private var favoritesList: ArrayList<FavoriteArticle> = arrayListOf()

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
        viewBinderHelper.bind(binding!!.swipereveallayout, item.hashCode().toString())
        viewBinderHelper.setOpenOnlyOne(true)
    }

    fun setItems(list: List<FavoriteArticle>){
        favoritesList = list as ArrayList<FavoriteArticle>
        notifyDataSetChanged()
    }

    inner class FavoriteArticleViewHolder(private val binding: RowFavoriteArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        private var btnDeleteFromFavorites: Button = binding.btnDeleteFromFavorites

        fun bind(item: FavoriteArticle) {
            this@FavoriteArticleAdapter.binding = binding.apply {
                favorite = item
                executePendingBindings()
            }
        }

        init {
            btnDeleteFromFavorites.setOnClickListener {
                itemViewModel.deleteFavoriteArticle(binding.favorite!!)
                itemViewModel.updateArticle(Converters.convertFavoriteArticleToArticle(binding.favorite!!, false))
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
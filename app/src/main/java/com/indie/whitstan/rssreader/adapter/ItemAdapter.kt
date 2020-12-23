package com.indie.whitstan.rssreader.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import com.chauthai.swipereveallayout.ViewBinderHelper

import com.google.android.material.snackbar.Snackbar

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.ItemAdapter.RSSItemViewHolder
import com.indie.whitstan.rssreader.databinding.RssItemRowBinding
import com.indie.whitstan.rssreader.model.Item
import com.indie.whitstan.rssreader.util.MODE_DELETE
import com.indie.whitstan.rssreader.util.MODE_FAVORITE
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

class ItemAdapter(private val mItemViewModel: ItemViewModel, var mItemsList: List<Item>) : RecyclerView.Adapter<RSSItemViewHolder>() {
    private var mMode : Int = 0
    private var mBinding: RssItemRowBinding? = null
    private val mViewBinderHelper = ViewBinderHelper()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RSSItemViewHolder {
        return RSSItemViewHolder(
                RssItemRowBinding.inflate(LayoutInflater.from(viewGroup.context),
                        viewGroup,
                        false)
        )
    }

    override fun onBindViewHolder(holder: RSSItemViewHolder, position: Int) {
        val rssItem =  mItemsList[position]
        holder.bind(rssItem)
        mViewBinderHelper.bind(mBinding!!.swipereveallayout, rssItem.guid)
        mViewBinderHelper.setOpenOnlyOne(true)
    }

    fun setMode(mode: Int){
        mMode = mode
    }

    fun setRSSItems(itemsList: List<Item>){
        mItemsList = itemsList
    }

    fun closeAllSwipeRevealLayouts(){
        if (mBinding != null){
            mBinding!!.swipereveallayout.close(true)
        }
    }

    inner class RSSItemViewHolder(private val binding: RssItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        private var btnAddToFavorites: Button = binding.btnAddToFavorites
        private var btnDeleteFromFavorites: Button = binding.btnDeleteFromFavorites

        fun bind(item: Item) {
            mBinding = binding.apply {
                rssitem = item
                executePendingBindings()
            }
        }

        private fun showSnackBar(text : String){
            val snackBar = Snackbar.make(itemView, text, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
            snackBar.setActionTextColor(Color.BLUE)
            snackBar.duration = 1500
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.DKGRAY)
            val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.CYAN)
            snackBar.show()
        }

        init {
            if (mMode == MODE_FAVORITE){
                (btnDeleteFromFavorites.parent as ViewGroup).removeView(btnDeleteFromFavorites)
                btnAddToFavorites.setOnClickListener {
                    GlobalScope.launch(Dispatchers.IO) {
                        val isFavorite = mItemViewModel.isRSSItemStoredInDB(binding.rssitem?.guid)
                        withContext(Dispatchers.Main) {
                            if (!isFavorite){
                                mItemViewModel.insertRSSItem(binding.rssitem)
                                showSnackBar("Added to Favorites!")
                            }
                            else{
                                mItemViewModel.deleteRSSItem(binding.rssitem)
                                showSnackBar("Deleted from Favorites!")
                            }
                            binding.rssitem?.setFavorite(!binding.rssitem?.getFavorite()!!)
                        }
                    }
                }
            }
            else if (mMode == MODE_DELETE){
                (btnAddToFavorites.parent as ViewGroup).removeView(btnAddToFavorites)
                btnDeleteFromFavorites.setOnClickListener {
                    GlobalScope.launch(Dispatchers.IO) {
                        withContext(Dispatchers.Main) {
                            mItemViewModel.deleteRSSItem(binding.rssitem)
                            binding.rssitem?.setFavorite(false)
                            showSnackBar("Deleted from Favorites!")
                        }
                    }
                }
            }

            binding.mainlayout.setOnClickListener{ view ->
                val args = bundleOf(
                    Pair("title",  binding.rssitem!!.title),
                    Pair("description", binding.rssitem!!.description),
                    Pair("pubDate", binding.rssitem!!.pubDate),
                    Pair("link", binding.rssitem!!.link)
                )

                val navController = view.findNavController()

                when(mMode){
                    MODE_FAVORITE ->{
                        navController.navigate(R.id.navigate_to_item_details_from_list, args)
                    }
                    MODE_DELETE -> {
                        navController.navigate(R.id.navigate_to_item_details_from_favorites, args)
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mItemsList.size
    }

}
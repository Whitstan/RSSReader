package com.indie.whitstan.rssreader.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import org.koin.android.viewmodel.ext.android.sharedViewModel

import kotlinx.android.synthetic.main.fragment_favorites_list.*

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.FavoriteArticleAdapter
import com.indie.whitstan.rssreader.databinding.FragmentFavoritesListBinding
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding : FragmentFavoritesListBinding
    private val itemViewModel : ItemViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_favorites_list,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        itemViewModel.loadFavoritesFromDb()
        itemViewModel.favoritesMediatorData.observe(viewLifecycleOwner, { favorites ->
            favorites?.let {
                val adapter = FavoriteArticleAdapter()
                adapter.setItems(favorites)
                rvFavoritesList.adapter = adapter
                rvFavoritesList.adapter!!.notifyDataSetChanged()
                hideLoadingIndicator()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        showLoadingIndicator()
    }

    private fun showLoadingIndicator() {
        rvFavoritesList.visibility = View.GONE
        clpbFavoritesList.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        rvFavoritesList.visibility = View.VISIBLE
        clpbFavoritesList.visibility = View.GONE
    }

    private fun setupViews() {
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvFavoritesList.layoutManager = linearLayoutManager
        rvFavoritesList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    }
}
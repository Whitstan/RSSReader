package com.indie.whitstan.rssreader.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.adapter.ItemAdapter
import com.indie.whitstan.rssreader.databinding.FragmentFavoritesBinding
import com.indie.whitstan.rssreader.util.MODE_DELETE
import com.indie.whitstan.rssreader.viewmodel.ItemViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding : FragmentFavoritesBinding

    private lateinit var mItemViewModel: ItemViewModel

    private lateinit var mAdapter: ItemAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ContentLoadingProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_favorites,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        mItemViewModel.allItems.observe(viewLifecycleOwner, { rssItems ->
            rssItems?.let {
                mAdapter = ItemAdapter(mItemViewModel, rssItems)
                mAdapter.setRSSItems(rssItems)
                mAdapter.setMode(MODE_DELETE)
                mRecyclerView.adapter = mAdapter
                mAdapter.notifyDataSetChanged()
                hideLoadingIndicator()
            }
        })
    }

    private fun setupViewModel(){
        mItemViewModel = ViewModelProviders.of(this).get(ItemViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        showLoadingIndicator()
    }

    private fun showLoadingIndicator() {
        mRecyclerView.visibility = View.GONE
        mProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        mRecyclerView.visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
    }

    private fun setupViews() {
        mRecyclerView = binding.recyclerViewSavedItems
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        mProgressBar = binding.progressBarSavedItems
    }
}
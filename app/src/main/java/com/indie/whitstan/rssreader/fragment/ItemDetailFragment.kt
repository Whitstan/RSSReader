package com.indie.whitstan.rssreader.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.indie.whitstan.rssreader.R
import com.indie.whitstan.rssreader.databinding.FragmentRssItemDetailsBinding
import com.indie.whitstan.rssreader.fragment.base.BaseFragment

class ItemDetailFragment : BaseFragment() {
    private lateinit var binding : FragmentRssItemDetailsBinding

    private val args: ItemDetailFragmentArgs by navArgs()

    private lateinit var mTitleText: TextView
    private lateinit var mDescriptionText: TextView
    private lateinit var mPubDateText: TextView
    private lateinit var mLink: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rss_item_details,
                container,
                false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupFieldReferences()
        setupFieldsData(args)
    }

    private fun setupFieldsData(data: ItemDetailFragmentArgs){
        mTitleText.text = data.title
        mDescriptionText.text = data.description
        mPubDateText.text = data.pubDate
        mLink.text = data.link

        mLink.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mLink.text.toString())))
        }
    }

    private fun setupFieldReferences() {
        mTitleText = binding.textTitle
        mDescriptionText = binding.textDescription
        mPubDateText = binding.textPubDate
        mLink = binding.textLink
    }
}
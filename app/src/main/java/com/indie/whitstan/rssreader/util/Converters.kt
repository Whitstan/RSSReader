package com.indie.whitstan.rssreader.util

import com.indie.whitstan.rssreader.model.network.RSSItem
import com.indie.whitstan.rssreader.model.persistence.LocalArticle

class Converters {
    companion object{

        fun convertRssItemsToArticles(rssItems : List<RSSItem>?) : List<LocalArticle>{
            val resultList : ArrayList<LocalArticle> = arrayListOf()
            if (rssItems != null) {
                for (rssItem in rssItems) {
                    val article = LocalArticle()
                    article.title = rssItem.title
                    article.description = rssItem.description
                    article.pubDate = rssItem.pubDate
                    article.link = rssItem.link
                    article.setFavorite(false)
                    resultList.add(article)
                }
            }
            return resultList
        }

    }
}
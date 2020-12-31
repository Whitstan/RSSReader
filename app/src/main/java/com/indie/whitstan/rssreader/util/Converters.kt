package com.indie.whitstan.rssreader.util

import com.indie.whitstan.rssreader.model.network.RSSItem
import com.indie.whitstan.rssreader.model.persistence.Article

class Converters {
    companion object{

        fun convertRssItemsToArticles(rssItems : List<RSSItem>?) : List<Article>{
            val resultList : ArrayList<Article> = arrayListOf()
            if (rssItems != null) {
                for (rssItem in rssItems) {
                    val article = Article()
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
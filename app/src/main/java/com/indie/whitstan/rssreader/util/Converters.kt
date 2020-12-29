package com.indie.whitstan.rssreader.util

import com.indie.whitstan.rssreader.model.network.RSSItem
import com.indie.whitstan.rssreader.model.persistence.Article
import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle

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

        fun convertArticleToFavoriteArticle(article: Article) : FavoriteArticle{
            val resultFavoriteArticle = FavoriteArticle()
            //resultFavoriteArticle.id = article.id
            resultFavoriteArticle.title = article.title
            resultFavoriteArticle.description = article.description
            resultFavoriteArticle.pubDate = article.pubDate
            resultFavoriteArticle.link = article.link
            return resultFavoriteArticle
        }

        fun convertFavoriteArticleToArticle(favoriteArticle: FavoriteArticle, isFavorite : Boolean) : Article{
            val resultArticle = Article()
            //resultArticle.id = favoriteArticle.id
            resultArticle.title = favoriteArticle.title
            resultArticle.description = favoriteArticle.description
            resultArticle.pubDate = favoriteArticle.pubDate
            resultArticle.link = favoriteArticle.link
            resultArticle.setFavorite(isFavorite)
            return resultArticle
        }

    }
}
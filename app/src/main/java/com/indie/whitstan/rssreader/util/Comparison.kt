package com.indie.whitstan.rssreader.util

import com.indie.whitstan.rssreader.model.persistence.FavoriteArticle
import com.indie.whitstan.rssreader.model.persistence.LocalArticle

class Comparison {
    companion object{
        fun crossEquals(localArticle: LocalArticle, favoriteArticle: FavoriteArticle) : Boolean{
            return (localArticle.title == favoriteArticle.title &&
                    localArticle.pubDate == favoriteArticle.pubDate &&
                    localArticle.description == favoriteArticle.description)
        }
    }
}
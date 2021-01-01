package com.indie.whitstan.rssreader.model.persistence

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
class LocalArticle() : BaseObservable() {

    constructor(favoriteArticle: FavoriteArticle) : this() {
        title = favoriteArticle.title
        link = favoriteArticle.link
        pubDate = favoriteArticle.pubDate
        description = favoriteArticle.description
    }

    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Long = 0

    @field:ColumnInfo(name = "title")
    var title: String? = null

    @field:ColumnInfo(name = "link")
    var link: String? = null

    @field:ColumnInfo(name = "pubDate")
    var pubDate: String? = null

    @field:ColumnInfo(name = "description")
    var description: String? = null

    @field:ColumnInfo(name = "favorite")
    var fav: Boolean = false

    @Bindable
    fun isFavorite() : Boolean{
        return fav
    }

    @JvmName("setFavorite_")
    fun setFavorite(newValue : Boolean){
        fav = newValue
        notifyPropertyChanged(BR.favorite);
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + (pubDate?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalArticle

        if (title != other.title) return false
        if (link != other.link) return false
        if (pubDate != other.pubDate) return false
        if (description != other.description) return false

        return true
    }

}
package com.indie.whitstan.rssreader.model.persistence

import java.util.*

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "articles")
class Article : BaseObservable() {
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

    override fun equals(other: Any?): Boolean {
        if (other == null){
            return false
        }
        val otherConverted = other as Article
        return otherConverted.title == title && otherConverted.pubDate == pubDate && otherConverted.description == description
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (link?.hashCode() ?: 0)
        result = 31 * result + (pubDate?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + fav.hashCode()
        return result
    }
}
package com.indie.whitstan.rssreader.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import java.util.*

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
@Entity(tableName = "items")
class Item : BaseObservable() {
    @field:PrimaryKey
    @field:ColumnInfo(name = "guid")
    @field:Element(name = "guid", required = false)
    var guid: String = UUID.randomUUID().toString()

    @field:ColumnInfo(name = "title")
    @field:Element(name = "title", required = false)
    var title: String? = null

    @field:ColumnInfo(name = "link")
    @field:Element(name = "link", required = false)
    var link: String? = null

    @field:ColumnInfo(name = "pubDate")
    @field:Element(name = "pubDate", required = false)
    var pubDate: String? = null

    @field:ColumnInfo(name = "description")
    @field:Element(name = "description", required = false)
    var description: String? = null

    @Ignore
    var isFavorite: Boolean = false

    @Bindable
    fun getFavorite() : Boolean{
        return isFavorite
    }

    @JvmName("setFavorite_")
    fun setFavorite(newValue : Boolean){
        isFavorite = newValue
        notifyPropertyChanged(BR.favorite);
    }
}
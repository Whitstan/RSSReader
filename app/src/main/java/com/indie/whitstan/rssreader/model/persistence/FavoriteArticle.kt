package com.indie.whitstan.rssreader.model.persistence

import androidx.databinding.BaseObservable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
class FavoriteArticle() : BaseObservable() {

    constructor(localArticle: LocalArticle) : this() {
        title = localArticle.title
        link = localArticle.link
        pubDate = localArticle.pubDate
        description = localArticle.description
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

        other as FavoriteArticle

        if (title != other.title) return false
        if (link != other.link) return false
        if (pubDate != other.pubDate) return false
        if (description != other.description) return false

        return true
    }
}
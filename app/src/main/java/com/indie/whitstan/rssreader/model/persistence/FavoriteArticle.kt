package com.indie.whitstan.rssreader.model.persistence

import java.util.*

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritearticles")
class FavoriteArticle {
    @field:PrimaryKey
    @field:ColumnInfo(name = "guid")
    var guid: String = UUID.randomUUID().toString()

    @field:ColumnInfo(name = "title")
    var title: String? = null

    @field:ColumnInfo(name = "link")
    var link: String? = null

    @field:ColumnInfo(name = "pubDate")
    var pubDate: String? = null

    @field:ColumnInfo(name = "description")
    var description: String? = null
}
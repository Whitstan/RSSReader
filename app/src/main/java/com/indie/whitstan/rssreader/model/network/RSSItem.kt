package com.indie.whitstan.rssreader.model.network

import java.util.*

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
class RSSItem {
    @field:Element(name = "guid", required = false)
    var guid: String = UUID.randomUUID().toString()

    @field:Element(name = "title", required = false)
    var title: String? = null

    @field:Element(name = "link", required = false)
    var link: String? = null

    @field:Element(name = "pubDate", required = false)
    var pubDate: String? = null

    @field:Element(name = "description", required = false)
    var description: String? = null
}
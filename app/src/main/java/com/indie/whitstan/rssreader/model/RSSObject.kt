package com.indie.whitstan.rssreader.model

import org.simpleframework.xml.*

@Root(name = "rss", strict = false)
data class RSSObject @JvmOverloads constructor(
    @field:Element(name = "title", required = false)
    @field:Path("channel")
    var channelTitle: String? = "",

    @field:ElementList(name = "item", inline = true, required = false)
    @field:Path("channel")
    var items: List<Item>? = null
)




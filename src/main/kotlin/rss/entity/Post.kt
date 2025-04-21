package rss.entity

import java.time.LocalDateTime

data class Post(
    val title: String,
    val pubDate: LocalDateTime,
    val link: String,
)

package rss.model

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.w3c.dom.Node
import rss.entity.Post
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

class PostModel {
    suspend fun getPostList(rssSources: List<String>): List<Post> =
        coroutineScope {
            rssSources.map {
                async { initPost(it) }
            }.awaitAll().flatten()
        }

    fun filterPostList(
        postList: List<Post>,
        keyword: String,
    ) = postList.filter { it.title.contains(keyword) }

    fun sortPostList(postList: List<Post>) = postList.sortedByDescending { it.pubDate }

    fun cutPostList(
        postList: List<Post>,
        size: Int,
    ) = postList.take(size)

    private suspend fun initPost(source: String): List<Post> =
        coroutineScope {
            val factory = DocumentBuilderFactory.newInstance()
            val xml = factory.newDocumentBuilder().parse(source)

            val items = xml.getElementsByTagName("item")
            (0 until items.length).map { i ->
                async { parseItem(items.item(i)) }
            }.awaitAll()
        }

    private fun parseItem(item: Node): Post {
        val children = item.childNodes
        var title = ""
        var date = now()
        var link = ""
        for (i in 0 until children.length) {
            val child = children.item(i)
            when (child.nodeName) {
                "title" -> title = child.textContent
                "pubDate" -> {
                    val input = child.textContent
                    val inputFormatter = DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.ENGLISH)
                    date = LocalDateTime.parse(input, inputFormatter)
                }
                "link" -> link = child.textContent
            }
        }
        return Post(title, date, link)
    }
}

package rss.model

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.w3c.dom.Element
import rss.entity.Post
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
            List(items.length) { items.item(it) }
                .filterIsInstance<Element>()
                .map { it.toPost() }
        }

    private fun Element.toPost() =
        Post(
            this.textOf("title"),
            LocalDateTime.parse(this.textOf("pubDate"), DateTimeFormatter.RFC_1123_DATE_TIME),
            this.textOf("link"),
        )

    private fun Element.textOf(tagName: String): String {
        return getElementsByTagName(tagName).item(0)?.textContent.orEmpty()
    }
}

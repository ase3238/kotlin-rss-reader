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
            val channel = xml.getElementsByTagName("channel").item(0)
            List(channel.childNodes.length) { channel.childNodes.item(it) }
                .filterIsInstance<Element>()
                .filter { it.tagName == "item" }
                .map {
                    Post(
                        it.textOf("title"),
                        LocalDateTime.parse(it.textOf("pubDate"), DateTimeFormatter.RFC_1123_DATE_TIME),
                        it.textOf("link"),
                    )
                }
        }

    private fun Element.textOf(tagName: String): String {
        return getElementsByTagName(tagName).item(0)?.textContent.orEmpty()
    }
}

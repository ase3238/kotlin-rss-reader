package rss.model

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Node
import rss.entity.Post
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

class PostModel {
    fun getPostList(rssSources: List<String>): List<Post> {
        return runBlocking {
            rssSources
                .map {
                    async { initPost(it) }
                }
                .awaitAll()
                .flatMap { it }
        }
    }

    fun filterPostList(
        postList: List<Post>,
        keyword: String,
    ) = postList.filter { it.title.contains(keyword) }

    fun sortPostList(postList: List<Post>) = postList.sortedBy { it.pubDate }

    fun cutPostList(
        postList: List<Post>,
        size: Int,
    ) = postList.take(size)

    private fun initPost(source: String): MutableList<Post> {
        return runBlocking {
            val subList = mutableListOf<Post>()
            val factory = DocumentBuilderFactory.newInstance()
            val xml = factory.newDocumentBuilder().parse(source)

            val items = xml.getElementsByTagName("item")
            for (i in 0 until items.length) {
                val item = items.item(i)
                val post = async { parseItem(item) }
                subList.add(post.await())
            }
            subList
        }
    }

    private fun parseItem(item: Node): Post {
        val children = item.childNodes
        var title = ""
        var date = now()
        for (i in 0 until children.length) {
            val child = children.item(i)
            when (child.nodeName) {
                "title" -> title = child.textContent
                "pubDate" -> {
                    val input = child.textContent
                    val inputFormatter = DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.ENGLISH)
                    date = LocalDateTime.parse(input, inputFormatter)
                }
            }
        }
        return Post(title, date)
    }
}

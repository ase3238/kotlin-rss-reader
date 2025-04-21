package rss.model

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Node
import rss.entity.Post
import rss.entity.RSS_SOURCE
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

class PostModel {
    fun initPostList(): List<Post> {
        return runBlocking {
            RSS_SOURCE
                .map {
                    async { initPost(it) }
                }
                .awaitAll()
                .flatMap { it }
        }
    }

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
        var title: String = ""
        var date: LocalDateTime = now()
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

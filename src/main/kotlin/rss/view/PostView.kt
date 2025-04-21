package rss.view

import rss.entity.Post
import java.time.format.DateTimeFormatter

class PostView {
    fun showKeywordMsg() {
        println("검색어를 입력하세요 (없으면 전체 출력):")
    }

    fun showPost(posts: List<Post>) {
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        println()
        posts.forEachIndexed { idx, post ->
            println(
                StringBuilder().apply {
                    append("[")
                    append(idx + 1)
                    append("] ")
                    append(post.title)
                    append(" (")
                    append(post.pubDate.format(outputFormatter))
                    append(") - ")
                    append(post.link)
                },
            )
        }
        println()
    }
}

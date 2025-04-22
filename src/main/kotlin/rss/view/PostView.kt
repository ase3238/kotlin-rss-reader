package rss.view

import rss.entity.Post
import java.time.format.DateTimeFormatter

class PostView {
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun showKeywordMsg() {
        println("\n검색어를 입력하세요 (없으면 전체 출력):")
    }

    fun showPost(posts: List<Post>) {
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
    }

    fun showNewPostMsg() {
        println("\n새로운 글이 등록되었습니다!")
    }

    fun showNewPost(post: Post) {
        println(
            StringBuilder().apply {
                append("[NEW] ")
                append(post.title)
                append(" (")
                append(post.pubDate.format(outputFormatter))
                append(") - ")
                append(post.link)
            },
        )
    }
}

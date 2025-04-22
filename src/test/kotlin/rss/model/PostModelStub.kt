package rss.model

import rss.entity.Post
import java.time.LocalDateTime.now

class PostModelStub : PostPort {
    private var called = 0

    val rssResourceStub = listOf("source1", "source2")
    val postListStub =
        listOf(
            Post("title1", now().minusDays(5), "url1"),
            Post("title2", now().minusDays(3), "url2"),
            Post("title3", now(), "url3"),
        )

    override suspend fun getPostList(rssSources: List<String>): List<Post> {
        if (rssResourceStub == rssSources) {
            called++
            return postListStub.take(called)
        }
        return emptyList()
    }

    override fun filterPostList(
        postList: List<Post>,
        keyword: String,
    ) = postList.filter { it.title.contains(keyword) }

    override fun sortPostList(postList: List<Post>) = postList.sortedByDescending { it.pubDate }

    override fun cutPostList(
        postList: List<Post>,
        size: Int,
    ) = postList.take(size)
}

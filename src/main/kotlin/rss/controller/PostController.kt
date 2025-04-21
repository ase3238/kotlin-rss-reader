package rss.controller

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import rss.entity.Post
import rss.entity.RSS_SOURCE
import rss.model.PostModel
import rss.view.PostView

class PostController(
    val postModel: PostModel,
    val postView: PostView,
) {
    fun runReader() {
        runBlocking {
            val posts = async { postModel.getPostList(RSS_SOURCE) }
            postView.showKeywordMsg()
            val keyword = readln()
            val operated = operatedPosts(posts.await(), keyword)
            postView.showPost(operated)
        }
    }

    private fun operatedPosts(
        posts: List<Post>,
        keyword: String,
    ): List<Post> {
        val filtered = if (keyword != "") postModel.filterPostList(posts, keyword) else posts
        val sorted = postModel.sortPostList(filtered)
        return postModel.cutPostList(sorted, 10)
    }
}

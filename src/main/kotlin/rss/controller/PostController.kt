package rss.controller

import rss.entity.Post
import rss.entity.RSS_SOURCE
import rss.model.PostModel
import rss.view.PostView

class PostController(
    val postModel: PostModel,
    val postView: PostView,
) {
    fun runReader() {
        val posts = getSortedPosts(RSS_SOURCE)
    }

    private fun getSortedPosts(rssSource: List<String>): List<Post> {
        val list = postModel.getPostList(rssSource)
        return postModel.sortPostList(list)
    }
}

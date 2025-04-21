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
        val posts = getPosts(RSS_SOURCE)
        postView.showKeywordMsg()
        val operated = operatedPosts(posts, readln())
    }

    private fun getPosts(rssSource: List<String>): List<Post> {
        val list = postModel.getPostList(rssSource)
        return postModel.sortPostList(list)
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

package rss.controller

import rss.model.PostModel
import rss.view.PostView

class PostController(
    val postModel: PostModel,
    val postView: PostView,
) {
    fun initPosts() {
        val postList = postModel.initPostList()
        println(postList)
    }
}

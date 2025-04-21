package rss

import rss.controller.PostController
import rss.model.PostModel
import rss.view.PostView

fun main() {
    val model = PostModel()
    val view = PostView()
    val controller = PostController(model, view)
    controller.initPosts()
}

package rss

import kotlinx.coroutines.runBlocking
import rss.controller.PostController
import rss.model.PostModel
import rss.view.PostView

fun main() {
    runBlocking {
        val model = PostModel()
        val view = PostView()
        val controller = PostController(model, view)
        while (true) {
            controller.runReader()
        }
    }
}

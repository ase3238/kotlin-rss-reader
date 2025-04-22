package rss.controller

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import rss.model.PostModelStub
import rss.view.PostView

class PostControllerTest {
    @Test
    fun test_getPosts_can_update() =
        runTest {
            val model = PostModelStub()
            val view = PostView()
            val controller = PostController(model, view)
            val post1 = controller.getPosts(model.rssResourceStub, emptyList())
            val post2 = controller.getPosts(model.rssResourceStub, post1)
            val post3 = controller.getPosts(model.rssResourceStub, post2)
            (post2.size - post1.size) shouldBe 1
            (post3.size - post2.size) shouldBe 1
        }
}

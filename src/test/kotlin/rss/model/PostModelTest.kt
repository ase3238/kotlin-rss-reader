package rss.model

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import rss.entity.RSS_SOURCE
import rss.model.impl.PostModel

class PostModelTest : BehaviorSpec({

    Given("올바른 URL을 가지고 있을 때") {
        val sources = RSS_SOURCE
        val model = PostModel()
        When("getPostList를 호출하면") {
            val posts = model.getPostList(sources)
            Then("게시물이 있어한다") {
                posts.size shouldBeGreaterThan 0
            }
        }
    }

    Given("올바르지 않은 URL이 일부 있을 때") {
        val sources = listOf(RSS_SOURCE[0], "invalid_source", RSS_SOURCE[1])
        val model = PostModel()
        When("getPostList를 호출하면") {
            val posts = model.getPostList(sources)
            Then("게시물이 있어한다") {
                posts.size shouldBeGreaterThan 0
            }
        }
    }

    Given("올바르지 않은 URL만 있을 때") {
        val sources = listOf("invalid_source", "invalid_source_2")
        val model = PostModel()
        When("getPostList를 호출하면") {
            val posts = model.getPostList(sources)
            Then("게시물이 있어한다") {
                posts.size shouldBe 0
            }
        }
    }
})

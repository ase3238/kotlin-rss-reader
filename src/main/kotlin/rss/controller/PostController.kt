package rss.controller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import rss.entity.Post
import rss.entity.RSS_SOURCE
import rss.model.PostModel
import rss.view.PostView
import java.time.Duration

class PostController(
    val postModel: PostModel,
    val postView: PostView,
) {
    fun runReader() =
        runBlocking {
            var posts: List<Post> = emptyList()
            launch(Dispatchers.IO) {
                while (isActive) {
                    postView.showKeywordMsg()
                    val keyword = readln()
                    val operated = operatedPosts(posts, keyword)
                    postView.showPost(operated)
                }
            }
            launch {
                while (isActive) {
                    posts = getPosts(RSS_SOURCE, posts)
                    delay(Duration.ofMinutes(10).toMillis())
                }
            }
        }

    suspend fun getPosts(
        source: List<String>,
        oldPosts: List<Post>,
    ): List<Post> {
        val posts = postModel.getPostList(source)
        if (oldPosts.size > posts.size) {
            return oldPosts
        }
        val sortedPosts = postModel.sortPostList(posts)
        if (oldPosts.isNotEmpty() && oldPosts[0].pubDate != sortedPosts[0].pubDate) {
            postView.showNewPostMsg()
            for (newPost in sortedPosts) {
                if (newPost == oldPosts[0]) break
                postView.showNewPost(newPost)
            }
        }
        return posts
    }

    private fun operatedPosts(
        posts: List<Post>,
        keyword: String,
    ): List<Post> {
        val filtered = if (keyword != "") postModel.filterPostList(posts, keyword) else posts
        return postModel.cutPostList(filtered, 10)
    }
}

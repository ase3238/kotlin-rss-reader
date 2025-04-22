package rss.model

import rss.entity.Post

interface PostPort {
    suspend fun getPostList(rssSources: List<String>): List<Post>

    fun filterPostList(
        postList: List<Post>,
        keyword: String,
    ): List<Post>

    fun sortPostList(postList: List<Post>): List<Post>

    fun cutPostList(
        postList: List<Post>,
        size: Int,
    ): List<Post>
}

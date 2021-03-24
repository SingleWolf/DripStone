package com.walker.demo.paging3.model

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("html_url") val html_url: String,
    @SerializedName("stargazers_count") val starCount: Int
)
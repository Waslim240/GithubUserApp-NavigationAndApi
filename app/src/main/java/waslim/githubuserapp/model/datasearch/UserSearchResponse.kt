package waslim.githubuserapp.model.datasearch

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val userItemResponses: ArrayList<UserItemResponse>?,
    @SerializedName("total_count")
    val totalCount: Int
)
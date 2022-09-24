package waslim.githubuserapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import waslim.githubuserapp.BuildConfig
import waslim.githubuserapp.model.datasearch.UserItemResponse
import waslim.githubuserapp.model.datadetail.UserDetailsResponse
import waslim.githubuserapp.model.datafollow.UserFollowResponse
import waslim.githubuserapp.model.datasearch.UserSearchResponse

interface ApiServices {

    @GET("users")
    @Headers("Authorization: token $MY_TOKEN")
    fun getUser(): Call<ArrayList<UserItemResponse>>

    @GET("search/users")
    @Headers("Authorization: token $MY_TOKEN")
    fun searchUsersByUsername(
        @Query("q") query: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $MY_TOKEN")
    fun getDetailsUser(
        @Path("username") username: String
    ): Call<UserDetailsResponse>

    @GET("users/{username}/{type}")
    @Headers("Authorization: token $MY_TOKEN")
    fun getFollow(
        @Path("username") username: String,
        @Path("type") type: String
    ) : Call<ArrayList<UserFollowResponse>>

    companion object {
        private const val MY_TOKEN = BuildConfig.API_TOKEN
    }
}
package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import waslim.githubuserapp.model.datafollow.UserFollowResponse
import waslim.githubuserapp.network.ApiConfig

class ViewModelUserFollowing: ViewModel() {
    private val _dataFollowing = MutableLiveData<ArrayList<UserFollowResponse>?>()
    val dataFollowing: LiveData<ArrayList<UserFollowResponse>?> = _dataFollowing

    private val _isLoading = MutableLiveData<Boolean> ()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getFollowing(username: String) {
        _isLoading.value = true
        ApiConfig.instance.getFollow(username, TYPE)
            .enqueue(object : Callback<ArrayList<UserFollowResponse>>{
                override fun onResponse(
                    call: Call<ArrayList<UserFollowResponse>>,
                    response: Response<ArrayList<UserFollowResponse>>,
                ) {
                    when {
                        response.isSuccessful -> {
                            _isLoading.value = false
                            _dataFollowing.value = response.body()
                        }
                        else -> {
                            _isLoading.value = false
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserFollowResponse>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            })
    }

    companion object {
        private const val TAG = "ViewModelUserFollowing"
        private const val TYPE = "following"
    }
}
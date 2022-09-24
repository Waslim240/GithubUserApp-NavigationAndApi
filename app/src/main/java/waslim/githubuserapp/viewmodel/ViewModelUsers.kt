package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import waslim.githubuserapp.model.datasearch.UserItemResponse
import waslim.githubuserapp.model.datasearch.UserSearchResponse
import waslim.githubuserapp.network.ApiConfig

class ViewModelUsers : ViewModel() {
    private val _searchUser = MutableLiveData<ArrayList<UserItemResponse>?>()
    val searchUser: LiveData<ArrayList<UserItemResponse>?> = _searchUser

    private val _userData = MutableLiveData<ArrayList<UserItemResponse>?>()
    val userData: LiveData<ArrayList<UserItemResponse>?> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getUserData(){
        _isLoading.value = true
        ApiConfig.instance.getUser()
            .enqueue(object : Callback<ArrayList<UserItemResponse>>{
                override fun onResponse(
                    call: Call<ArrayList<UserItemResponse>>,
                    response: Response<ArrayList<UserItemResponse>>,
                ) {
                    when {
                        response.isSuccessful -> {
                            _isLoading.value = false
                            _userData.value = response.body()
                        }
                        else -> {
                            _isLoading.value = false
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserItemResponse>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            })
    }

    fun searchUserByUsername(username: String) {
        _isLoading.value = true
        ApiConfig.instance.searchUsersByUsername(username)
            .enqueue(object : Callback<UserSearchResponse> {
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>,
                ) {
                    when {
                        response.isSuccessful -> {
                            _isLoading.value = false
                            _searchUser.postValue(response.body()?.userItemResponses)
                        }
                        else -> {
                            _isLoading.value = false
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            })
    }

    companion object {
        private const val TAG = "ViewModelUser"
    }

}

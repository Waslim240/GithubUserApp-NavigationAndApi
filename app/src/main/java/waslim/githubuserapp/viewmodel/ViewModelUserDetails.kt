package waslim.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import waslim.githubuserapp.model.datadetail.UserDetailsResponse
import waslim.githubuserapp.network.ApiConfig

class ViewModelUserDetails: ViewModel(){
    private val _detailUser = MutableLiveData<UserDetailsResponse?>()
    val detailUser: LiveData<UserDetailsResponse?> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDataDetailsUser(username: String){
        _isLoading.value = true
        ApiConfig.instance.getDetailsUser(username)
            .enqueue(object : Callback<UserDetailsResponse>{
                override fun onResponse(
                    call: Call<UserDetailsResponse>,
                    response: Response<UserDetailsResponse>,
                ) {
                    when {
                        response.isSuccessful -> {
                            _isLoading.value = false
                            _detailUser.value = response.body()
                        }
                        else -> {
                            _isLoading.value = false
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UserDetailsResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }
            })
    }

    companion object {
        private const val TAG = "ViewModelUserDetails"
    }
}
package waslim.githubuserapp.view.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ActivityMainBinding
import waslim.githubuserapp.model.datasearch.UserItemResponse
import waslim.githubuserapp.view.detail.UserDetailsActivity
import waslim.githubuserapp.view.home.adapter.UserAdapter
import waslim.githubuserapp.viewmodel.ViewModelUsers

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExit = false
    private var username: String? = null
    private val viewModelUsers by viewModels<ViewModelUsers>()
    private val adapterList: UserAdapter by lazy {
        UserAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doubleBackExit()
        chekConnection()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.cari_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                viewModelUsers.searchUserByUsername(query)
                username = query
                viewModelUsers.searchUser.observe(this@MainActivity) {
                    if (it.isNullOrEmpty()) {
                        showLoading(false)
                        binding.apply {
                            rvUser.visibility = View.GONE
                            noData.visibility = View.VISIBLE
                        }
                        closedKeyboard()
                    } else {
                        setRecyclerList()
                        adapterList.setUserDataList(it)
                        showLoading(false)
                        closedKeyboard()
                    }
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showLoading(isLoading: Boolean) = viewModelUsers.isLoading.observe(this) {
        binding.progressBarMain.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun chekConnection() {
        if (isOnline(applicationContext)) setUserData() else Toast.makeText(applicationContext, getString(R.string.no_internet), Toast.LENGTH_LONG).show()
    }

    private fun setUserData() {
        if (username != null) {
            viewModelUsers.searchUser.observe(this) {
                if (it != null) {
                    showLoading(false)
                    setRecyclerList()
                    adapterList.setUserDataList(it)
                } else {
                    showLoading(false)
                    Toast.makeText(applicationContext, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
                }
            }
            viewModelUsers.searchUserByUsername(username.toString())
        } else {
            viewModelUsers.userData.observe(this) {
                if (it != null) {
                    showLoading(false)
                    setRecyclerList()
                    adapterList.setUserDataList(it)
                } else {
                    showLoading(false)
                    Toast.makeText(applicationContext, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
                }
            }
            viewModelUsers.getUserData()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                true
            } else capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else false
    }

    private fun setRecyclerList() {
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(applicationContext)
            rvUser.adapter = adapterList
            noData.visibility = View.GONE
            rvUser.visibility = View.VISIBLE
            rvUser.setHasFixedSize(true)
        }

        adapterList.run {
            setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
                override fun onItemClickedMoveDetail(dataUsers: UserItemResponse) {
                    moveWhiteGithubUserData(dataUsers)
                    closedKeyboard()
                }

                override fun onItemClickedShare(dataUsers: UserItemResponse) {
                    shareUserData(dataUsers)
                    closedKeyboard()
                }
            })
        }
    }

    private fun moveWhiteGithubUserData(dataUsers: UserItemResponse) {
        Intent(applicationContext, UserDetailsActivity::class.java).apply {
            putExtra(UserDetailsActivity.DATA_USER, dataUsers)
            startActivity(this)
        }
    }

    private fun shareUserData(dataUsers: UserItemResponse) {
        val shareUserData = "Username: ${dataUsers.login}\n" +
                "Link Github: ${dataUsers.htmlUrl}"
        val share: Intent = Intent()
            .apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareUserData)
                type = "text/plain"
            }
        Intent.createChooser(share, "Share Data Using").apply {
            startActivity(this)
        }
    }

    private fun closedKeyboard() {
        val view: View? = currentFocus
        val inputMethodManager: InputMethodManager
        if (view != null) {
            inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun doubleBackExit() {
        onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (doubleBackToExit) {
                        finish()
                    } else {
                        doubleBackToExit = true
                        Toast.makeText(applicationContext, "Tekan lagi untuk keluar", Toast.LENGTH_SHORT).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            kotlin.run {
                                doubleBackToExit = false
                            }
                        }, 2000L)
                    }
                }
            })
    }

}
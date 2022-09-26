package waslim.githubuserapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ActivityDetailsUserBinding
import waslim.githubuserapp.model.datasearch.UserItemResponse
import waslim.githubuserapp.viewmodel.ViewModelUserDetails

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsUserBinding
    private val viewModelUserDetails by viewModels<ViewModelUserDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataUser = intent.getParcelableExtra<UserItemResponse>(DATA_USER) as UserItemResponse
        when {
            dataUser.login != null -> setDataDetailsUser(dataUser.login)
        }
        showLoading(true)
        showSectionPager()
    }

    private fun setDataDetailsUser(username: String) {
        viewModelUserDetails.detailUser.observe(this){ UserDetailsResponse ->
            binding.apply {
                when {
                    UserDetailsResponse != null -> {
                        when {
                            UserDetailsResponse.name.isNullOrEmpty() -> {
                                tvNameDetails.text = getString(R.string.no_name)
                            }
                            UserDetailsResponse.location.isNullOrEmpty() -> {
                                tvLocationDetail.text = getString(R.string.no_location)
                            }
                            UserDetailsResponse.login.isNullOrEmpty() -> {
                                tvUsernameDetail.text = getString(R.string.no_username)
                            }
                            UserDetailsResponse.company.isNullOrEmpty() -> {
                                tvCompanyDetail.text = getString(R.string.no_company)
                            }
                            UserDetailsResponse.htmlUrl.isNullOrEmpty() -> {
                                tvLinkDetail.text = getString(R.string.no_link)
                            }
                            UserDetailsResponse.email.isNullOrEmpty() -> {
                                tvEmailDetail.text = getString(R.string.no_email)
                            }
                            else -> {
                                tvNameDetails.text = UserDetailsResponse.name
                                tvLocationDetail.text = UserDetailsResponse.location
                                tvCompanyDetail.text = UserDetailsResponse.company
                                tvLinkDetail.text = UserDetailsResponse.htmlUrl
                                tvEmailDetail.text = UserDetailsResponse.email
                            }
                        }

                        Glide.with(applicationContext)
                            .load(UserDetailsResponse.avatarUrl)
                            .error(R.drawable.ic_baseline_error_24)
                            .into(ivAvatarsUserDetail)
                        setVisibleIcon()
                    }
                }
            }
            showLoading(false)
        }
        viewModelUserDetails.getDataDetailsUser(username)
    }

    private fun setVisibleIcon() {
        binding.apply {
            imageView1.visibility = View.VISIBLE
            imageView2.visibility = View.VISIBLE
            imageView3.visibility = View.VISIBLE
            imageView4.visibility = View.VISIBLE
            imageView5.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) = viewModelUserDetails.isLoading.observe(this){
        binding.progressBarDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    
    private fun showSectionPager() {
        binding.apply {
            val sectionPager = SectionsPagerFollow(this@UserDetailsActivity)
            val viewPager: ViewPager2 = viewPager
            viewPager.adapter = sectionPager
            val tabs: TabLayout = tabs
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }
    }

    companion object {
        const val DATA_USER = "data_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
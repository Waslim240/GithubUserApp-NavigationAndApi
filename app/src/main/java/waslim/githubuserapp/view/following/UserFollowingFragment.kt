package waslim.githubuserapp.view.following

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import waslim.githubuserapp.databinding.FragmentFollowingBinding
import waslim.githubuserapp.model.datafollow.UserFollowResponse
import waslim.githubuserapp.model.datasearch.UserItemResponse
import waslim.githubuserapp.view.following.adapter.UserFollowingAdapter
import waslim.githubuserapp.viewmodel.ViewModelUserFollowing

class UserFollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val viewModelUserFollowing by viewModels<ViewModelUserFollowing>()
    private val userFollowingAdapter: UserFollowingAdapter by lazy {
        UserFollowingAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataUser = activity?.intent?.getParcelableExtra<UserItemResponse>(DATA_USER) as UserItemResponse
        when {
            dataUser.login != null -> showRecyclerList(dataUser.login)
        }
    }

    private fun showRecyclerList(username: String) {
        showLoading(true)
        viewModelUserFollowing.dataFollowing.observe(viewLifecycleOwner) {
            if (it != null) {
                userFollowingAdapter.setUserDataFollowing(it)
                setRecyclerList()
                showLoading(false)
            }
        }
        viewModelUserFollowing.getFollowing(username)
    }

    private fun setRecyclerList() {
        binding.apply {
            rvFollowing.layoutManager = LinearLayoutManager(requireContext())
            rvFollowing.adapter = userFollowingAdapter
            rvFollowing.setHasFixedSize(true)
        }

        userFollowingAdapter.setOnItemClickCallback(object : UserFollowingAdapter.OnItemClickCallback{
            override fun onItemClickedShare(dataUsers: UserFollowResponse) {
                shareUserData(dataUsers)
            }

        })
    }

    private fun shareUserData(dataUsers: UserFollowResponse) {
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

    private fun showLoading(isLoading: Boolean) = viewModelUserFollowing.isLoading.observe(viewLifecycleOwner){
        binding.progressBarFollowing.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DATA_USER = "data_user"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
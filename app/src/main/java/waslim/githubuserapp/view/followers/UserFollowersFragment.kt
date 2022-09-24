package waslim.githubuserapp.view.followers

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import waslim.githubuserapp.databinding.FragmentFollowersBinding
import waslim.githubuserapp.model.datafollow.UserFollowResponse
import waslim.githubuserapp.model.datasearch.UserItemResponse
import waslim.githubuserapp.view.followers.adapter.UserFollowersAdapter
import waslim.githubuserapp.viewmodel.ViewModelUserFollowers

class UserFollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private val viewModelUserFollowers by viewModels<ViewModelUserFollowers>()
    private val userFollowersAdapter: UserFollowersAdapter by lazy {
        UserFollowersAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
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
        viewModelUserFollowers.dataFollowers.observe(viewLifecycleOwner) {
            if (it != null) {
                userFollowersAdapter.setUserDataFollowers(it)
                setRecyclerList()
                showLoading(false)
            }
        }
        viewModelUserFollowers.getFollowers(username)
    }

    private fun setRecyclerList() {
        binding.apply {
            rvFollowers.layoutManager = LinearLayoutManager(requireContext())
            rvFollowers.adapter = userFollowersAdapter
            rvFollowers.setHasFixedSize(true)
        }

        userFollowersAdapter.setOnItemClickCallback(object : UserFollowersAdapter.OnItemClickCallback{
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

    private fun showLoading(isLoading: Boolean) = viewModelUserFollowers.isLoading.observe(viewLifecycleOwner){
        binding.progressBarFollowers.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val DATA_USER = "data_user"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
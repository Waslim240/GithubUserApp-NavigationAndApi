package waslim.githubuserapp.view.followers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ItemListUsersBinding
import waslim.githubuserapp.model.datafollow.UserFollowResponse

class UserFollowersAdapter : RecyclerView.Adapter<UserFollowersAdapter.ViewHolder>() {
    private lateinit var onItemClickShareUserData: OnItemClickCallback
    private val listFollowers = ArrayList<UserFollowResponse>()

    fun setOnItemClickCallback(onItemClick: OnItemClickCallback) {
        this.onItemClickShareUserData = onItemClick
    }

    fun setUserDataFollowers(items: ArrayList<UserFollowResponse>) {
        listFollowers.clear()
        listFollowers.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listFollowers[position]
        holder.apply {
            binding.apply {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(ivAvatarsList)
                tvUsernameList.text = user.login
                tvLinkList.text = user.htmlUrl
                btnShareUserData.setOnClickListener {
                    onItemClickShareUserData.onItemClickedShare(listFollowers[adapterPosition])
                }
            }
        }
    }

    override fun getItemCount() = listFollowers.size

    class ViewHolder (var binding: ItemListUsersBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClickedShare(dataUsers: UserFollowResponse)
    }

}
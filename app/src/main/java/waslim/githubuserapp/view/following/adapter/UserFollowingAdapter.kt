package waslim.githubuserapp.view.following.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ItemListUsersBinding
import waslim.githubuserapp.model.datafollow.UserFollowResponse

class UserFollowingAdapter : RecyclerView.Adapter<UserFollowingAdapter.ViewHolder>() {
    private lateinit var onItemClickShareUserData: OnItemClickCallback
    private val listFollowing = ArrayList<UserFollowResponse>()

    fun setOnItemClickCallback(onItemClick: OnItemClickCallback) {
        this.onItemClickShareUserData = onItemClick
    }

    fun setUserDataFollowing(items: ArrayList<UserFollowResponse>) {
        listFollowing.clear()
        listFollowing.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listFollowing[position]
        holder.apply {
            binding.apply {
                Glide.with(holder.itemView.context)
                    .load(user.avatarUrl)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(ivAvatarsList)
                tvUsernameList.text = user.login
                tvLinkList.text = user.htmlUrl
                btnShareUserData.setOnClickListener {
                    onItemClickShareUserData.onItemClickedShare(listFollowing[adapterPosition])
                }
            }
        }
    }

    override fun getItemCount() = listFollowing.size

    class ViewHolder (var binding: ItemListUsersBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClickedShare(dataUsers: UserFollowResponse)
    }

}
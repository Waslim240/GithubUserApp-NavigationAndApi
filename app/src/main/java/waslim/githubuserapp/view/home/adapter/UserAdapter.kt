package waslim.githubuserapp.view.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import waslim.githubuserapp.R
import waslim.githubuserapp.databinding.ItemListUsersBinding
import waslim.githubuserapp.model.datasearch.UserItemResponse

class UserAdapter : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickDetails: OnItemClickCallback
    private lateinit var onItemClickShareUserData: OnItemClickCallback
    private val listUsers = ArrayList<UserItemResponse>()

    fun setOnItemClickCallback(onItemClick: OnItemClickCallback) {
        this.onItemClickDetails = onItemClick
        this.onItemClickShareUserData = onItemClick
    }

    fun setUserDataList(userItemResponses: ArrayList<UserItemResponse>) {
        listUsers.clear()
        listUsers.addAll(userItemResponses)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUsers[position]
        holder.apply {
            binding.apply {
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(ivAvatarsList)
                tvUsernameList.text = user.login
                tvLinkList.text = user.htmlUrl
                btnShareUserData.setOnClickListener {
                    onItemClickShareUserData.onItemClickedShare(listUsers[adapterPosition])
                }
            }

            itemView.setOnClickListener {
                onItemClickDetails.onItemClickedMoveDetail(listUsers[adapterPosition])
            }
        }
    }

    override fun getItemCount() = listUsers.size

    class ViewHolder (var binding: ItemListUsersBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClickedMoveDetail(dataUsers: UserItemResponse)
        fun onItemClickedShare(dataUsers: UserItemResponse)
    }

}
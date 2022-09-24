package waslim.githubuserapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import waslim.githubuserapp.view.followers.UserFollowersFragment
import waslim.githubuserapp.view.following.UserFollowingFragment

class SectionsPagerFollow(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = UserFollowersFragment()
            1 -> fragment = UserFollowingFragment()
        }
        return fragment as Fragment
    }

}
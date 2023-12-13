package com.dimashn.dimashub.ui.detail.following

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimashn.R
import com.dimashn.databinding.FragmentFollowBinding
import com.dimashn.dimashub.ui.detail.DetailUserActivity
import com.dimashn.dimashub.ui.main.UserAdapter


class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FollowingViewModel by activityViewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username = arguments?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter()
        setupViews()
        setupViewModel()
    }

    private fun setupViews() {
        binding.apply {
            rvFollow.setHasFixedSize(true)
            rvFollow.layoutManager = LinearLayoutManager(requireContext())
            rvFollow.adapter = adapter
        }

        showLoading(true)
    }

    private fun setupViewModel() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.fetchFollowing(username)
        viewModel.following.observe(viewLifecycleOwner) { followingList ->
            if (followingList != null) {
                adapter.updateList(followingList)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

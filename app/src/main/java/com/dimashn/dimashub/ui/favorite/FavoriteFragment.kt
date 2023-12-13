package com.dimashn.dimashub.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimashn.databinding.FragmentFavoriteBinding
import com.dimashn.dimashub.data.local.FavoriteEntity
import com.dimashn.dimashub.data.remote.User
import com.dimashn.dimashub.ui.detail.DetailUserActivity
import com.dimashn.dimashub.ui.main.UserAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by activityViewModels()
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAdapter = UserAdapter()
        binding.rvFavorite.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = userAdapter
        }

        setupItemClickCallback()
        setupViewModel()
    }

    private fun setupItemClickCallback() {
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(requireContext(), DetailUserActivity::class.java).apply {
                    putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    putExtra(DetailUserActivity.EXTRA_HTML, data.htmlUrl)
                }
                startActivity(intent)
            }
        })
    }

    private fun setupViewModel() {
        favoriteViewModel.getFavoriteUser().observe(viewLifecycleOwner) { users ->
            users?.let {
                val list = mapList(it)
                userAdapter.updateList(list)
            }
        }
    }

    private fun mapList(users: List<FavoriteEntity>): List<User> {
        return users.map {
            User(
                it.id,
                it.login,
                it.avatarUrl,
                it.htmlUrl
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}



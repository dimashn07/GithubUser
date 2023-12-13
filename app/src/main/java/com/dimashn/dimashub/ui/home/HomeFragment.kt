package com.dimashn.dimashub.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimashn.databinding.FragmentHomeBinding
import com.dimashn.dimashub.data.remote.User
import com.dimashn.dimashub.ui.detail.DetailUserActivity
import com.dimashn.dimashub.ui.main.UserAdapter
import com.dimashn.dimashub.ui.setting.SettingViewModelFactory
import com.dimashn.dimashub.ui.setting.SettingsPreferences

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userAdapter: UserAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupUserList()
        fetchUsersFromGitHub()
    }

    private fun setupAdapter() {
        userAdapter = UserAdapter()
    }

    private fun setupUserList() {
        setupAdapter()
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(requireContext(), DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_HTML, data.htmlUrl)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvSearchUser.layoutManager = LinearLayoutManager(requireContext())
            rvSearchUser.setHasFixedSize(true)
            rvSearchUser.adapter = userAdapter
        }

        setupViewModel()
    }

    private fun setupViewModel() {
        val pref = SettingsPreferences.getInstance(requireContext().dataStore)
        homeViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[HomeViewModel::class.java]

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.listUsers.observe(viewLifecycleOwner) {
            if (it != null) {
                userAdapter.updateList(it)
                showLoading(false)
            }
        }

        homeViewModel.getThemeSetting().observe(viewLifecycleOwner) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun fetchUsersFromGitHub() {
        homeViewModel.getGitHubUsers().observe(viewLifecycleOwner) { users: List<User> ->
            if (users.isNotEmpty()) {
                userAdapter.updateList(users)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

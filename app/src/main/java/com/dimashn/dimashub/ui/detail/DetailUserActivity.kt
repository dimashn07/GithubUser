package com.dimashn.dimashub.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dimashn.R
import com.dimashn.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var sectionPagerAdapter: SectionPagerAdapter
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_user)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR).toString()
        val htmlUrl = intent.getStringExtra(EXTRA_HTML).toString()

        val bundle = Bundle().apply {
            putString(EXTRA_USERNAME, username)
            putInt(EXTRA_ID, id)
        }

        showLoading(true)

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        viewModel.isLoading.observe(this) { showLoading(it) }

        viewModel.setDetailUser(username)
        viewModel.user.observe(this) { user ->
            user?.let {
                with(binding) {
                    tvUsername.text = it.login
                    tvName.text = it.name
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvDetailRepository.text = it.public_repos.toString()
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(ivProfile)
                }
                showLoading(false)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                isFavorite = (count ?: 0) > 0
                binding.btnFavorite.isChecked = isFavorite
            }
        }

        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                viewModel.addToFavorite(avatar, htmlUrl, id, username)
            } else {
                viewModel.removeFromFavorite(id)
            }
            binding.btnFavorite.isChecked = isFavorite
        }

        sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        with(binding) {
            viewPagerFollow.adapter = sectionPagerAdapter
            TabLayoutMediator(tabsLayoutFollow, viewPagerFollow) { tabs, position ->
                tabs.text = tabTitle[position]
            }.attach()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
        const val EXTRA_ID = "EXTRA_ID"
        const val EXTRA_AVATAR = "EXTRA_AVATAR"
        const val EXTRA_HTML  = "EXTRA_HTML"

        private val tabTitle = listOf("Followers", "Following")
    }
}


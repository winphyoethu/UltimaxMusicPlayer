package com.winphyoethu.ultimaxmusic.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.winphyoethu.ultimaxmusic.R
import com.winphyoethu.ultimaxmusic.databinding.ActivityMainBinding
import com.winphyoethu.ultimaxmusic.player.MusicPlayerService
import com.winphyoethu.ultimaxmusic.ui.base.BaseActivity
import com.winphyoethu.ultimaxmusic.ui.home.HomeFragment
import com.winphyoethu.ultimaxmusic.ui.home.HomeViewModel
import com.winphyoethu.ultimaxmusic.util.Constants
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var preferenceHelper:PreferenceHelper

    private lateinit var binding: ActivityMainBinding

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serviceIntent = Intent(this, MusicPlayerService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        homeViewModel.getLatestSongList()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!preferenceHelper.getBoolean(Constants.SP_CURRENT_PLAYING, false)) {
            preferenceHelper.apply {
                remove(Constants.SP_PLAYLIST_NAME)
                remove(Constants.SP_PLAYLIST_ID)
                remove(Constants.SP_CURRENT_SONG)
                remove(Constants.SP_CURRENT_SONG_ID)
                remove(Constants.SP_IS_PLAYLIST)
                remove(Constants.SP_CURRENT_PLAYING)
                remove(Constants.SP_CURRENT_POSITION)
                remove(Constants.SP_LAST_POSITION)
            }
        }
    }
}
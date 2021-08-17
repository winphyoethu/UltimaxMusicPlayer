package com.winphyoethu.ultimaxmusic.ui.miniplayer

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.winphyoethu.ultimaxmusic.R
import com.winphyoethu.ultimaxmusic.databinding.FragmentMiniPlayerBinding
import com.winphyoethu.ultimaxmusic.player.MusicPlayerService
import com.winphyoethu.ultimaxmusic.ui.home.HomeViewModel
import com.winphyoethu.ultimaxmusic.util.Constants
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_ACTION
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_ACTION_NAME
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_NEXT
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_PAUSE
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_PREVIOUS
import com.winphyoethu.ultimaxmusic.util.Constants.BROADCAST_MUSIC_RESUME
import com.winphyoethu.ultimaxmusic.util.sharedpreference.PreferenceHelper
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class MiniPlayerFragment : Fragment(), ServiceConnection {

    private var musicPlayerService: MusicPlayerService? = null
    private lateinit var serviceIntent: Intent
    private var isBound = false

    private var _binding: FragmentMiniPlayerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var pHelper: PreferenceHelper

    private val musicReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(BROADCAST_MUSIC_ACTION_NAME)) {
                BROADCAST_MUSIC_RESUME -> {
                    binding.ibPlayPause.setImageResource(R.drawable.ic_pause)
                    homeViewModel.updatePlayPause()
                }
                BROADCAST_MUSIC_PAUSE -> {
                    binding.ibPlayPause.setImageResource(R.drawable.ic_play)
                    homeViewModel.updatePlayPause()
                }
                BROADCAST_MUSIC_NEXT -> {
                    homeViewModel.updateSongPosition(false)
                }
                BROADCAST_MUSIC_PREVIOUS -> {
                    homeViewModel.updateSongPosition(true)
                }
                else -> {
                    homeViewModel.updatePlayPause()
                    unbindService()
                    bindService()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindService()
    }

    fun bindService() {
        if (!isBound) {
            serviceIntent = Intent(requireContext(), MusicPlayerService::class.java)
            requireContext().bindService(serviceIntent, this, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindService() {
        requireContext().unbindService(this)
        isBound = false
        musicPlayerService = null
    }

    override fun onResume() {
        super.onResume()

        if (pHelper.getBoolean(Constants.SP_CURRENT_PLAYING, false)) {
            binding.ibPlayPause.setImageResource(R.drawable.ic_pause)
        } else {
            binding.ibPlayPause.setImageResource(R.drawable.ic_play)
        }

        if (pHelper.getString(Constants.SP_CURRENT_SONG, "").isNullOrEmpty()) {
            binding.tvCurrentSong.text = ""
        } else {
            binding.tvCurrentSong.text = pHelper.getString(Constants.SP_CURRENT_SONG, "")
        }

        if (pHelper.getString(Constants.SP_PLAYLIST_NAME, "").isNullOrEmpty()) {
            binding.tvCurrentPlaylist.text = ""
        } else {
            binding.tvCurrentPlaylist.text = pHelper.getString(Constants.SP_PLAYLIST_NAME, "")
        }

        requireContext().registerReceiver(musicReceiver, IntentFilter(BROADCAST_MUSIC_ACTION))

        Log.i("MINIFRAG :: ", "ONRESUME")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        unbindService()

        requireContext().unregisterReceiver(musicReceiver)
        Log.i("MINIFRAG :: ", "ONDESTROY")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMiniPlayerBinding.inflate(inflater, container, false)

        Log.i("MINIFRAG :: ", "ONCREATEVIEW")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("MINIFRAG :: ", "ONVIEWCREATED")
        binding.ibPlayPause.setOnClickListener {
//            if (::musicPlayerService.isInitialized)
//                musicPlayerService.resumePauseSong()
            homeViewModel.playPauseSong()
        }

        binding.ibSkipNext.setOnClickListener {
//            if (::musicPlayerService.isInitialized)
//                musicPlayerService.skipNextSong()
            homeViewModel.skipPreviousNext(false)
        }

        binding.ibSkipPrevious.setOnClickListener {
//            if (::musicPlayerService.isInitialized)
//                musicPlayerService.skipPreviousSong()
            homeViewModel.skipPreviousNext(true)
        }

        compositeDisposable.add(
            homeViewModel.miniPlayerStatePublisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is MiniPlayerPlaySong -> {
                            musicPlayerService!!.playSong(it.position)
                            updateMetaData()
                            binding.ibPlayPause.setImageResource(R.drawable.ic_pause)
                        }
                        is MiniPlayerResumeSong -> {
                            musicPlayerService?.resumePauseSong()
                            updateMetaData()
                            binding.ibPlayPause.setImageResource(R.drawable.ic_pause)
                        }
                        is MiniPlayerPauseSong -> {
                            musicPlayerService?.resumePauseSong()
                            binding.ibPlayPause.setImageResource(R.drawable.ic_play)
                        }
                        is MiniPlayerAddSong -> {
                            musicPlayerService?.addSongs(it.songList)
                        }
                    }
                }, {
                    Log.i("ULTIMATE :: ", it.message.toString())
                })
        )
    }

    private fun updateMetaData() {
        val song = musicPlayerService?.getCurrentSong()

        binding.tvCurrentSong.text = song?.title
        binding.tvCurrentSong.isSelected = true

        val playlistTitle = pHelper.getString(Constants.SP_PLAYLIST_NAME, "")
        if (playlistTitle!!.isNotEmpty()) {
            binding.tvCurrentPlaylist.text = playlistTitle
        } else {
            binding.tvCurrentPlaylist.text = ""
        }
    }

    override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
        musicPlayerService = (binder as MusicPlayerService.MusicBinder).service
        isBound = true
        Log.i("flark :: ", "INIITIAZLED")
        Log.i("MUSICPLAYER :: ", musicPlayerService.hashCode().toString())
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicPlayerService = null
        isBound = false
    }
}
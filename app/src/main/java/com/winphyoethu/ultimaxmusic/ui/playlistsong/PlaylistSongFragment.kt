package com.winphyoethu.ultimaxmusic.ui.playlistsong

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.winphyoethu.ultimaxmusic.databinding.FragmentPlaylistSongBinding
import com.winphyoethu.ultimaxmusic.player.MusicPlayerService
import com.winphyoethu.ultimaxmusic.ui.home.*
import com.winphyoethu.ultimaxmusic.util.dialog.DialogUtil
import com.winphyoethu.ultimaxmusic.util.gone
import com.winphyoethu.ultimaxmusic.util.visible
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistSongFragment : Fragment() {

    @Inject
    lateinit var dialogUtil: DialogUtil

    private var playlistId: Int = -1
    private var playlistTitle: String = ""

    private val playlistSongViewModel: HomeViewModel by activityViewModels()

    private val compositeDisposable = CompositeDisposable()

    private lateinit var _binding: FragmentPlaylistSongBinding

    private val binding
        get() = _binding

    private val args: PlaylistSongFragmentArgs by navArgs()

    private val songAdapter: SongAdapter by lazy {
        SongAdapter({
            playlistSongViewModel.playSelectedPlaylistSong(it)
        }, {
            dialogUtil.showMessageDialog(requireActivity(), positiveClicked = {
                playlistSongViewModel.deleteSongFromPlaylist(it.id, playlistId)
            }, negativeClicked = {

            })
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("PLAYLISTSONGFRAG :: ", "ONCREATE")
    }

    override fun onResume() {
        super.onResume()
        Log.i("PLAYLISTSONGFRAG :: ", "ONRESUME")

        playlistSongViewModel.getPlaylistSong(playlistId)
    }

    override fun onStart() {
        super.onStart()
        Log.i("PLAYLISTSONGFRAG :: ", "ONSTART")
    }

    override fun onPause() {
        super.onPause()

        playlistSongViewModel.disposePlaylistSong()
        Log.i("PLAYLISTSONGFRAG :: ", "ONPAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.i("PLAYLISTSONGFRAG :: ", "ONSTOP")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("PLAYLISTSONGFRAG :: ", "ONATTACH")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("PLAYLISTSONGFRAG :: ", "ONDETACH")
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
        Log.i("PLAYLISTSONGFRAG :: ", "ONDESTROY")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("PLAYLISTSONGFRAG :: ", "ONDESTROYVIEW")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistSongBinding.inflate(inflater, container, false)
        Log.i("PLAYLISTSONGFRAG :: ", "ONCREATEVIEW")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("PLAYLISTSONGFRAG :: ", "ONVIEWCREATED")
        playlistId = args.playlistId
        playlistTitle = args.playlistTitle

        binding.tvPlaylistTitle.text = playlistTitle

        binding.rvPlaylistSong.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = songAdapter
        }

        compositeDisposable.add(
            playlistSongViewModel.songStatePublisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is Error -> {

                        }
                        is ShowPlaylistSong -> {
                            binding.tvPlaylistSongEmpty.gone()
                            songAdapter.submitList(it.songList.toList())
                        }
                        is EmptySong -> {
                            binding.tvPlaylistSongEmpty.visible()
                        }
                        is ShowSongDeleted -> {
                            Toast.makeText(requireContext(), "Song Deleted", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }, {

                })
        )
    }

}
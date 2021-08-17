package com.winphyoethu.ultimaxmusic.ui.playlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.winphyoethu.ultimaxmusic.R
import com.winphyoethu.ultimaxmusic.databinding.FragmentPlaylistBinding
import com.winphyoethu.ultimaxmusic.util.gone
import com.winphyoethu.ultimaxmusic.util.makeToast
import com.winphyoethu.ultimaxmusic.util.rxview.ViewHelper
import com.winphyoethu.ultimaxmusic.util.visible
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModels()
    private var _binding: FragmentPlaylistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()

    private val playlistAdapter: PlaylistAdapter by lazy {
        PlaylistAdapter {
            val action =
                PlaylistFragmentDirections.actionNavigationDashboardToNavigationPlaylistSong(
                    playlistId = it.playlist.id, playlistTitle = it.playlist.playlistTitle
                )
            findNavController().navigate(action)
        }
    }

    @Inject
    lateinit var viewHelper: ViewHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("PLAYLISTFRAG :: ", "ONCTEATE")
    }

    override fun onResume() {
        super.onResume()
        Log.i("PLAYLISTFRAG :: ", "ONRESUME")
    }

    override fun onStart() {
        super.onStart()
        Log.i("PLAYLISTFRAG :: ", "ONSTART")
    }

    override fun onPause() {
        super.onPause()
        Log.i("PLAYLISTFRAG :: ", "ONPAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.i("PLAYLISTFRAG :: ", "ONSTOP")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("PLAYLISTFRAG :: ", "ONDESTROY")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("PLAYLISTFRAG :: ", "ONATTACH")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("PLAYLISTFRAG :: ", "ONDETACH")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.i("PLAYLISTFRAG :: ", "ONCREATEVIEW")
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("PLAYLISTFRAG :: ", "ONVIEWCREATED")
        playlistViewModel.getPlaylist()

        compositeDisposable.add(
            viewHelper.bindButton(binding.btnCreatePlaylist)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe({
                    findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_create_playlist_dialog)
                }, {

                })
        )

        binding.rvPlaylist.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = playlistAdapter
        }

        compositeDisposable.add(
            playlistViewModel.playlistPublisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is ShowPlaylist -> {
                            binding.tvEmptyPlaylist.gone()
                            playlistAdapter.submitList(it.playlist.toList())
                        }
                        is PlaylistDeleted -> {
                            requireContext().makeToast("Playlist is deleted", Toast.LENGTH_SHORT)
                        }
                        is EmptyPlaylist -> {
                            binding.tvEmptyPlaylist.visible()
                        }
                        is PlaylistError -> {

                        }
                    }
                }, {

                })
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        compositeDisposable.clear()

        Log.i("PLAYLISTFRAG :: ", "ONDESTROYVIEW")
    }

}
package com.winphyoethu.ultimaxmusic.ui.playlistdialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.winphyoethu.ultimaxmusic.R
import com.winphyoethu.ultimaxmusic.databinding.FragmentPlaylistDialogBinding
import com.winphyoethu.ultimaxmusic.util.makeToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class PlaylistDialogFragment : DialogFragment() {

    private val args: PlaylistDialogFragmentArgs by navArgs()

    private var songId: Int = -1

    private val playlistDialogViewModel: PlaylistDialogViewModel by viewModels()

    private lateinit var binding: FragmentPlaylistDialogBinding

    private val compositeDisposable = CompositeDisposable()

    private val playlistDialogAdapter: PlaylistDialogAdapter by lazy {
        PlaylistDialogAdapter {
            playlistDialogViewModel.addSongToPlaylist(songId, it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentPlaylistDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val dialogBuilder = AlertDialog.Builder(requireActivity())
            .setTitle("Choose Playlist")
            .setView(binding.root)

        return dialogBuilder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        songId = args.songId

        playlistDialogViewModel.getPlaylist()

        binding.rvPlaylist.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = playlistDialogAdapter
        }

        compositeDisposable.add(
            playlistDialogViewModel.playlistPublisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is ShowPlaylist -> {
                            playlistDialogAdapter.submitList(it.playlist.toList())
                        }
                        is ShowSongInserted -> {
                            requireContext().makeToast("Song added to playlist", Toast.LENGTH_SHORT)
                            dismiss()
                        }
                    }
                }, {
                    Log.i("PDIALOG :: ", it.toString())
                })
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}
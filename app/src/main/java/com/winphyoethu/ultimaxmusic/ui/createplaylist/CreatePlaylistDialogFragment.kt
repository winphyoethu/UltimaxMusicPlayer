package com.winphyoethu.ultimaxmusic.ui.createplaylist

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.winphyoethu.ultimaxmusic.databinding.FragmentCreatePlaylistDialogBinding
import com.winphyoethu.ultimaxmusic.util.rxview.ViewHelper
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class CreatePlaylistDialogFragment : DialogFragment() {

    private lateinit var _binding: FragmentCreatePlaylistDialogBinding

    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModels()

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var viewHelper: ViewHelper

    private val binding
        get() = _binding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding =
            FragmentCreatePlaylistDialogBinding.inflate(LayoutInflater.from(requireContext()))

        val dialogBuilder = AlertDialog.Builder(requireActivity())
            .setTitle("Create Playlist")
            .setView(binding.root)

        return dialogBuilder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        compositeDisposable.add(
            viewHelper.bindEditText(binding.etPlaylistName)
                .map {
                    it.isEmpty()
                }
                .subscribe({
                    if (it) {
                        binding.btnCreatePlaylist.isEnabled = false
                        binding.tipPlaylistName.error = "Playlist Title must not be empty"
                    } else {
                        binding.btnCreatePlaylist.isEnabled = true
                        binding.tipPlaylistName.error = ""
                    }
                }, {

                })
        )

        compositeDisposable.add(
            viewHelper.bindButton(binding.btnCancelCreate)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe({
                    dismiss()
                }, {

                })
        )

        compositeDisposable.add(
            viewHelper.bindButton(binding.btnCreatePlaylist)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe({
                    dismiss()
                    createPlaylistViewModel.createPlaylist(binding.etPlaylistName.text.toString())
                }, {

                })
        )
    }

}
package com.winphyoethu.ultimaxmusic.ui.home

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbruyelle.rxpermissions3.RxPermissions
import com.winphyoethu.ultimaxmusic.databinding.FragmentHomeBinding
import com.winphyoethu.ultimaxmusic.player.MusicPlayerService
import com.winphyoethu.ultimaxmusic.util.gone
import com.winphyoethu.ultimaxmusic.util.visible
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val songAdapter: SongAdapter by lazy {
        SongAdapter({
            homeViewModel.playSelectedSong(it)
        }, {
            val action = HomeFragmentDirections.actionHomeToPlaylistDialog(it.id)
            findNavController().navigate(action)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("HOMEFRAG :: ", "ONCREATE")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.i("HOMEFRAG :: ", "ONCREATEVIEW")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        RxPermissions(this)
            .request(READ_EXTERNAL_STORAGE)
            .subscribe({
                homeViewModel.getSongs()
            }, {

            })

        Log.i("HOMEFRAG :: ", "ONRESUME")
    }

    override fun onStart() {
        super.onStart()
        Log.i("HOMEFRAG :: ", "ONSTART")
    }

    override fun onPause() {
        super.onPause()
        Log.i("HOMEFRAG :: ", "ONPAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.i("HOMEFRAG :: ", "ONSTOP")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("HOMEFRAG :: ", "ONDESTROY")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("HOMEFRAG :: ", "ONATTACH")
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("HOMEFRAG :: ", "ONDETACH")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("HOMEFRAG :: ", "ONVIEWCREATED")

        binding.rvAllSong.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = songAdapter
        }

        compositeDisposable.add(
            homeViewModel.songStatePublisher
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is ShowSong -> {
                            binding.tvEmptySong.gone()
                            songAdapter.submitList(it.songList.toList())
                        }
                        is EmptySong -> {
                            binding.tvEmptySong.visible()
                        }
                        is Error -> {

                        }
                    }
                }, {
                    Log.i("DUCKFICK :: ", it.message.toString())
                })
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i("HOMEFRAG :: ", "ONDESTROYVIEW")
        _binding = null

        compositeDisposable.clear()
    }

}
package com.prox.music_download6_ms3.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prox.music_download6_ms3.databinding.FragmentSettingBinding
import com.prox.music_download6_ms3.ui.playlist.PlaylistViewModel

class SettingFragment: Fragment() {


    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val playlistViewModel =
            ViewModelProvider(this)[PlaylistViewModel::class.java]

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initEvent()


        return root
    }

    private fun initEvent() {
        binding.tvRatingApp.setOnClickListener {
            Toast.makeText(requireActivity(), "Comming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.tvFeedBack.setOnClickListener {
            Toast.makeText(requireActivity(), "Comming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.tvPrivacy.setOnClickListener {
            Toast.makeText(requireActivity(), "Comming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.tvShare.setOnClickListener {
            Toast.makeText(requireActivity(), "Comming soon!", Toast.LENGTH_SHORT).show()
        }

        binding.tvUpgrade.setOnClickListener {
            Toast.makeText(requireActivity(), "Comming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
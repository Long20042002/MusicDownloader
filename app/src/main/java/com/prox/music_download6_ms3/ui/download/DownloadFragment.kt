package com.prox.music_download6_ms3.ui.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.prox.music_download6_ms3.adpater.TabLayoutAdapter
import com.prox.music_download6_ms3.databinding.FragmentDownloadBinding
import com.prox.music_download6_ms3.fragment.DownLoadedFragment
import com.prox.music_download6_ms3.fragment.DownLoadingFragment

class DownloadFragment : Fragment() {

    private var _binding: FragmentDownloadBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val downloadViewModel =
            ViewModelProvider(this)[DownloadViewModel::class.java]

        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewPager()
        return root
    }

    private fun initViewPager() {
        val adapter = TabLayoutAdapter(this)
        adapter.addFragment(DownLoadedFragment(), "Downloaded")
        adapter.addFragment(DownLoadingFragment(), "Downloading")

        binding.viewPager2.adapter = adapter
        binding.viewPager2.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
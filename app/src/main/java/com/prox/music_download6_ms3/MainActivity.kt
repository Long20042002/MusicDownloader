package com.prox.music_download6_ms3

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prox.music_download6_ms3.databinding.ActivityMainBinding
import com.prox.music_download6_ms3.fragment.PlayMusicFragment
import com.prox.music_download6_ms3.model.MesFromEventBus
import com.prox.music_download6_ms3.music.ManagerMedia
import com.prox.music_download6_ms3.music.PlayMusicService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var playMusicFragment: PlayMusicFragment ?= null
    var action: Int = 0

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(mesFromEventBus: MesFromEventBus){
        action = mesFromEventBus.ms
       remoteLayoutMusic()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        EventBus.getDefault().register(this)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home, R.id.download, R.id.playlist, R.id.setting
            )
        )
        navView.setupWithNavController(navController)
        navView.itemIconTintList = null

        remoteLayoutMusic()
        setStatusBarColor()

        if (ManagerMedia.isPlaying || !ManagerMedia.isStop){
            musicLayout()
        }

    }


    private fun remoteLayoutMusic() {
        when(action){
            PlayMusicService.ACTION_START -> {
                musicLayout()
                binding.viewPlaceHolder.visibility = View.VISIBLE
            }
            PlayMusicService.ACTION_CLOSE -> {
                supportFragmentManager.popBackStack("playFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
                binding.viewPlaceHolder.visibility = View.GONE

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun musicLayout(){
        val tran = supportFragmentManager.beginTransaction()
        playMusicFragment = PlayMusicFragment()
        tran.replace(R.id.container_layout_playing, playMusicFragment!!)
        tran.addToBackStack("playFragment")
        tran.commit()
    }

    private fun setStatusBarColor(){
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
    }
}
package com.sj.saurabh.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.sj.saurabh.databinding.ActivityMain2Binding
import com.sj.saurabh.utils.Utils

/**
 **@author Saurabh Kumar 27-03-2023
 */
const val KEY_EVENT_ACTION = "key_event_action"
const val KEY_EVENT_EXTRA = "key_event_extra"
private const val IMMERSIVE_FLAG_TIMEOUT = 500L
class MainActivity2 : AppCompatActivity() {
    private lateinit var binding : ActivityMain2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val utils = Utils()
        if (!utils.checkInternetConnection(this)) {
            Toast.makeText(this, "Opps! No Internet\nPlease Connect to Internet", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        binding.fragmentContainer.postDelayed({
//            hideSystemUI() // not hiding ri8 now
        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    /** When key down event is triggered, relay it via local broadcast so fragments can handle it */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                val intent = Intent(KEY_EVENT_ACTION).apply { putExtra(KEY_EVENT_EXTRA, keyCode) }
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.fragmentContainer).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}
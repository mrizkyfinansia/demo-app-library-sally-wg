package com.example.nwg.whitegoods

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.nwg.R
import com.example.nwg.flutter.FlutterConfig
import io.flutter.embedding.android.ExclusiveAppComponent
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WhiteGoodsActivity : AppCompatActivity(), ExclusiveAppComponent<Activity> {
    private lateinit var flutterView: FlutterView
    private lateinit var flutterEngine: FlutterEngine
    private lateinit var loadingSpinner: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_white_goods)
        loadingSpinner = findViewById(R.id.loading_spinner)

        onBackPress()
        initializeFlutter()

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            loadingSpinner.visibility = View.GONE
        }
    }

    private fun initializeFlutter() {
        flutterEngine = FlutterConfig.getFlutterEngine(this)
        flutterEngine.activityControlSurface.attachToActivity(this, this.lifecycle)

        flutterView = findViewById(R.id.flutter_view)
        flutterView.attachToFlutterEngine(flutterEngine)

        FlutterConfig.setMethodCallHandler(this)
    }

    private fun detachFlutterView() {
        flutterEngine.activityControlSurface.detachFromActivity()
        flutterEngine.lifecycleChannel.appIsDetached()
        flutterView.detachFromFlutterEngine()
        flutterEngine.destroy()
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                flutterEngine.navigationChannel.popRoute()
            }
        })
    }

    override fun detachFromFlutterEngine() {
        detachFlutterView()
    }

    override fun onDestroy() {
        detachFlutterView()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        flutterEngine.lifecycleChannel.appIsResumed()
    }

    override fun onPause() {
        super.onPause()
        flutterEngine.lifecycleChannel.appIsPaused()
    }

    override fun getAppComponent(): ComponentActivity {
        return this
    }
}
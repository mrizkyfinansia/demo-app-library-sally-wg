package com.example.nwg

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.nwg.ChannelConfig.channelConfig
import com.example.nwg.databinding.ActivityMainBinding
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.common.MethodChannel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.getString("token", "")
        if (editor != "") {
            ChannelConfig.initialiseFlutterEngine(
                context = applicationContext,
                engineId = "my_engine_id",
                channelId = "library.sally_wg",
                initialRoute = "/login"
            ).channelConfig()

            startActivity(
                ChannelConfig.createBuilder("my_engine_id").build(applicationContext)
            )
        } else {
            ChannelConfig.initialiseFlutterEngine(
                context = applicationContext,
                engineId = "my_engine_id",
                channelId = "library.sally_wg",
                initialRoute = "/login"
            ).channelConfig()
        }

        binding.fab.setOnClickListener {
            val sharedPreferencess = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val editorr = sharedPreferencess.edit()

            // Simpan data saat aplikasi akan dihentikan
            editorr.putString("token", "user_token")
            editorr.apply()
            startActivity(
                ChannelConfig.createBuilder("my_engine_id").build(applicationContext)
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
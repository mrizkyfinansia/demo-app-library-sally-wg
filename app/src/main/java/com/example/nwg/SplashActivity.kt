package com.example.nwg
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.nwg.ChannelConfig.channelConfig

class SplashActivity : AppCompatActivity() {

    // Durasi splash screen dalam milidetik (misalnya 3 detik)
    private val splashDuration: Long = 30000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Menggunakan Handler untuk menunggu beberapa detik sebelum memulai MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            // Mulai MainActivity setelah delay
            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.getString("token", "")
            if (editor != "") {
                ChannelConfig.initialiseFlutterEngine(
                    context = applicationContext,
                    engineId = "my_engine_id",
                    channelId = "library.sally_wg",
                    initialRoute = "/home"
                ).channelConfig()

                startActivity(
                    ChannelConfig.createBuilder("my_engine_id").build(applicationContext)
                )
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                // Selesaikan SplashActivity sehingga tidak bisa kembali ke sini dengan tombol Back
                finish()
            }

        }, splashDuration)
    }
}

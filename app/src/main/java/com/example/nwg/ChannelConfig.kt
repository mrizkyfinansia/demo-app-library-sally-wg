package com.example.nwg

import android.content.Context
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

object ChannelConfig {
    private lateinit var cachedFlutterEngine: FlutterEngine

    fun initialiseFlutterEngine(
        context: Context,
        initialRoute: String?,
        engineId: String,
        channelId: String,
    ): MethodChannel {
        cachedFlutterEngine = FlutterEngine(context)
        if (initialRoute != null) {
            cachedFlutterEngine.navigationChannel.setInitialRoute(initialRoute)
        }
        cachedFlutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache
            .getInstance()
            .put(engineId, cachedFlutterEngine)

        return MethodChannel(
            cachedFlutterEngine.dartExecutor,
            channelId
        )
    }

    fun createBuilder(engineId: String): FlutterActivity.CachedEngineIntentBuilder {
        return FlutterActivity.withCachedEngine(engineId).destroyEngineWithActivity(true)
    }

    fun MethodChannel.channelConfig(
        methodCallHandler: MethodChannel.MethodCallHandler? = null
    ) {


        val config : Map<String, Any?> = mapOf(
            "access_token" to "token",
            "environment" to "environment",
            "app_version" to "",
            "employee_id" to "employee_id",
            "pos_code" to "pos code",
            "pos_name" to "pos name",
        )

        val map : Map<String, Any?> = mapOf(
            "app_config" to config,
        )
        this.invokeMethod("library.sally_wg.set_app_config", map)
        this.setMethodCallHandler(methodCallHandler)
    }
}
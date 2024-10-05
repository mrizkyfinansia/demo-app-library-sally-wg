package com.example.nwg.flutter

import android.content.Context
import com.example.nwg.flutter.FlutterConstants.FLUTTER_CHANNEL
import com.example.nwg.flutter.FlutterConstants.FLUTTER_ENGINE_ID
import com.example.nwg.flutter.FlutterConstants.KEY_ACCESS_TOKEN
import com.example.nwg.flutter.FlutterConstants.KEY_APP_CONFIG
import com.example.nwg.flutter.FlutterConstants.KEY_APP_VERSION
import com.example.nwg.flutter.FlutterConstants.KEY_EMPLOYEE_ID
import com.example.nwg.flutter.FlutterConstants.KEY_ENVIRONMENT
import com.example.nwg.flutter.FlutterConstants.KEY_NOTIFICATION_ID
import com.example.nwg.flutter.FlutterConstants.KEY_POS_CODE
import com.example.nwg.flutter.FlutterConstants.KEY_POS_NAME
import com.example.nwg.flutter.FlutterConstants.METHOD_CALL_LOGOUT
import com.example.nwg.flutter.FlutterConstants.METHOD_CALL_SET_CONFIG
import com.example.nwg.flutter.FlutterConstants.VALUE_ENVIRONMENT_DEVELOPMENT
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

object FlutterConfig {
    /**
     * Determines the current environment based on the BuildConfig flavor.
     */
    private const val setEnvironment = VALUE_ENVIRONMENT_DEVELOPMENT

    /**
     * Handles the `METHOD_CALL_LOGOUT` method call from the Flutter side.
     *
     * This function performs the following actions upon receiving the logout call:
     * 1. Clears the application cache (TODO: Implement cache clearing logic).
     * 2. Starts the `LoginActivity`.
     * 3. Finishes the current activity and all activities below it in the task stack.
     * 4. Re-initializes the Flutter engine.
     *
     * @param context Must fill with latest activity context.
     */
    private fun onMethodCallLogout(context: Context) {
        // TODO: Handle logout clear cache

//        if (context is Activity) {
//            context.finish()
//            context.startActivity(
//                Intent(context.applicationContext, LoginActivity::class.java)
//            )
//            context.finishAffinity()
//        }
        initialiseFlutterEngine(context)
    }

    /**
     * Retrieves the Flutter engine from the cache or initializes a new one if not found.
     */
    fun getFlutterEngine(context: Context): FlutterEngine {
        return FlutterEngineCache.getInstance().get(FLUTTER_ENGINE_ID) ?: initialiseFlutterEngine(
            context
        )
    }

    /**
     * Initializes a new Flutter engine, executes the default Dart entrypoint,
     * and caches the engine.
     */
    fun initialiseFlutterEngine(
        context: Context,
    ): FlutterEngine {
        val cachedFlutterEngine = FlutterEngine(context)
        cachedFlutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(FLUTTER_ENGINE_ID, cachedFlutterEngine)
        return cachedFlutterEngine
    }

    /**
     * Configures the method channel for communication between Flutter and native code.
     * Sends initial configuration data to Flutter to listen for method calls from Flutter.
     */
    fun channelConfig(
        accessToken: String,
        employeeId: String,
        posCode: String,
        posName: String,
        notificationId: String? = null,
        context: Context,
    ) {
        val flutterEngine = getFlutterEngine(context)
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, FLUTTER_CHANNEL)

        // Create a map containing the configuration data to be sent to Flutter.
        val config: Map<String, Any?> = mapOf(
            KEY_ACCESS_TOKEN to accessToken,
            KEY_APP_VERSION to "1.0.0", // TODO: Change to actual app version
            KEY_EMPLOYEE_ID to employeeId,
            KEY_ENVIRONMENT to setEnvironment,
            KEY_POS_CODE to posCode,
            KEY_POS_NAME to posName,
            KEY_NOTIFICATION_ID to notificationId
        )

        // Create a map containing the app configuration.
        val map: Map<String, Any?> = mapOf(
            KEY_APP_CONFIG to config,
        )

        // Invoke the METHOD_CALL_SET_CONFIG method on the Flutter side with the configuration data.
        channel.invokeMethod(METHOD_CALL_SET_CONFIG, map)

        // Set a method call handler to listen for method calls from Flutter.
        channel.setMethodCallHandler { call, _ ->
            when (call.method) {
                METHOD_CALL_LOGOUT -> onMethodCallLogout(context)
            }
        }
    }

    /**
     * Sets the method call handler for the Flutter method channel.
     *
     * This function sets up a handler to listen for method calls from the Flutter side
     * and respond accordingly. Currently, it handles the `METHOD_CALL_LOGOUT` method
     * by calling the `onMethodCallLogout` function.
     *
     * @param context Must fill with latest activity context .
     */
    fun setMethodCallHandler(
        context: Context,
    ) {
        val flutterEngine = getFlutterEngine(context)
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, FLUTTER_CHANNEL)

        // Set a method call handler to listen for method calls from Flutter.
        channel.setMethodCallHandler { call, _ ->
            when (call.method) {
                METHOD_CALL_LOGOUT -> onMethodCallLogout(context)
            }
        }
    }
}
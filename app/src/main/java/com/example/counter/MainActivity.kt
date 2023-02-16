package com.example.counter

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


class MainActivity: FlutterActivity() {

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        MethodChannel(
            flutterEngine.dartExecutor,
            "samples.flutter.dev/battery"
        ).setMethodCallHandler { call, result ->

            when(call.method) {
                "getBatteryLevel" -> {
                    val batteryLevel = getBatteryLevel()
                    if (batteryLevel != -1) {
                        result.success(batteryLevel)
                    } else {
                        result.error("UNAVAILABLE", "Battery level not available.", null)
                    }
                }
                "getUsers" -> {
                    val allUser = getUsers()
                    if (allUser.isNotEmpty()) {
                        result.success(allUser)
                    } else {
                        result.error("NOT FOUND", "USERS NOT FOUND.", null)
                    }
                }
                else -> result.notImplemented()
            }




        }
    }

    private fun getBatteryLevel(): Int {
        val batteryLevel: Int

        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }

        return batteryLevel
    }

    private fun getUsers(): String {

        return ("[{\"name\":\"Guimas\",\"document\":\"51814120840\",\"age\":\"19\"},"
                + "{\"name\":\"Garcia\",\"document\":\"12345678909\",\"age\":\"16\"}]")

    }

}


package io.purplesoft.azuredevops

import android.util.Log
import android.content.Intent;
import android.os.Bundle
import io.flutter.embedding.android.FlutterActivity
import androidx.annotation.NonNull
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import java.net.URL

class MainActivity : FlutterActivity(), MethodCallHandler {
    private var sharedUrl = ""
    private val allowedDomains = listOf("dev.azure.com", "visualstudio.com")
    private val CHANNEL = "io.purplesoft.azuredevops.shareextension"
    private val tag = "AzDevopsMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(tag, "onCreate")

        MethodChannel(
            flutterEngine!!.getDartExecutor().getBinaryMessenger(),
            CHANNEL
        ).setMethodCallHandler { call, result -> onMethodCall(call, result) }

        val intent = getIntent()
        handleNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        Log.d(tag, "onNewIntent: ${intent}")
        handleNewIntent(intent)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        Log.d(tag, "onMethodCall: ${call.method}")

        if (call.method.contentEquals("getSharedUrl")) {
            result.success(sharedUrl)
            sharedUrl = ""
        }
    }

    private fun handleNewIntent(intent: Intent) {
        val action = intent.getAction()
        val type = intent.getType()

        if (!Intent.ACTION_SEND.equals(action) || type == null || !"text/plain".equals(type)) {
            Log.d(tag, "Received intent with wrong action or type, not handling it")
            return
        }

        val text = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        Log.d(tag, "Received shared text: ${text}")

        val url = URL(text)
        val host = url.host.lowercase()

        if (!allowedDomains.any( { host.contains(it.lowercase()) } )) {
            Log.d(tag, "Received shared URL with wrong domain (${host}), not handling it")
            return
        }

        updateSharedUrl(intent);
    }

    private fun updateSharedUrl(intent: Intent) {
        sharedUrl = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        Log.d(tag, "updated shared url: $sharedUrl")
    }
}

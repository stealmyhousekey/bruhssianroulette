package com.stelmo.brtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.widget.Toast
import java.nio.file.Files.size
import android.net.wifi.p2p.WifiP2pDeviceList



/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
class WiFiDirectBroadcastReceiver(
        private val manager: WifiP2pManager?,
        private val channel: WifiP2pManager.Channel,
        private val activity: NetworkActivity
) : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val action: String = intent.action
        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    // Wifi Direct mode is enabled
                    Toast.makeText(context, "WIFI IS ENABLED", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "WIFI IS DISABLED", Toast.LENGTH_SHORT).show()

                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                if (manager != null) {
                   //
                    manager?.requestPeers(channel) { peers ->
                        // YOU CAN GET ACCESS TO ALL THE DEVICES YOU FOUND FROM peers OBJECT
                        if(!peers.deviceList.isEmpty()) {
                            val firstpeer = peers.deviceList.first().deviceName
                            Toast.makeText(context, firstpeer, Toast.LENGTH_LONG)
                        }
                        
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
            }
        }
    }
}

package com.stelmo.brtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.wifi.p2p.WifiP2pDevice
import android.widget.TextView
import android.widget.Toast
import android.support.v4.app.SupportActivity
import android.support.v4.app.SupportActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.wifi.p2p.WifiP2pDeviceList
import android.view.View
import android.widget.ArrayAdapter


class NetworkActivity : AppCompatActivity() {

    //register wifip2p manager, channel, and receiver
    val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var mChannel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    var peers: List<WifiP2pDevice>? = null
    var peerNames: Array<String>? = null
    var deviceArray: Array<WifiP2pDevice>? = null

    //register intent filters
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //create and set activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        //initialize components
        val text = findViewById(R.id.TEXT_STATUS_ID) as TextView
        val menuButton = findViewById(R.id.n_menu_button) as Button
        val pollButton = findViewById(R.id.poll_button) as Button

        //disable menu button, because every android phone has a back button lol
        //leaving the buttons in code for future debug reasons.
        menuButton.setVisibility(View.GONE)


        mChannel = manager?.initialize(this, mainLooper, null)
        mChannel?.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(manager, channel, this)
        }

        menuButton.setOnClickListener{
            val intent = Intent(baseContext, HomeActivity::class.java)
            startActivity(intent)
        }


        pollButton.setOnClickListener{
            manager?.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    //do stuff when peers found
                    text.setText("Discovery started...")

                }

                override fun onFailure(reasonCode: Int) {
                    //do stuff when no peers found
                    text.setText("Discovery failed (is wifi on?)")
                }
            })
        }

    }


    // register the broadcast receiver with the intent values to be matched
    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
    }

    //unregister the broadcast receiver
    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }




}

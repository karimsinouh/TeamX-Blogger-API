package com.teamxdevelopers.teamx.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object ConnectivityUtility {

    fun hasInternet(c:Context):Boolean{

        var result=false

        val connectivityManager=c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities= connectivityManager.activeNetwork
            val net=connectivityManager.getNetworkCapabilities(capabilities)

            if(net==null)
                false
            else
                when{
                net.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                net.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                net.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else->false
            }

        } else {
            val info=connectivityManager.activeNetworkInfo
            info!=null && info.isConnected
        }

        return result

    }

}
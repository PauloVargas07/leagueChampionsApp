package com.example.leaguechampions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ChampionsViewModel : ViewModel() {

    fun reqTest() {
        viewModelScope.launch(Dispatchers.IO) {
        val url = URL("http://www.girardon.com.br:3001/champions")
        val urlConnection = url.openConnection() as HttpURLConnection
            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val responseContent = input.bufferedReader().use { it.readText() }

            Log.d("Response", responseContent)

            val gson = Gson()
            val type = object : TypeToken<List<Champion>>() {}.type
            val championsList: List<Champion> = gson.fromJson(responseContent, type)

            championsList.forEach { champion ->
                Log.d("Champion", champion.toString())
            }

            urlConnection.disconnect()
        }

    }

}
package com.example.leaguechampions.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaguechampions.models.Champion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChampionsViewModel : ViewModel() {

    private val _champions = MutableStateFlow<List<Champion>>(emptyList())
    val champions: StateFlow<List<Champion>> = _champions

    fun getChampions() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("http://www.girardon.com.br:3001/champions")
            val urlConnection = url.openConnection() as HttpURLConnection
            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val responseContent = input.bufferedReader().use { it.readText() }

            val gson = Gson()
            val type = object : TypeToken<List<Champion>>() {}.type
            val championsList: List<Champion> = gson.fromJson(responseContent, type)

            withContext(Dispatchers.Main) {
                _champions.value = championsList
            }

            urlConnection.disconnect()
        }
    }
}

package com.example.leaguechampions.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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

class ChampionsViewModel (application: Application) : AndroidViewModel(application) {

    private val _champions = MutableStateFlow<List<Champion>>(emptyList())
    val champions: StateFlow<List<Champion>> = _champions

    private val sharedPreferences = application.getSharedPreferences("champions_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

//    init {
//        loadChampionsFromLocalStorage()
//    }

    var isLoading = MutableStateFlow(false)
        private set

    var currentPage = 1
        private set

    private val maxPage = 10

    fun loadNextPage() {
        if (currentPage > maxPage) return
        if (isLoading.value) return

        getChampions(currentPage)
        currentPage++
    }

    private fun getChampions(page: Int) {
        isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://www.girardon.com.br:3001/champions?page=$page")
                val urlConnection = url.openConnection() as HttpURLConnection
                val input: InputStream = BufferedInputStream(urlConnection.inputStream)
                val responseContent = input.bufferedReader().use { it.readText() }

                val type = object : TypeToken<List<Champion>>() {}.type
                val championsList: List<Champion> = gson.fromJson(responseContent, type)

//                saveChampionsToLocalStorage(championsList)

                withContext(Dispatchers.Main) {
                    _champions.value += championsList
                }

                urlConnection.disconnect()
            } catch (e: Exception) {
                Log.d("error", "${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }


    private fun saveChampionsToLocalStorage(championsList: List<Champion>) {
        val championsJson = gson.toJson(championsList)
        sharedPreferences.edit().putString("champions_data", championsJson).apply()
    }

    private fun loadChampionsFromLocalStorage() {
        val championsJson = sharedPreferences.getString("champions_data", null)
        if (championsJson != null) {
            val type = object : TypeToken<List<Champion>>() {}.type
            val championsList: List<Champion> = gson.fromJson(championsJson, type)
            _champions.value = championsList
        }
    }
}

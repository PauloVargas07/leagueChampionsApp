package com.example.leaguechampions.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.leaguechampions.models.Champion
import com.example.leaguechampions.models.Item
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

    private val _teams = MutableStateFlow<Pair<List<Champion>, List<Champion>>?>(null)
    val teams: StateFlow<Pair<List<Champion>, List<Champion>>?> = _teams

    private val sharedPreferences = application.getSharedPreferences("champions_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

//    init {
//        loadChampionsFromLocalStorage()
//    }
    private var itemsCache: List<Item>? = null

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

                urlConnection.disconnect()

                val type = object : TypeToken<List<Champion>>() {}.type
                val championsList: List<Champion> = gson.fromJson(responseContent, type)

//                saveChampionsToLocalStorage(championsList)

                withContext(Dispatchers.Main) {
                    _champions.value += championsList
                }

            } catch (e: Exception) {
                Log.d("error", "${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }

    private suspend fun fetchItems(): List<Item> {
        if (itemsCache != null) {
            return itemsCache!!
        }

        return withContext(Dispatchers.IO) {
            val url = URL("http://www.girardon.com.br:3001/items?size=242")
            val urlConnection = url.openConnection() as HttpURLConnection

            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val responseContent = input.bufferedReader().use { it.readText() }

            urlConnection.disconnect()

            val type = object : TypeToken<List<Item>>() {}.type
            val itemsList: List<Item> = gson.fromJson(responseContent, type)

            itemsCache = itemsList

            itemsList
        }
    }

    fun generateTeamsAndAssignItems() {
        viewModelScope.launch {
            val currentChampions = _champions.value
            if (currentChampions.size < 10) {
                throw IllegalStateException("Não há campeões suficientes para sortear.")
            }

            val items = fetchItems()

            val teams = drawRandomTeams(currentChampions)

            val team1WithItems = assignItemsToChampions(teams.first, items)
            val team2WithItems = assignItemsToChampions(teams.second, items)

            _teams.value = Pair(team1WithItems, team2WithItems)
        }
    }

    private fun assignItemsToChampions(
        champions: List<Champion>,
        items: List<Item>
    ): List<Champion> {
        return champions.map { champion ->
            val randomItems = items.shuffled().take(6)
            champion.copy(items = randomItems)
        }
    }

    private fun drawRandomTeams(champions: List<Champion>): Pair<List<Champion>, List<Champion>> {
        if (champions.size < 10) {
            throw IllegalStateException("Não há campeões suficientes para sortear.")
        }
        val shuffledChampions = champions.shuffled()
        val selectedChampions = shuffledChampions.take(10)
        val team1 = selectedChampions.take(5)
        val team2 = selectedChampions.drop(5)
        return Pair(team1, team2)
    }

//    private fun saveChampionsToLocalStorage(championsList: List<Champion>) {
//        val championsJson = gson.toJson(championsList)
//        sharedPreferences.edit().putString("champions_data", championsJson).apply()
//    }
//
//    private fun loadChampionsFromLocalStorage() {
//        val championsJson = sharedPreferences.getString("champions_data", null)
//        if (championsJson != null) {
//            val type = object : TypeToken<List<Champion>>() {}.type
//            val championsList: List<Champion> = gson.fromJson(championsJson, type)
//            _champions.value = championsList
//        }
//    }
}

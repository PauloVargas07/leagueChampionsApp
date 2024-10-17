package com.example.leaguechampions.helpers

import com.example.leaguechampions.models.Champion

fun drawRandomTeams(champions: List<Champion>): Pair<List<Champion>, List<Champion>> {
    if (champions.size < 10) {
        throw IllegalStateException("Não há campeões suficientes para sortear.")
    }
    val shuffledChampions = champions.shuffled()
    val selectedChampions = shuffledChampions.take(10)
    val team1 = selectedChampions.take(5)
    val team2 = selectedChampions.drop(5)
    return Pair(team1, team2)
}

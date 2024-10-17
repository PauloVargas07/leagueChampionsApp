package com.example.leaguechampions

import com.example.leaguechampions.helpers.drawRandomTeams
import com.example.leaguechampions.models.Champion
import com.example.leaguechampions.models.ChampionSprite
import com.example.leaguechampions.models.ChampionStats
import org.junit.Assert.*
import org.junit.Test

class DrawRandomTeamsTest {

    private fun createChampions(count: Int): List<Champion> {
        return (1..count).map { id ->
            Champion(
                id = "id_$id",
                key = "key_$id",
                name = "Champion$id",
                title = "Title$id",
                tags = arrayOf("Tag${id % 3}", "Tag${(id + 1) % 3}"),
                stats = ChampionStats(
                    hp = 500.0 + id * 10,
                    hpperlevel = 85.0 + id,
                    mp = 300.0 + id * 5,
                    mpperlevel = 50.0 + id * 0.5,
                    movespeed = 350.0 + id,
                    armor = 30.0 + id * 2,
                    armorperlevel = 3.5 + id * 0.1,
                    spellblock = 32.0 + id * 1.5,
                    spellblockperlevel = 1.25 + id * 0.05,
                    attackrange = 550.0 + id * 10,
                    hpregen = 5.5 + id * 0.5,
                    mpregen = 8.0 + id * 0.8,
                    mpregenperlevel = 0.8 + id * 0.04,
                    crit = 0.0 + id * 0.1,
                    critperlevel = 0.0 + id * 0.02,
                    attackdamage = 60.0 + id * 5,
                    attackdamageperlevel = 3.0 + id * 0.3,
                    attackspeed = 0.625 + id * 0.05,
                    attackspeedperlevel = 2.5 + id * 0.2
                ),
                icon = "icon_$id.png",
                sprite = ChampionSprite(
                    url = "tomi",
                    x = id,
                    y = id + 1
                ),
                description = "Description of Champion$id"
            )
        }
    }

    @Test
    fun `drawRandomTeams retorna duas equipes com 5 campeões cada quando a entrada tem mais de 10 campeões`() {
        val champions = createChampions(20)

        val (team1, team2) = drawRandomTeams(champions)

        assertEquals("A primeira equipe deve ter 5 campeões", 5, team1.size)
        assertEquals("A segunda equipe deve ter 5 campeões", 5, team2.size)

        val team1Ids = team1.map { it.id }.toSet()
        val team2Ids = team2.map { it.id }.toSet()
        val intersection = team1Ids.intersect(team2Ids)
        assertTrue("As equipes não devem ter campeões em comum", intersection.isEmpty())

        val selectedIds = team1Ids.union(team2Ids)
        assertEquals("Deve haver exatamente 10 campeões selecionados", 10, selectedIds.size)

        selectedIds.forEach { id ->
            assertTrue("O campeão $id deve estar na lista original", champions.any { it.id == id })
        }
    }

    @Test
    fun `drawRandomTeams retorna duas equipes com todos os campeões disponíveis quando a entrada tem exatamente 10 campeões`() {
        val champions = createChampions(10)

        val (team1, team2) = drawRandomTeams(champions)

        assertEquals("A primeira equipe deve ter 5 campeões", 5, team1.size)
        assertEquals("A segunda equipe deve ter 5 campeões", 5, team2.size)

        val selectedIds = team1.map { it.id } + team2.map { it.id }
        assertEquals("Todas as IDs devem ser únicas", 10, selectedIds.distinct().size)

        selectedIds.forEach { id ->
            assertTrue("O campeão $id deve estar na lista original", champions.any { it.id == id })
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `drawRandomTeams lança exceção quando a entrada tem menos de 10 campeões`() {

        val champions = createChampions(7)

        drawRandomTeams(champions)
    }

    @Test
    fun `drawRandomTeams não modifica a lista original`() {
        val champions = createChampions(15)
        val originalListCopy = champions.toList()

        drawRandomTeams(champions)

        assertEquals("A lista original não deve ser modificada", originalListCopy, champions)
    }
}
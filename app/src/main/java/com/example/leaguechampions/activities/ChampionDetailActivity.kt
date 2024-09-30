package com.example.leaguechampions.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import com.google.gson.Gson
import com.example.leaguechampions.ui.theme.MyApplicationTheme
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.leaguechampions.Champion

class ChampionDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val championJson = intent.getStringExtra("CHAMPION")
        val champion = Gson().fromJson(championJson, Champion::class.java)

        setContent {
            MyApplicationTheme {
                ChampionDetailsScreen(
                    champion = champion,
                    onBackClicked = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChampionDetailsScreen(champion: Champion?, onBackClicked: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // Garantindo o padding da barra superior
                .padding(16.dp) // Padding interno para o conteúdo
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top, // Garante que os itens da coluna fiquem no topo
            horizontalAlignment = Alignment.Start // Alinha os itens à esquerda
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = champion?.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(end = 16.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier.align(Alignment.Top)
                ) {
                    Text(
                        text = champion?.name ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Text(
                        text = champion?.title ?: "",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.05f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    champion?.tags?.let { tags ->
                        Text(
                            text = tags.joinToString(", ").uppercase(),
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descrição
            Text(
                text = "Description",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize * 0.9f
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = champion?.description ?: "",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = MaterialTheme.typography.bodyMedium.fontSize * 1.05f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Estatísticas
            Text(
                text = "Stats",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize * 0.9f
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                val stats = listOf(
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/7/75/Attack_damage_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203443", "Attack Damage" to champion?.stats?.attackdamage),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/1/17/Health_icon.png/revision/latest/scale-to-width-down/14?cb=20240607103046", "Health" to champion?.stats?.hp),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/f/f0/Armor_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203442", "Armor" to champion?.stats?.armor),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/e/ea/Movement_speed_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203540", "Move Speed" to champion?.stats?.movespeed),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/9/91/Attack_speed_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203443", "Attack Speed" to champion?.stats?.attackspeed),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/8/84/Magic_resistance_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203539", "Magic Resist" to champion?.stats?.spellblock),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/3/31/Health_regeneration_icon.png/revision/latest/scale-to-width-down/14?cb=20240607102806", "Health Regen" to champion?.stats?.hpregen),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/1/13/Range_icon.png/revision/latest/scale-to-width-down/14?cb=20170715002053", "Attack Range" to champion?.stats?.attackrange),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/c/cc/Chance_de_Acerto_Cr%C3%ADtico_%C3%8Dcone.png/revision/latest/scale-to-width-down/16?cb=20210418131915&path-prefix=pt-br", "Critical Strike" to champion?.stats?.crit),
                )

                stats.chunked(3).forEach { rowStats ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowStats.forEach { (iconUrl, stat) ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = iconUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp),
                                    contentScale = ContentScale.Fit
                                )
                                Text(
                                    text = "${stat.first}: ${stat.second ?: "N/A"}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = MaterialTheme.typography.bodySmall.fontSize * 1.1f)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

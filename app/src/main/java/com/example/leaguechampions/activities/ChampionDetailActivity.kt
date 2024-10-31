package com.example.leaguechampions.activities

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.leaguechampions.R
import com.example.leaguechampions.models.Champion
import com.example.leaguechampions.ui.theme.MyApplicationTheme
import com.google.gson.Gson

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

@SuppressLint("DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChampionDetailsScreen(champion: Champion?, onBackClicked: () -> Unit) {
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(champion?.name ?: "")
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        champion?.let {
                            mediaPlayer?.stop()
                            mediaPlayer?.release()
                            mediaPlayer = null

                            val audioResId = context.resources.getIdentifier(it.id.lowercase(), "raw", context.packageName)
                            if (audioResId != 0) {
                                mediaPlayer = MediaPlayer.create(context, audioResId)
                                mediaPlayer?.start()
                                mediaPlayer?.setOnCompletionListener { mp ->
                                    mp.release()
                                    mediaPlayer = null
                                }
                            } else {
                                Toast.makeText(context, context.getString(R.string.no_sound_available), Toast.LENGTH_SHORT).show()
                                Log.e("ChampionDetails", "Arquivo de áudio não encontrado para o campeão: ${it.id}")
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = stringResource(id = R.string.play_sound)
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
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
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    champion?.tags?.let { tags ->
                        Text(
                            text = tags.joinToString(", ").uppercase(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.description),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = champion?.description ?: "",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.stats),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                val stats = listOf(
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/7/75/Attack_damage_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203443", stringResource(id = R.string.attack_damage) to champion?.stats?.attackdamage),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/1/17/Health_icon.png/revision/latest/scale-to-width-down/14?cb=20240607103046", stringResource(id = R.string.health) to champion?.stats?.hp),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/f/f0/Armor_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203442", stringResource(id = R.string.armor) to champion?.stats?.armor),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/e/ea/Movement_speed_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203540", stringResource(id = R.string.move_speed) to champion?.stats?.movespeed),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/9/91/Attack_speed_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203443", stringResource(id = R.string.attack_speed) to champion?.stats?.attackspeed),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/8/84/Magic_resistance_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203539", stringResource(id = R.string.magic_resist) to champion?.stats?.spellblock),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/3/31/Health_regeneration_icon.png/revision/latest/scale-to-width-down/14?cb=20240607102806", stringResource(id = R.string.health_regen) to champion?.stats?.hpregen),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/1/13/Range_icon.png/revision/latest/scale-to-width-down/14?cb=20170715002053", stringResource(id = R.string.attack_range) to champion?.stats?.attackrange),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/c/cc/Chance_de_Acerto_Cr%C3%ADtico_%C3%8Dcone.png/revision/latest/scale-to-width-down/16?cb=20210418131915&path-prefix=pt-br", stringResource(id = R.string.critical_strike) to champion?.stats?.crit),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/3/31/Health_regeneration_icon.png/revision/latest/scale-to-width-down/15?cb=20240607102806", stringResource(id = R.string.health_per_level) to champion?.stats?.hpperlevel),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/8/8b/Mana_icon.png/revision/latest/scale-to-width-down/15?cb=20240607103302", stringResource(id = R.string.mana) to champion?.stats?.mp),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/8/8b/Mana_icon.png/revision/latest/scale-to-width-down/15?cb=20240607103302", stringResource(id = R.string.mana_per_level) to champion?.stats?.mpperlevel),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/f/f0/Armor_icon.png/revision/latest/scale-to-width-down/15?cb=20170515203442", stringResource(id = R.string.armor_per_level) to champion?.stats?.armorperlevel),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/8/84/Magic_resistance_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203539", stringResource(id = R.string.magic_resist_per_level) to champion?.stats?.spellblockperlevel),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/0/0c/Mana_regeneration_icon.png/revision/latest/scale-to-width-down/15?cb=20240607103627", stringResource(id = R.string.mana_regen) to champion?.stats?.mpregen),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/0/0c/Mana_regeneration_icon.png/revision/latest/scale-to-width-down/15?cb=20240607103627", stringResource(id = R.string.mana_regen_per_level) to champion?.stats?.mpregenperlevel),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/7/75/Attack_damage_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203443", stringResource(id = R.string.attack_damage_per_level) to champion?.stats?.attackdamageperlevel),
                    Pair("https://static.wikia.nocookie.net/leagueoflegends/images/9/91/Attack_speed_icon.png/revision/latest/scale-to-width-down/14?cb=20170515203443", stringResource(id = R.string.attack_speed_per_level) to champion?.stats?.attackspeedperlevel)
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

package com.example.leaguechampions.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.leaguechampions.ui.theme.MyApplicationTheme
import com.google.gson.Gson
import com.example.leaguechampions.models.Champion
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

class TeamsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val team1Json = intent.getStringExtra("TEAM1")
        val team2Json = intent.getStringExtra("TEAM2")
        val gson = Gson()
        val team1 = gson.fromJson(team1Json, Array<Champion>::class.java).toList()
        val team2 = gson.fromJson(team2Json, Array<Champion>::class.java).toList()

        setContent {
            MyApplicationTheme {
                TeamsScreen(
                    team1 = team1,
                    team2 = team2,
                    onBackClicked = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    team1: List<Champion>,
    team2: List<Champion>,
    onBackClicked: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Times Gerados",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Time 1",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChampionTeamRow(team = team1)

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Time 2",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChampionTeamRow(team = team2)

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val shareText = buildString {
                            append("Time 1:\n")
                            team1.forEach { append("- ${it.name}\n") }
                            append("\nTime 2:\n")
                            team2.forEach { append("- ${it.name}\n") }
                        }
                        shareText(context, shareText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Compartilhar Times")
                }
            }
        }
    )
}

@Composable
fun ChampionTeamRow(team: List<Champion>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(team) { champion ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(60.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = champion.icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = champion.name,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    fontSize = 13.sp
                )
            }
        }
    }
}

fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar via"))
}

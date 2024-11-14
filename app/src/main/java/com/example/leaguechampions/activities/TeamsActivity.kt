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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.leaguechampions.R
import com.example.leaguechampions.models.Item


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

    val team1Title = stringResource(id = R.string.team_1)
    val team2Title = stringResource(id = R.string.team_2)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.generated_teams),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.team_1),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        ChampionTeamColumn(team = team1, cardSize = 50.dp)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.team_2),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Red,
                            modifier = Modifier
                                .padding(8.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ChampionTeamColumn(team = team2, cardSize = 50.dp)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        val shareText = buildString {
                            append("${team1Title}:\n")
                            team1.forEach { append("- ${it.name}\n") }
                            append("\n${team2Title}:\n")
                            team2.forEach { append("- ${it.name}\n") }
                        }
                        shareText(context, shareText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0397AB)),
                    ) {
                    Text(stringResource(id = R.string.share_teams))
                }
            }
        }
    )
}

@Composable
fun ChampionTeamColumn(team: List<Champion>, cardSize: Dp) {
    val iconUrls = listOf(
        "https://static.wikia.nocookie.net/leagueoflegends/images/e/ef/Top_icon.png/revision/latest/scale-to-width-down/32?cb=20181117143602",
        "https://static.wikia.nocookie.net/leagueoflegends/images/1/1b/Jungle_icon.png/revision/latest/scale-to-width-down/32?cb=20181117143559",
        "https://static.wikia.nocookie.net/leagueoflegends/images/9/98/Middle_icon.png/revision/latest/scale-to-width-down/32?cb=20181117143644",
        "https://static.wikia.nocookie.net/leagueoflegends/images/9/97/Bottom_icon.png/revision/latest/scale-to-width-down/32?cb=20181117143632",
        "https://static.wikia.nocookie.net/leagueoflegends/images/e/e0/Support_icon.png/revision/latest/scale-to-width-down/32?cb=20181117143601"
    )

    val expandedChampionIds = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        team.forEachIndexed { index, champion ->
            val isExpanded = expandedChampionIds.contains(champion.id)
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(8.dp)
                    .background(Color(0xFF3C3C41), shape = MaterialTheme.shapes.medium)
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        if (isExpanded) {
                            expandedChampionIds.remove(champion.id)
                        } else {
                            expandedChampionIds.add(champion.id)
                        }
                    }
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (index < iconUrls.size) {
                        Image(
                            painter = rememberAsyncImagePainter(model = iconUrls[index]),

                            contentDescription = "Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = rememberAsyncImagePainter(model = champion.icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(cardSize)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = champion.name,
//                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(horizontal = 8.dp)
//                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ChampionItemsRow(champion.items)
                }
            }
        }
    }
}

@Composable
fun ChampionItemsRow(items: List<Item>) {
    if (items.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1C1C1E))
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item ->
                Image(
                    painter = rememberAsyncImagePainter(model = item.icon),
                    contentDescription = item.name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                )
            }
        }
    } else {
        Text(text = "No items assigned.", style = MaterialTheme.typography.bodySmall)
    }
}


fun shareText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Compartilhar via"))
}

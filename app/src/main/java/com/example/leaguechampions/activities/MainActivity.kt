package com.example.leaguechampions.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.leaguechampions.R
import com.example.leaguechampions.helpers.drawRandomTeams
import com.example.leaguechampions.models.Champion
import com.example.leaguechampions.viewmodels.ChampionsViewModel
import com.google.gson.Gson
import com.example.leaguechampions.ui.theme.MyApplicationTheme
import com.example.leaguechampions.utils.sendTeamNotification

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            MyApplicationTheme {
                actionBar?.hide()
                ChampionListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChampionListScreen(championsViewModel: ChampionsViewModel = viewModel()) {
    val champions by championsViewModel.champions.collectAsState(initial = emptyList())
    val isLoading by championsViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        championsViewModel.loadNextPage()
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredChampions = if (searchQuery.isEmpty()) champions else champions.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            Pair(totalItems, lastVisibleItem)
        }.collect { (totalItems, lastVisibleItem) ->
            if (lastVisibleItem >= totalItems - 5 && !isLoading && championsViewModel.currentPage <= 10) {
                championsViewModel.loadNextPage()
            }
        }
    }
    val context = LocalContext.current

    val permissionDeniedMessage = stringResource(id = R.string.permission_denied)

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                sendTeamNotification(context)
            } else {
                Toast.makeText(context, permissionDeniedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Button(
                    onClick = {
                        val teams = drawRandomTeams(champions)
                        val intent = Intent(context, TeamsActivity::class.java).apply {
                            putExtra("TEAM1", Gson().toJson(teams.first))
                            putExtra("TEAM2", Gson().toJson(teams.second))
                        }
                        context.startActivity(intent)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else sendTeamNotification(context)
                    },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(stringResource(id = R.string.sort_teams))
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(stringResource(id = R.string.search_champions)) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    state = listState
                ) {
                    items(filteredChampions) { champion ->
                        ChampionCard(champion, context)
                    }
                }
            }
        }
    )
}

@Composable
fun ChampionCard(champion: Champion, context: Context) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                val intent =
                    Intent(context, ChampionDetailsActivity::class.java).apply {
                        putExtra("CHAMPION", Gson().toJson(champion))
                    }
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = champion.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop,
            )
            Column {
                Text(text = champion.name, style = MaterialTheme.typography.titleLarge)
                Text(text = champion.title, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
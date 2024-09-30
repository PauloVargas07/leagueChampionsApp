package com.example.leaguechampions.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.leaguechampions.ChampionsViewModel
import com.google.gson.Gson
import com.example.leaguechampions.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                ChampionListScreen()
            }
        }
    }
}

@Composable
fun ChampionListScreen(championsViewModel: ChampionsViewModel = viewModel()) {
    val champions by championsViewModel.champions.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        championsViewModel.reqTest()
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredChampions = if (searchQuery.isEmpty()) {
        champions
    } else {
        champions.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
    }

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Pesquisar Campeões") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(filteredChampions) { champion ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val intent = Intent(context, ChampionDetailsActivity::class.java).apply {
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
        }
    }
}



package ph.edu.comteq.deguit_lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.deguit_lab3.ui.theme.Deguit_Lab3Theme

// Font setup

val playfair: FontFamily
    @Composable
    get() = if (LocalInspectionMode.current) FontFamily.Serif else FontFamily(Font(R.font.playfairdisplayregular, FontWeight.Normal))

class ArtistsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Deguit_Lab3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF3E8CC) // light beige background
                ) {
                    ArtistsScreen()
                }
            }
        }
    }
}

@Composable
fun ArtistsScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Artists", "Artworks")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E8CC))
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // Title section
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Explore the art of",
                fontFamily = playfair,
                fontSize = 32.sp,
                color = Color.Black,
                lineHeight = 40.sp
            )
            Text(
                text = "Renaissance",
                fontFamily = playfair,
                fontSize = 42.sp,
                color = Color(0xFFD4A574),
                lineHeight = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text("Type to search…", color = Color.Gray, fontFamily = playfairdisplayregular)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.Gray
                )
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF999999),
                unfocusedBorderColor = Color(0xFF999999),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = Color(0xFFD4A574),
            divider = {},
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(3.dp),
                        color = Color(0xFFD4A574)
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontFamily = playfairdisplayregular,
                            fontSize = 18.sp,
                            color = if (selectedTabIndex == index) Color(0xFFD4A574) else Color.Gray
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedTabIndex) {
            0 -> ArtistList()
            1 -> ArtworkList()
        }
    }
}

data class Artist(val name: String, val years: String, val avatar: Int, val artworks: List<Int>)

@Composable
fun ArtistList() {
    val artists = listOf(
        Artist(
            "Leonardo da Vinci",
            "1452 - 1519",
            R.drawable.leonardo_da_vinci,
            listOf(
                R.drawable.mona_lisa,
                R.drawable.lady_ermine,
                R.drawable.litta_madonna
            )
        ),
        Artist(
            "Michelangelo",
            "1475 - 1564",
            R.drawable.michelangelo,
            listOf(
                R.drawable.david,
                R.drawable.delphic_sibyl,
                R.drawable.torment_of_saint_anthony
            )
        ),
        Artist(
            "Gustav Klimt",
            "1862 - 1918",
            R.drawable.gustav_klimt,
            listOf(
                R.drawable.the_kiss,
                R.drawable.lady_with_fan,
                R.drawable.adele_bloch_bauer
            )
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        items(artists) { artist ->
            ArtistItem(artist)
        }
    }
}

@Composable
fun ArtistItem(artist: Artist) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Artist header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = artist.avatar),
                contentDescription = artist.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = artist.name,
                    fontFamily = playfairdisplayregular,
                    fontSize = 24.sp,
                    color = Color(0xFF1C1C1C)
                )
                Text(
                    text = artist.years,
                    fontFamily = playfairdisplayregular,
                    fontSize = 16.sp,
                    color = Color(0xFF7D7D7D)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Artworks
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(artist.artworks) { artwork ->
                Image(
                    painter = painterResource(id = artwork),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(width = 180.dp, height = 260.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

@Composable
fun ArtworkList() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Artworks section coming soon…", fontFamily = playfairdisplayregular, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun ArtistsScreenPreview() {
    Deguit_Lab3Theme {
        ArtistsScreen()
    }
}

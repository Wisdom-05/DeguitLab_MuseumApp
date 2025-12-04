package ph.edu.comteq.deguit_lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import org.json.JSONArray
import ph.edu.comteq.deguit_lab3.ui.theme.Deguit_Lab3Theme
import kotlin.math.abs

// ✅ Load Optima font safely


class ExhibitActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Deguit_Lab3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1A1A1A)
                ) {
                    ExhibitScreen()
                }
            }
        }
    }
}

// Data class for type-safe artwork details
data class Artwork(
    val title: String,
    val years: String,
    val bornAt: String,
    val comment: String,
    val imageResId: Int
) {
    val locationText: String
        get() = "$years, $bornAt"
}

// Encapsulated logic to load artworks for both runtime and preview
@Composable
fun rememberArtworks(): List<Artwork> {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current

    return if (isPreview) {
        // Provide stable data for preview
        listOf(
            Artwork(
                title = "Mona Lisa",
                years = "c. 1503-19",
                bornAt = "Florence, Italy",
                comment = "The best known, the most visited, the most written about, the most sung about, the most parodied work of art in the world",
                imageResId = R.drawable.mona_lisa
            ),
            Artwork(
                title = "Lady Ermine",
                years = "c. 1489-91",
                bornAt = "Milan, Italy",
                comment = "It is a captivating image of exquisite elegance and reveals the artistic genius of Leonardo's incomparable creative mind",
                imageResId = R.drawable.lady_ermine
            ),
            Artwork(
                title = "Litta Madonna",
                years = "c. 1490",
                bornAt = "Italy",
                comment = "This portrayal reflects the religious devotion of the Renaissance period, emphasizes the virtues of motherhood",
                imageResId = R.drawable.litta_madonna
            )
        )
    } else {
        // Load data from JSON for the actual app
        remember {
            val artworksJson =
                context.assets.open("artworks.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(artworksJson)
            buildList {
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val title = obj.getString("title")
                    val years = obj.getString("years")
                    val bornAt = obj.getString("born_at")
                    val comment = obj.getString("comment")
                    add(
                        Artwork(
                            title = title,
                            years = years,
                            bornAt = bornAt,
                            comment = comment,
                            imageResId = getDrawableIdForTitle(title)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ExhibitScreen() {
    val artworks = rememberArtworks()
    val listState = rememberLazyListState()
    var selectedIndex by remember { mutableStateOf(0) }

    // Track which artwork is visually centered as the user swipes
    LaunchedEffect(listState, artworks) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collectLatest { visibleItems ->
                if (visibleItems.isEmpty()) return@collectLatest

                val layoutInfo = listState.layoutInfo
                val viewportCenter =
                    (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2f

                val closestItem = visibleItems.minByOrNull { itemInfo ->
                    val itemCenter = itemInfo.offset + itemInfo.size / 2f
                    abs(itemCenter - viewportCenter)
                }

                selectedIndex = closestItem?.index
                    ?.coerceIn(0, maxOf(artworks.size - 1, 0))
                    ?: 0
            }
    }

    val currentArtwork = artworks.getOrNull(selectedIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Petal-shaped, horizontally swipeable artworks
        LazyRow(
            state = listState,
            contentPadding = PaddingValues(horizontal = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(artworks) { index, artwork ->
                val isSelected = index == selectedIndex
                val baseModifier = Modifier
                    .width(280.dp)
                    .graphicsLayer {
                        // Slight emphasis on the centered artwork
                        val scale = if (isSelected) 1f else 0.94f
                        scaleX = scale
                        scaleY = scale
                    }

                if (index % 2 == 0) {
                    // Mona Lisa / Litta Madonna style: gold on top, image with rounded bottom
                    ArtworkTopGoldBottomPetalCard(
                        artwork = artwork,
                        modifier = baseModifier
                    )
                } else {
                    // Lady Ermine style: image with rounded top, gold on bottom
                    ArtworkTopArchBottomGoldCard(
                        artwork = artwork,
                        modifier = baseModifier
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Quote / comment section that updates while swiping
        if (currentArtwork != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.quote),
                    contentDescription = "Quote",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 12.dp),
                    alpha = 0.5f
                )
                Text(
                    text = currentArtwork.comment,
                    fontSize = 18.sp,
                    fontFamily = playfairdisplayregular,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    lineHeight = 26.sp
                )
            }
        }
    }
}

@Composable
private fun ArtworkTopGoldBottomPetalCard(
    artwork: Artwork,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gold info card at the top
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFCC9B32)
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = artwork.title,
                        fontFamily = playfairdisplayregular,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = artwork.locationText,
                        fontFamily = playfairdisplayregular,
                        fontSize = 14.sp,
                        color = Color(0xFF4A3613)
                    )
                }

                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color(0xFF1A1A1A)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = "View details",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Petal-shaped artwork image (rounded heavily at the bottom)
        Image(
            painter = painterResource(id = artwork.imageResId),
            contentDescription = artwork.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(bottomPetalShape())
        )
    }
}

@Composable
private fun ArtworkTopArchBottomGoldCard(
    artwork: Artwork,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Artwork image with rounded top (arch)
        Image(
            painter = painterResource(id = artwork.imageResId),
            contentDescription = artwork.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(topPetalShape())
        )

        // Gold info card at the bottom
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFCC9B32)
            ),
            shape = RoundedCornerShape(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = artwork.title,
                        fontFamily = playfairdisplayregular,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = artwork.locationText,
                        fontFamily = playfairdisplayregular,
                        fontSize = 14.sp,
                        color = Color(0xFF4A3613)
                    )
                }

                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = Color(0xFF1A1A1A)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = "View details",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

// Custom shape for the petal bottom
fun bottomPetalShape(): Shape = RoundedCornerShape(
    topStart = 0.dp,
    topEnd = 0.dp,
    bottomStart = 220.dp,
    bottomEnd = 220.dp
)

// Custom shape for the petal top (arch)
fun topPetalShape(): Shape = RoundedCornerShape(
    topStart = 220.dp,
    topEnd = 220.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

private fun getDrawableIdForTitle(title: String): Int {
    return when (title) {
        "Mona Lisa" -> R.drawable.mona_lisa
        "Lady Ermine" -> R.drawable.lady_ermine
        "Litta Madonna" -> R.drawable.litta_madonna
        else -> R.drawable.mona_lisa
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ExhibitScreenPreview() {
    Deguit_Lab3Theme {
        ExhibitScreen()
    }
}

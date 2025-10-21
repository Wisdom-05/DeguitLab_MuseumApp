package ph.edu.comteq.deguit_lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import ph.edu.comteq.deguit_lab3.ui.theme.Deguit_Lab3Theme

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

// ✅ Data class for type-safe artwork details
data class Artwork(
    val title: String,
    val location: String,
    val comment: String,
    val imageResId: Int
)

// ✅ Encapsulated logic to load artwork for both runtime and preview
@Composable
fun rememberArtwork(): Artwork {
    val isPreview = LocalInspectionMode.current
    val context = LocalContext.current

    return if (isPreview) {
        // Provide stable data for preview
        Artwork(
            title = "Lady Ermine",
            location = "c. 1489-91, Milan, Italy",
            comment = "It is a captivating image of exquisite elegance and reveals the artistic genius of Leonardo's incomparable creative mind",
            imageResId = R.drawable.lady_ermine
        )
    } else {
        // Load data from JSON for the actual app
        remember {
            val artworksJson = context.assets.open("artworks.json").bufferedReader().use { it.readText() }
            val firstArtworkJson = JSONArray(artworksJson).getJSONObject(0)
            Artwork(
                title = firstArtworkJson.getString("title"),
                location = firstArtworkJson.getString("location"),
                comment = firstArtworkJson.getString("comment"),
                imageResId = getDrawableIdFromFileName(firstArtworkJson.getString("image"))
            )
        }
    }
}

@Composable
fun ExhibitScreen() {
    val artwork = rememberArtwork()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2B2B))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top image section with arch-style clipping
        Image(
            painter = painterResource(id = artwork.imageResId),
            contentDescription = artwork.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .clip(topArchShape())
        )

        // Gold info card (below image)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFD4A574)
            ),
            shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = artwork.title,
                        fontFamily = playfairdisplayregular,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = artwork.location,
                        fontFamily = playfairdisplayregular,
                        fontSize = 16.sp,
                        color = Color(0xFF5A4A3A)
                    )
                }

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color(0xFF1A1A1A)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ArrowOutward,
                            contentDescription = "View details",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Quote section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.quote),
                contentDescription = "Quote",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 8.dp),
                alpha = 0.5f
            )
            Text(
                text = artwork.comment,
                fontSize = 18.sp,
                fontFamily = playfairdisplayregular,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                lineHeight = 28.sp
            )
        }
    }
}

// ✅ Custom shape for top arch
fun topArchShape(): Shape = RoundedCornerShape(topStart = 220.dp, topEnd = 220.dp)

fun getDrawableIdFromFileName(imageName: String): Int {
    return when (imageName) {
        "mona_lisa.jpg" -> R.drawable.mona_lisa
        "lady_ermine.jpg" -> R.drawable.lady_ermine
        "litta_madonna.jpg" -> R.drawable.litta_madonna
        "david.jpg" -> R.drawable.david
        "delphic_sibyl.jpg" -> R.drawable.delphic_sibyl
        "torment_of_saint_anthony.jpg" -> R.drawable.torment_of_saint_anthony
        "the_kiss.jpg" -> R.drawable.the_kiss
        "lady_with_fan.jpg" -> R.drawable.lady_with_fan
        "adele_bloch_bauer.jpg" -> R.drawable.adele_bloch_bauer
        else -> 0
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
fun ExhibitScreenPreview() {
    Deguit_Lab3Theme {
        ExhibitScreen()
    }
}

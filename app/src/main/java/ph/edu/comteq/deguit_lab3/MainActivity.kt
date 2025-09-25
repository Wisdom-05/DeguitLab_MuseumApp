package ph.edu.comteq.deguit_lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.deguit_lab3.ui.theme.Deguit_Lab3Theme

// Fonts with fallback
val playfairdisplayregular = FontFamily(Font(R.font.playfairdisplayregular, FontWeight.Normal))
val optima = FontFamily(Font(R.font.optima, FontWeight.Normal))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Deguit_Lab3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Homepage()
                }
            }
        }
    }
}

@Composable
fun Homepage(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.louvre),
            contentDescription = "Louvre Museum",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(140.dp)
                    .padding(top = 40.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Title
            Text(
                text = "Experience Art",
                fontFamily = playfairdisplayregular,
                fontSize = 32.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = "Join us for an extraordinary event\nthat immerses you in the world of art.",
                fontFamily = optima,
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Explore button
            Button(
                onClick = {
                    try {
                        println("Explore button clicked!") // Debug log
                        val intent = Intent(context, ExploreActivity::class.java)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        println("Error navigating to ExploreActivity: ${e.message}")
                        e.printStackTrace()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    text = "Explore Now",
                    fontFamily = optima,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Deguit_Lab3Theme {
        Homepage()
    }
}
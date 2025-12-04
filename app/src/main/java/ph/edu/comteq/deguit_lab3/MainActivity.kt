package ph.edu.comteq.deguit_lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
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
    // Progress of the museum reveal (0f = hidden, 1f = fully shown)
    val museumReveal = remember { Animatable(0f) }
    var showTitle by remember { mutableStateOf(false) }
    var showIntro by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    // Run the animation sequence: museum → title typing → intro typing → button
    LaunchedEffect(Unit) {
        // 1. Reveal the museum from top to bottom
        museumReveal.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400)
        )

        // 2. Start typing the title once the museum is fully visible
        showTitle = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        MuseumBackground(
            progress = museumReveal.value,
            modifier = Modifier.fillMaxSize()
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

            // Title with typing effect
            if (showTitle) {
                TypewriterText(
                    text = "Experience Art",
                    fontFamily = playfairdisplayregular,
                    fontSize = 32.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    onFinished = {
                        if (!showIntro) showIntro = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Introduction text with typing effect, starts after title
            if (showIntro) {
                TypewriterText(
                    text = "Join us for an extraordinary event that immerses you in the world of art.",
                    fontFamily = optima,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    typingDelayMillis = 35L,
                    onFinished = {
                        showButton = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Explore button fades in after text is done
            if (showButton) {
                Button(
                    onClick = {
                        try {
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
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MuseumBackground(progress: Float, modifier: Modifier = Modifier) {
    // Reveal the museum image from top to bottom using a clipping animation
    Box(
        modifier = modifier
            .background(Color.Black)
            .graphicsLayer {
                // Nothing animated here; clipping is done in drawWithContent below
            }
            .drawWithReveal(progress)
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.louvre),
            contentDescription = "Louvre Museum",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
    }
}

// Helper modifier to reveal content vertically from top to bottom
@Composable
private fun Modifier.drawWithReveal(progress: Float): Modifier =
    this.then(
        Modifier.drawWithContent {
            val clamped = progress.coerceIn(0f, 1f)
            if (clamped <= 0f) return@drawWithContent

            val revealBottom = size.height * clamped
            clipRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = revealBottom
            ) {
                this@drawWithContent.drawContent()
            }
        }
    )

@Composable
fun TypewriterText(
    text: String,
    fontFamily: FontFamily,
    fontSize: androidx.compose.ui.unit.TextUnit,
    color: Color,
    textAlign: TextAlign,
    typingDelayMillis: Long = 45L,
    onFinished: () -> Unit = {}
) {
    var visibleText by remember(text) { mutableStateOf("") }

    LaunchedEffect(text) {
        visibleText = ""
        for (i in 1..text.length) {
            visibleText = text.substring(0, i)
            kotlinx.coroutines.delay(typingDelayMillis)
        }
        onFinished()
    }

    Text(
        text = visibleText,
        fontFamily = fontFamily,
        fontSize = fontSize,
        color = color,
        textAlign = textAlign
    )
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    Deguit_Lab3Theme {
        Homepage()
    }
}

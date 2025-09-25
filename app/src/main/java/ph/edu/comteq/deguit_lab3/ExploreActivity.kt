package ph.edu.comteq.deguit_lab3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.deguit_lab3.ui.theme.Deguit_Lab3Theme

class ExploreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Deguit_Lab3Theme {
                ExploreScreen()
            }
        }
    }
}

@Composable
fun ExploreScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Header
        Text(
            text = "Explore",
            fontFamily = playfairdisplayregular,
            fontSize = 32.sp,
            color = Color(0xFFFFD43F),
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
        )

        // Upcoming Event row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Upcoming Event",
                fontFamily = optima,
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Normal
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Only "Tickets" is clickable
                Text(
                    text = "Tickets",
                    fontFamily = optima,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.clickable {
                        val intent = Intent(context, TicketingActivity::class.java)
                        context.startActivity(intent)
                    }
                )
                Text(
                    text = " ›",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Event Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column {
                // Renaissance painting image
                Image(
                    painter = painterResource(id = R.drawable.renaissance),
                    contentDescription = "Renaissance Exhibition",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // Event details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF333333))
                        .padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Date box
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(70.dp)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "10",
                            fontFamily = optima,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "OCT",
                            fontFamily = optima,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Normal
                        )
                    }

                    // Details
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp)
                    ) {
                        Text(
                            text = "Renaissance Exhibition",
                            fontFamily = playfairdisplayregular,
                            fontSize = 22.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Text(
                            text = "9:00 AM - 6:00 PM",
                            fontFamily = optima,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Indulge in the rich tapestry of Renaissance art",
                            fontFamily = optima,
                            fontSize = 16.sp,
                            color = Color(0xFFFFD43F),
                            modifier = Modifier.padding(bottom = 12.dp),
                            lineHeight = 22.sp
                        )
                        Text(
                            text = "+33 (0)1 23 45 67 89",
                            fontFamily = optima,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Visit Gallery button
                Button(
                    onClick = { /* Not linked to Ticketing */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFD43F)
                    )
                ) {
                    Text(
                        text = "Visit Gallery",
                        fontFamily = optima,
                        fontSize = 18.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    Deguit_Lab3Theme {
        ExploreScreen()
    }
}

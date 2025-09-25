package ph.edu.comteq.deguit_lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.deguit_lab3.ui.theme.Deguit_Lab3Theme

class TicketingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Deguit_Lab3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicketingScreen()
                }
            }
        }
    }
}

@Composable
fun TicketingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Ticketing",
                fontSize = 28.sp,
                color = Color(0xFFFFD700),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Renaissance Exhibition", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Date: 10 OCT", fontSize = 14.sp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Time: 9:00 AM - 6:00 PM", fontSize = 14.sp, color = Color.LightGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Price: ₱500", fontSize = 14.sp, color = Color.LightGray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { /* TODO: Ticket purchase logic */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Buy Ticket", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketingScreenPreview() {
    Deguit_Lab3Theme {
        TicketingScreen()
    }
}
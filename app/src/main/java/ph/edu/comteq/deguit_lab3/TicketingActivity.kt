package ph.edu.comteq.deguit_lab3

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ph.edu.comteq.deguit_lab3.ui.theme.Deguit_Lab3Theme
import java.time.Instant
import java.time.Duration

class TicketingActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Deguit_Lab3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Ticketing()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ticketing() {
    var generalAdmissionCount by remember { mutableIntStateOf(1) }
    var freeTicketCount by remember { mutableIntStateOf(0) }

    val pricePerTicket = 500
    val totalPrice = generalAdmissionCount * pricePerTicket

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now()
            .plus(Duration.ofDays(2)).toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= Instant.now()
                    .plus(Duration.ofDays(1)).toEpochMilli()
            }
        }
    )

    Column(
        modifier = Modifier.background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.palace),
                    contentDescription = "Museum",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    contentScale = ContentScale.Crop
                )
                // Black overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                )
                Text(
                    "Official\nTicketing Service",
                    fontSize = 32.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp
                )
            }

            // Inner container for date and ticket types
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePicker(
                    modifier = Modifier
                        .padding(0.dp)
                        .fillMaxWidth(),
                    state = datePickerState,
                    title = null,
                    showModeToggle = false,
                    headline = {
                        Text(
                            "1. Date to Visit",
                            fontSize = 26.sp,
                            fontFamily = playfairdisplayregular
                        )
                    },
                    colors = DatePickerDefaults.colors(
                        titleContentColor = Color(0xFFD29F1B),
                        headlineContentColor = Color(0xFFD29F1B),
                        weekdayContentColor = Color(0xFFD29F1B),
                        containerColor = Color.Transparent,
                        dayContentColor = Color.White,
                        todayContentColor = Color(0xFFD29F1B),
                        todayDateBorderColor = Color(0xFFD29F1B),
                        selectedDayContainerColor = Color(0xFFD29F1B),
                        selectedDayContentColor = Color.Black,
                        disabledDayContentColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Number of Tickets Section
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "2. Number of Tickets",
                        fontSize = 26.sp,
                        fontFamily = playfairdisplayregular,
                        color = Color(0xFFD29F1B),
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // General Admission Ticket
                    TicketCounter(
                        title = "General Admission",
                        price = "P$pricePerTicket",
                        count = generalAdmissionCount,
                        onIncrement = { generalAdmissionCount++ },
                        onDecrement = { if (generalAdmissionCount > 0) generalAdmissionCount-- }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Free Ticket
                    TicketCounter(
                        title = "Under 18s, Under 26s\nresidents of the EEA,\nMuseum members,\nProfessionals",
                        price = "FREE",
                        count = freeTicketCount,
                        onIncrement = { freeTicketCount++ },
                        onDecrement = { if (freeTicketCount > 0) freeTicketCount-- },
                        isFree = true
                    )
                }
            }
        }

        // Bottom bar for totals
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFFD29F1B))
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total: P$totalPrice",
                fontSize = 26.sp,
                fontFamily = playfairdisplayregular,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
            Button(
                modifier = Modifier.padding(5.dp),
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    "Checkout",
                    fontSize = 20.sp,
                    fontFamily = playfairdisplayregular,
                    color = Color(0xFFD29F1B),
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun TicketCounter(
    title: String,
    price: String,
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    isFree: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                title,
                fontSize = 18.sp,
                color = Color.White,
                lineHeight = 22.sp
            )
            Text(
                price,
                fontSize = 20.sp,
                fontFamily = playfairdisplayregular,
                color = if (isFree) Color(0xFFD29F1B) else Color(0xFFD29F1B),
                fontWeight = FontWeight.Normal
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // Decrement button
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(50.dp)
                    .border(1.dp, Color(0xFFD29F1B), CircleShape)
            ) {
                Text(
                    "−",
                    fontSize = 24.sp,
                    color = Color(0xFFD29F1B)
                )
            }

            // Count
            Text(
                count.toString(),
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.widthIn(min = 30.dp),
                textAlign = TextAlign.Center
            )

            // Increment button
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(50.dp)
                    .border(1.dp, Color(0xFFD29F1B), CircleShape)
            ) {
                Text(
                    "+",
                    fontSize = 24.sp,
                    color = Color(0xFFD29F1B)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TicketingPreview() {
    Deguit_Lab3Theme {
        Ticketing()
    }
}
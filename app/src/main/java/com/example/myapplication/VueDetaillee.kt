package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class VueDetaillee : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ConsumptionScreen()
                }            }
        }
    }
}

val BackgroundPurple = Color(0xFF2D0A31)
val CardLightBlue = Color(0xFFA2E3E7)
val ButtonOrange = Color(0xFFF08060)
val StatusGreen = Color(0xFF82C17D)

@Composable
fun ConsumptionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundPurple)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nom en haut
        Text(
            text = "HUGO OGUH",
            style = TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            modifier = Modifier.padding(top = 40.dp, bottom = 40.dp)
        )

        // Carte principale
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CardLightBlue)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
               // Icon(
               //     contentDescription = null,
               //     modifier = Modifier.size(100.dp)
               // )

                // Textes d'information
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Nombre de consommation.s : XX", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Date et heure de la dernière consommation :\nXX/XX/XX à XXhXXmin",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // Statut SAM
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("STATUT : SAM", fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(StatusGreen, shape = CircleShape)
                    )
                }

                // Boutons
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CustomActionButton(text = "DÉCLARER")
                    Spacer(modifier = Modifier.height(12.dp))
                    CustomActionButton(text = "SIGNALER")
                }
            }
        }
    }
}

@Composable
fun CustomActionButton(text: String) {
    Button(
        onClick = { /* Action */ },
        colors = ButtonDefaults.buttonColors(containerColor = ButtonOrange),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(150.dp)
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
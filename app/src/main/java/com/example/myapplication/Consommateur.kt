package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.ui.theme.MyApplicationTheme

class Consommateur : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isSamView by rememberSaveable { mutableStateOf(false) }
            // Ici on défini les amis, il faudra aller les chercher dans la BD
            val friends = remember {
                mutableStateListOf(
                    FriendInfo(1, "HUGO OGUH", 2, true),
                    FriendInfo(2, "LISA MONA", 1, false),
                    FriendInfo(3, "JEAN NAEJ", 3, false),
                    FriendInfo(4, "PAUL TEST", 0, true)
                )
            }

            val navController = rememberNavController()

            MyApplicationTheme {
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        AppScreen(
                            isSamView = isSamView,
                            onSwitchChange = { isSamView = it },
                            friends = friends,
                            onFriendClick = { friend ->
                                navController.navigate("detail/${friend.id}")
                            }
                        )
                    }

                    composable("detail/{friendId}") { backStackEntry ->
                        val friendId = backStackEntry.arguments
                            ?.getString("friendId")
                            ?.toIntOrNull() ?: -1

                        val friend = friends.find { it.id == friendId }

                        if (friend != null) {
                            ConsumptionScreen(
                                friend = friend,
                                onBackClick = { navController.popBackStack() },
                                onDeclareClick = {
                                    friend.consumptionCount += 1
                                },
                                onToggleSamClick = {
                                    friend.isSafe = !friend.isSafe
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


/*
   DONNÉES
   On définis ici des données tels que friends. On pourrait aller les chercher en BD

    */

val ButtonOrange = Color(0xFFF08060)
val StatusGreen = Color(0xFF82C17D)

val GreenStatus = Color(0xFF10C95A)
data class UserInfo(
    val name: String,
    val consumptionCount: Int
)

data class FriendInfo(
    val id: Int,
    val name: String,
    var consumptionCount: Int,
    var isSafe: Boolean,
    // On pourrait mettre le genre (Homme/Femme) pour faire des calculs futurs, mais on ne fera pas de calcul ici, donc on ne le met pas
)

/*
   ÉCRAN PRINCIPAL
    */
@Composable
fun AppScreen(
    isSamView: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    friends: List<FriendInfo>,
    onFriendClick: (FriendInfo) -> Unit
) {
    if (isSamView) {
        SamView(
            isSamView = isSamView,
            onSwitchChange = onSwitchChange,
            friends = friends,
            onFriendClick = onFriendClick
        )
    } else {
        PasSamView(
            isSamView = isSamView,
            onSwitchChange = onSwitchChange,
            friends = friends,
            onFriendClick = onFriendClick
        )
    }
}
/*
   ÉCRAN NON SAM
    */
@Composable
fun PasSamView(
    isSamView: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    friends: List<FriendInfo>,
    onFriendClick: (FriendInfo) -> Unit
) {
    val me = UserInfo(
        name = "MOI",
        consumptionCount = 2
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                TopHeader(
                    isSamView = isSamView,
                    onSwitchChange = onSwitchChange,
                    onCallClick = { }
                )
            }

            item { SectionTitle("MOI") }

            item {
                MyCard(
                    userInfo = me,
                    onDeclareClick = { },
                    onReportClick = { }
                )
            }

            item { SectionTitle("MES AMIS") }

            items(friends, key = { it.id }) { friend ->
                FriendCard(
                    friend = friend,
                    onReportClick = { },
                    onFriendClick = { onFriendClick(friend) }
                )
            }
        }
    }
}

/*
   ÉCRAN SAM
    */
@Composable
fun SamView(
    isSamView: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    friends: List<FriendInfo>,
    onFriendClick: (FriendInfo) -> Unit
) {
    val me = UserInfo(
        name = "MOI",
        consumptionCount = 2
    )


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                TopHeader(
                    isSamView = isSamView,
                    onSwitchChange = onSwitchChange,
                    onCallClick = { }
                )
            }


            item {
                Text(
                    text = "Vous êtes déclarés comme “SAM”. Attention à votre consommation, vos amis comptent sur vous !",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            item { SectionTitle("MES AMIS") }

            items(friends) { friend ->
                FriendCard(
                    friend = friend,
                    onReportClick = { },
                    onFriendClick = { onFriendClick(friend) }
                )
            }
        }
    }
}

/*
   En tête (SAM OU PAS SAM)
    */
@Composable
fun TopHeader(
    isSamView: Boolean,
    onSwitchChange: (Boolean) -> Unit,
    onCallClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SamModeSwitch(
            checked = isSamView,
            onCheckedChange = onSwitchChange
        )

        Text(
            text = if (isSamView) "SAM" else "PAS SAM",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold
        )

        RoundedIconButton(
            containerColor = MaterialTheme.colorScheme.secondary,
            iconColor = Color.Black,
            onClick = onCallClick,
            iconContent = {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Appeler",
                    modifier = Modifier.size(34.dp)
                )
            }
        )
    }
}

@Composable
fun RoundedIconButton(
    containerColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    iconContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            androidx.compose.material3.LocalContentColor provides iconColor
            Box(contentAlignment = Alignment.Center) {
                iconContent()
            }
        }
    }
}

/*
   TITRESs
   */

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 30.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

/*
    Rubrique MOI
 */

@Composable
fun MyCard(
    userInfo: UserInfo,
    onDeclareClick: () -> Unit,
    onReportClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            Text(
                text = "Nombre de consommations : ${userInfo.consumptionCount}",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionPillButton(
                    text = "DÉCLARER",
                    onClick = onDeclareClick
                )

                ActionPillButton(
                    text = "SIGNALER",
                    onClick = onReportClick
                )
            }
        }
    }
}

/*
   Rubrique AMI
*/

@Composable
fun FriendCard(
    friend: FriendInfo,
    onReportClick: () -> Unit,
    onFriendClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.width(110.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .width(1.dp)
                        .background(Color(0x66000000))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Icône personne",
                    tint = Color.Black,
                    modifier = Modifier.size(72.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .width(1.dp)
                        .background(Color(0x66000000))
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = friend.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.clickable { onFriendClick() }
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Nombre de consommation.s : ${friend.consumptionCount}",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ActionPillButton(
                        text = "SIGNALER",
                        onClick = onReportClick
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    if (friend.isSafe) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(GreenStatus)
                        )
                    }
                }
            }
        }
    }
}

/*
   BOUTON PILULE
    */

@Composable
fun ActionPillButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp
        )
    }
}

/*
   SAM Switch
    */

val SamGreen = Color(0xFF10C95A)
val NonSamRed = Color(0xFFFF5A5F)
val SwitchThumb = Color(0xFFFFFFFF)
val SwitchTextDark = Color(0xFF1A1A1A)

@Composable
fun SamModeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 64.dp else 4.dp,
        animationSpec = tween(durationMillis = 220),
        label = "thumbOffset"
    )

    val backgroundColor = if (checked) SamGreen else NonSamRed

    Box(
        modifier = Modifier
            .width(120.dp)
            .height(64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(56.dp)
                .clip(CircleShape)
                .background(SwitchThumb)
                .align(Alignment.CenterStart)
        )


    }
}
/*
   ÉCRAN Detaille consomation
    */
@Composable
fun ConsumptionScreen(
    friend: FriendInfo,
    onBackClick: () -> Unit,
    onDeclareClick: () -> Unit,
    onToggleSamClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "← Retour",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
                .wrapContentWidth(Alignment.Start)
                .clickable { onBackClick() }
        )

        Text(
            text = friend.name,
            style = TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            ),
            modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Nombre de consommation.s : ${friend.consumptionCount}",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Date et heure de la dernière consommation :\nXX/XX/XX à XXhXXmin",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (friend.isSafe) "STATUT : SAM" else "STATUT : NON SAM",
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                if (friend.isSafe) StatusGreen else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CustomActionButton(
                        text = "DÉCLARER",
                        //Ici, l'affichage ne s'actualise pas automatique, il faut quitter la page et revenir.
                        onClick = onDeclareClick
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CustomActionButton(
                        text = if (friend.isSafe) "PASSER NON SAM" else "PASSER SAM",
                        //Ici, l'affichage ne s'actualise pas automatique, il faut quitter la page et revenir.
                        onClick = onToggleSamClick
                    )
                }
            }
        }
    }
}


@Composable
fun CustomActionButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = ButtonOrange),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(180.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}


/*
   PREVIEW
   */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppScreenPreview() {
    var isSamView by remember { mutableStateOf(false) }

    val friends = listOf(
        FriendInfo(1, "HUGO OGUH", 2, true),
        FriendInfo(2, "LISA MONA", 1, false),
        FriendInfo(3, "JEAN NAEJ", 3, false),
        FriendInfo(4, "PAUL TEST", 0, true)
    )

    MyApplicationTheme {
        AppScreen(
            isSamView = isSamView,
            onSwitchChange = { isSamView = it },
            friends = friends,
            onFriendClick = { }
        )
    }
}
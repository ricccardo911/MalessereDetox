package com.example.malesseredetox

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

data class RedFlag(
    val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val isUnlocked: Boolean
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        val sharedPref = getSharedPreferences("MalesserePrefs", Context.MODE_PRIVATE)

        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFFF4081),
                    background = Color(0xFF121212),
                    surface = Color(0xFF1E1E1E)
                )
            ) {
                MalessereApp(sharedPref)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "malessere_night_alerts",
                "Messaggi Tossici Notturni",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifiche simulate dell'Uomo Malessere alle 3 di notte"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }
}

fun sendToxicNotification(context: Context, petName: String) {
    val toxicMessages = listOf(
        "Sei sveglia? Stavo pensando a noi...",
        "Ho visto che eri online su IG 3 minuti fa.",
        "Scusa per ieri sera, ero strano.",
        "Ti va di fare un giro in SH? Passo sotto da te ora.",
        "Nessuna mi capisce come te comunque.",
        "Ascolta questa traccia, mi ha ricordato te."
    )
    val randomMsg = toxicMessages.random()

    val notification = NotificationCompat.Builder(context, "malessere_night_alerts")
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setContentTitle("03:42 • $petName")
        .setContentText(randomMsg)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    val manager = context.getSystemService(NotificationManager::class.java)
    manager?.notify(System.currentTimeMillis().toInt(), notification)
}

@Composable
fun MalessereAvatarCanvas(egoLevel: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "smoke")
    val smokeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "smokeAnim"
    )

    Canvas(modifier = Modifier.size(140.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        // Mullet Posteriore
        drawPath(
            path = Path().apply {
                moveTo(centerX - 42.dp.toPx(), centerY - 10.dp.toPx())
                lineTo(centerX - 50.dp.toPx(), centerY + 50.dp.toPx())
                lineTo(centerX + 50.dp.toPx(), centerY + 50.dp.toPx())
                lineTo(centerX + 42.dp.toPx(), centerY - 10.dp.toPx())
                close()
            },
            color = Color(0xFF1A1A1A)
        )

        // Viso / Base Testa
        drawCircle(
            color = Color(0xFFFFCC80),
            radius = 38.dp.toPx(),
            center = Offset(centerX, centerY)
        )

        // Catenina d'oro al collo
        drawArc(
            color = Color(0xFFFFD700),
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(centerX - 24.dp.toPx(), centerY + 18.dp.toPx()),
            size = Size(48.dp.toPx(), 26.dp.toPx()),
            style = Stroke(width = 3.dp.toPx())
        )

        // Occhiali da sole Cyber/Techno
        drawRoundRect(
            color = Color(0xFF0D0D0D),
            topLeft = Offset(centerX - 35.dp.toPx(), centerY - 10.dp.toPx()),
            size = Size(70.dp.toPx(), 18.dp.toPx()),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        // Riflesso lenti occhiali
        drawLine(
            color = if (egoLevel > 70) Color(0xFF00E5FF) else Color(0x66FFFFFF),
            start = Offset(centerX - 25.dp.toPx(), centerY - 6.dp.toPx()),
            end = Offset(centerX - 10.dp.toPx(), centerY + 4.dp.toPx()),
            strokeWidth = 2.dp.toPx()
        )

        // Espressione Bocca
        drawPath(
            path = Path().apply {
                if (egoLevel > 50) {
                    moveTo(centerX - 12.dp.toPx(), centerY + 18.dp.toPx())
                    quadraticTo(centerX, centerY + 24.dp.toPx(), centerX + 15.dp.toPx(), centerY + 14.dp.toPx())
                } else {
                    moveTo(centerX - 12.dp.toPx(), centerY + 20.dp.toPx())
                    quadraticTo(centerX, centerY + 15.dp.toPx(), centerX + 12.dp.toPx(), centerY + 20.dp.toPx())
                }
            },
            color = Color(0xFF5D4037),
            style = Stroke(width = 2.5.dp.toPx())
        )

        // Sigaretta / Vape
        if (egoLevel > 40) {
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(centerX + 12.dp.toPx(), centerY + 14.dp.toPx()),
                size = Size(18.dp.toPx(), 4.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.dp.toPx(), 1.dp.toPx())
            )
            drawCircle(
                color = Color(0xFFFF3D00),
                radius = 2.dp.toPx(),
                center = Offset(centerX + 30.dp.toPx(), centerY + 16.dp.toPx())
            )
            // Fumo animato
            drawCircle(
                color = Color(0x55E0E0E0),
                radius = 4.dp.toPx(),
                center = Offset(centerX + 33.dp.toPx(), centerY + 12.dp.toPx() + smokeOffset)
            )
        }
    }
}

@Composable
fun MalessereApp(prefs: android.content.SharedPreferences) {
    val context = LocalContext.current
    var currentTab by remember { mutableIntStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {}

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF1E1E1E)) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    label = { Text("Detox & Pet") },
                    icon = { Text("💔") }
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    label = { Text("Album Red Flags") },
                    icon = { Text("🚩") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF121212))
                .padding(padding)
        ) {
            if (currentTab == 0) {
                MainTrackerScreen(prefs, context)
            } else {
                RedFlagsScreen(prefs)
            }
        }
    }
}

@Composable
fun MainTrackerScreen(prefs: android.content.SharedPreferences, context: Context) {
    var lastResetTimestamp by remember {
        mutableLongStateOf(prefs.getLong("last_reset", System.currentTimeMillis()))
    }
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var activeMotivationalMessage by remember { mutableStateOf<String?>(null) }
    var petName by remember { mutableStateOf(prefs.getString("pet_name", "Kevin") ?: "Kevin") }
    var showNameDialog by remember { mutableStateOf(false) }
    var showBlockDialog by remember { mutableStateOf(false) }
    var egoLevel by remember { mutableIntStateOf(65) }
    var toxicAffection by remember { mutableIntStateOf(40) }
    var petStatusText by remember { mutableStateOf("Sta accelerando al semaforo con l'SH senza specchietti.") }

    val interactionCount = prefs.getInt("interaction_count", 0)

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = System.currentTimeMillis()
            delay(1000L)
        }
    }

    val elapsedSeconds = ((currentTime - lastResetTimestamp) / 1000).coerceAtLeast(0)
    val days = elapsedSeconds / 86400
    val hours = (elapsedSeconds % 86400) / 3600
    val minutes = (elapsedSeconds % 3600) / 60
    val seconds = elapsedSeconds % 60

    fun incrementInteractions() {
        prefs.edit().putInt("interaction_count", interactionCount + 1).apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "MALESSERE DETOX",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFF4081)
        )

        // Card Contatore
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tempo Sobria dal Malessere:", fontSize = 13.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = String.format("%02dd : %02dh : %02dm : %02ds", days, hours, minutes, seconds),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        val now = System.currentTimeMillis()
                        lastResetTimestamp = now
                        prefs.edit().putLong("last_reset", now).apply()
                        activeMotivationalMessage = "Sei ricascata nel tranello. Niente drammi, azzera il timer e ricomincia la disintossicazione."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("SONO RICASCACO/A! (RESET)")
                }
            }
        }

        // Tasto Rosso Emergenza: Blocco Contatto
        Button(
            onClick = { showBlockDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("🚨 BLOCCA SUBITO IL NUMERO (EMERGENZA)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        // Simulatore notifica
        OutlinedButton(
            onClick = { sendToxicNotification(context, petName) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFFB74D))
        ) {
            Text("Simula Notifica Tossica delle 03:42 💬")
        }

        // Card Tamagotchi
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF242424)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Cucciolo: $petName", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00E5FF))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("[Rinomina]", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.clickable { showNameDialog = true })
                }

                MalessereAvatarCanvas(egoLevel = egoLevel)

                Text(text = petStatusText, fontSize = 12.sp, textAlign = TextAlign.Center, color = Color.LightGray)

                Text("Ego & Sregolatezza: $egoLevel%", fontSize = 11.sp, color = Color.Gray)
                LinearProgressIndicator(progress = { egoLevel / 100f }, modifier = Modifier.fillMaxWidth(), color = Color(0xFFFF5252))

                Text("Dipendenza Emotiva: $toxicAffection%", fontSize = 11.sp, color = Color.Gray)
                LinearProgressIndicator(progress = { toxicAffection / 100f }, modifier = Modifier.fillMaxWidth(), color = Color(0xFFFF4081))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = {
                            egoLevel = (egoLevel + 10).coerceAtMost(100)
                            petStatusText = "Kebab cipolla e piccante divorato. Ti ha promesso che cambierà."
                            incrementInteractions()
                        },
                        modifier = Modifier.weight(1f).padding(2.dp)
                    ) {
                        Text("Nutri", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            egoLevel = (egoLevel + 15).coerceAtMost(100)
                            petStatusText = "Svapa guardando l'orizzonte: 'Tu per me sei casa, però non posso legarmi'."
                            incrementInteractions()
                        },
                        modifier = Modifier.weight(1f).padding(2.dp)
                    ) {
                        Text("Sigaretta", fontSize = 11.sp)
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = {
                            egoLevel = (egoLevel - 15).coerceAtLeast(0)
                            toxicAffection = (toxicAffection - 10).coerceAtLeast(0)
                            petStatusText = "Gli hai chiesto stabilità emotiva. Ha guardato il telefono ed è fuggito."
                            incrementInteractions()
                        },
                        modifier = Modifier.weight(1f).padding(2.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2))
                    ) {
                        Text("Saggio Consiglio", fontSize = 10.sp)
                    }

                    Button(
                        onClick = {
                            if (toxicAffection <= 20) {
                                petStatusText = "VITTORIA! È partito a tutto gas verso altri lidi. SEI LIBERA!"
                                egoLevel = 50
                                toxicAffection = 0
                            } else {
                                petStatusText = "Hai provato a respingerlo ma ti ha mandato una nota audio acustica. Sei ricascata."
                                toxicAffection = (toxicAffection + 15).coerceAtMost(100)
                            }
                            incrementInteractions()
                        },
                        modifier = Modifier.weight(1f).padding(2.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                    ) {
                        Text("Lascialo Andare", fontSize = 10.sp)
                    }
                }
            }
        }
    }

    if (showBlockDialog) {
        AlertDialog(
            onDismissRequest = { showBlockDialog = false },
            title = { Text("⚠️ PROTOCOLLO SALVA-DIGNITÀ") },
            text = {
                Text("Stai per essere reindirizzata alla tua rubrica per bloccare ed eliminare definitivamente il contatto di $petName prima del prossimo messaggio notturno.\n\nSei pronta a fare la scelta giusta?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBlockDialog = false
                        val intent = Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Portami in Rubrica ORA")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockDialog = false }) {
                    Text("Fammi soffrire ancora")
                }
            }
        )
    }

    if (showNameDialog) {
        var tempName by remember { mutableStateOf(petName) }
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            title = { Text("Battezza il tuo Malessere") },
            text = {
                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    label = { Text("Nome (es. Kevin, Manuel)") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(onClick = {
                    petName = tempName
                    prefs.edit().putString("pet_name", tempName).apply()
                    showNameDialog = false
                }) { Text("Salva") }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) { Text("Annulla") }
            }
        )
    }

    activeMotivationalMessage?.let { msg ->
        Dialog(onDismissRequest = { activeMotivationalMessage = null }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xE6101010))
                    .clickable { activeMotivationalMessage = null }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("MEMENTO MALESSERE", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5252))
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = msg, fontSize = 20.sp, color = Color.White, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("(Tocca per chiudere)", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun RedFlagsScreen(prefs: android.content.SharedPreferences) {
    val interactionCount = prefs.getInt("interaction_count", 0)
    var selectedFlag by remember { mutableStateOf<RedFlag?>(null) }

    val flags = remember(interactionCount) {
        listOf(
            RedFlag(1, "L'SH e la Targa Inclinata", "Livello 1", "Si presenta al primo appuntamento con lo scooter che fa rumore da jet e senza casco di riserva.", interactionCount >= 1),
            RedFlag(2, "Il 'Non Voglio Etichette'", "Livello 2", "Vuole l'esclusività ma senza l'impegno formale, così può flirtare legalmente alle feste techno.", interactionCount >= 3),
            RedFlag(3, "Online ma non Risponde", "Livello 3", "Visualizzato alle 14:15, risposta alle 02:30 con 'scusa ero presissimo'.", interactionCount >= 6),
            RedFlag(4, "La Sua Ex 'Era Pazza'", "Livello 4", "Tutte le ragazze della sua vita precedente erano inspiegabilmente isteriche secondo la sua versione.", interactionCount >= 10),
            RedFlag(5, "L'Audio da 7 Minuti alle 4 AM", "Livello 5", "Monologo sgrammaticato sotto cassa in cui ti dice che solo tu lo capisci davvero nel profondo.", interactionCount >= 15),
            RedFlag(6, "La Chiamata 'Mi Trovo Sotto Casa'", "Livello BOSS", "Comparsa improvvisa a sorpresa dopo due settimane di ghosting totale.", interactionCount >= 20)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "GALLERIA RED FLAGS 🚩",
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFFFF5252)
        )
        Text(
            text = "Interagisci col Tamagotchi per sbloccare tutti i trofei del dramma relazionale.",
            fontSize = 12.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(vertical = 6.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize().padding(top = 8.dp)
        ) {
            items(flags) { flag ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (flag.isUnlocked) Color(0xFF2C1E21) else Color(0xFF1E1E1E)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clickable { if (flag.isUnlocked) selectedFlag = flag }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (flag.isUnlocked) "🚩 SBLOCCATA" else "🔒 BLOCCATA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (flag.isUnlocked) Color(0xFFFF5252) else Color.Gray
                        )
                        Text(
                            text = if (flag.isUnlocked) flag.title else "???",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (flag.isUnlocked) Color.White else Color.DarkGray
                        )
                        Text(
                            text = flag.subtitle,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }

    selectedFlag?.let { flag ->
        AlertDialog(
            onDismissRequest = { selectedFlag = null },
            title = { Text("🚩 ${flag.title}") },
            text = { Text(flag.description, fontSize = 14.sp) },
            confirmButton = {
                Button(onClick = { selectedFlag = null }) {
                    Text("Preso Atto")
                }
            }
        )
    }
}

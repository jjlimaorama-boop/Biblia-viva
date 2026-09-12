package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BiblicalCharacter
import com.example.data.model.PrayerRequest
import com.example.data.model.StudyTopic
import com.example.ui.BibleViewModel
import com.example.ui.NavTab
import com.example.ui.theme.*

data class ReadingPlanItem(
    val id: String,
    val title: String,
    val description: String,
    val totalDays: Int,
    var completedDays: Int,
    val accentColor: Color,
    val todayPassage: String,
    val targetBookId: String,
    val targetChapter: Int,
    val dailySchedule: List<String>
)

data class DiscipleshipTrackItem(
    val id: String,
    val title: String,
    val description: String,
    val lessons: List<DiscipleshipLessonItem>
)

data class DiscipleshipLessonItem(
    val step: Int,
    val title: String,
    val passage: String,
    val bookId: String,
    val chapter: Int,
    val keyTruth: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyHubScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTabIdx by remember { mutableStateOf(0) }
    val tabTitles = listOf("Temas", "Personagens", "Planos", "Oração", "Discipulado")

    val topics by viewModel.topics.collectAsState()
    val characters by viewModel.characters.collectAsState()
    val prayers by viewModel.prayers.collectAsState()

    var activeTopicDetail by remember { mutableStateOf<StudyTopic?>(null) }
    var activeCharacterDetail by remember { mutableStateOf<BiblicalCharacter?>(null) }
    var activePlanDetail by remember { mutableStateOf<ReadingPlanItem?>(null) }
    var activeTrackDetail by remember { mutableStateOf<DiscipleshipTrackItem?>(null) }
    var showNewPrayerDialog by remember { mutableStateOf(false) }

    // Pre-configured Reading Plans
    var plansList by remember {
        mutableStateOf(
            listOf(
                ReadingPlanItem(
                    id = "evangelhos",
                    title = "Os Evangelhos em 30 Dias",
                    description = "Uma jornada profunda pela vida, ministério, morte e ressurreição de Cristo nos 4 evangelhos.",
                    totalDays = 30,
                    completedDays = 14,
                    accentColor = VividBlue,
                    todayPassage = "João 3",
                    targetBookId = "JHN",
                    targetChapter = 3,
                    dailySchedule = listOf(
                        "Dia 1: João 1 - O Verbo se fez carne",
                        "Dia 2: João 2-3 - O Novo Nascimento e Nicodemos",
                        "Dia 3: João 4-5 - A Samaritana e o Paralítico de Betesda",
                        "Dia 4: João 6 - O Pão da Vida",
                        "Dia 5: Marcos 1-2 - O Início do Ministério",
                        "Dia 6: Mateus 5-7 - O Sermão da Montanha",
                        "Dia 7: Lucas 15 - As Parábolas da Misericórdia",
                        "Dia 8: João 14-16 - O Consolador e a Videira Verdadeira",
                        "Dia 9: João 19-20 - Morte e Ressurreição de Jesus"
                    )
                ),
                ReadingPlanItem(
                    id = "biblia_anual",
                    title = "A Bíblia Completa em 1 Ano",
                    description = "Leituras diárias balanceadas do Antigo e Novo Testamento com Salmos e Provérbios.",
                    totalDays = 365,
                    completedDays = 62,
                    accentColor = VividEmerald,
                    todayPassage = "Gênesis 1",
                    targetBookId = "GEN",
                    targetChapter = 1,
                    dailySchedule = listOf(
                        "Dia 1: Gênesis 1-3 & Mateus 1",
                        "Dia 2: Gênesis 4-6 & Mateus 2",
                        "Dia 3: Gênesis 7-9 & Salmos 1",
                        "Dia 4: Gênesis 10-12 & Mateus 3",
                        "Dia 5: Gênesis 13-15 & Provérbios 1"
                    )
                ),
                ReadingPlanItem(
                    id = "proverbios",
                    title = "Sabedoria em 31 Dias: Provérbios",
                    description = "Um capítulo de Provérbios para cada dia do mês, aplicando a sabedoria cristocêntrica.",
                    totalDays = 31,
                    completedDays = 8,
                    accentColor = VividAmber,
                    todayPassage = "Provérbios 1",
                    targetBookId = "PRO",
                    targetChapter = 1,
                    dailySchedule = (1..31).map { "Dia $it: Provérbios $it — Princípios para o viver sábio" }
                ),
                ReadingPlanItem(
                    id = "novo_convertido",
                    title = "Fundamentos para o Novo Convertido (14 Dias)",
                    description = "Passos essenciais sobre arrependimento, fé, segurança em Cristo e testemunho.",
                    totalDays = 14,
                    completedDays = 14,
                    accentColor = VividRose,
                    todayPassage = "João 1",
                    targetBookId = "JHN",
                    targetChapter = 1,
                    dailySchedule = listOf(
                        "Dia 1: João 1:1-18 - A Luz do Mundo",
                        "Dia 2: João 3:1-21 - O Novo Nascimento",
                        "Dia 3: Romanos 8:1-17 - Nenhuma Condenação",
                        "Dia 4: Efésios 2:1-10 - Salvos Pela Graça",
                        "Dia 5: Filipenses 4:4-13 - A Paz de Deus",
                        "Dia 6: Salmos 23 - O Senhor é o meu Pastor",
                        "Dia 7: 1 João 1:5-2:2 - Perdão e Comunhão"
                    )
                )
            )
        )
    }

    // Pre-configured Discipleship Tracks
    val tracks = remember {
        listOf(
            DiscipleshipTrackItem(
                id = "t1",
                title = "Trilha 1: Novo Convertido",
                description = "Fundamentos da salvação, certeza da vida eterna e primeiros passos na fé cristã.",
                lessons = listOf(
                    DiscipleshipLessonItem(1, "O Que Aconteceu Comigo?", "João 1:12", "JHN", 1, "Pela fé em Cristo, você foi feito filho de Deus e adotado na família do Pai."),
                    DiscipleshipLessonItem(2, "A Certeza da Salvação", "1 João 5:11-13", "1JN", 5, "A salvação repousa na promessa infalível de Deus e na cruz de Cristo, não em sentimentos."),
                    DiscipleshipLessonItem(3, "Como Conversar com Deus", "Mateus 6:9-13", "MAT", 6, "A oração é a conversa sincera do filho com o Pai Celeste."),
                    DiscipleshipLessonItem(4, "Alimentando o Espírito com a Palavra", "Salmos 119:105", "PSA", 119, "A Bíblia é lâmpada para os pés e alimento diário para a alma.")
                )
            ),
            DiscipleshipTrackItem(
                id = "t2",
                title = "Trilha 2: Fundamentos da Fé Reformada",
                description = "A soberania divina, autoridade das Escrituras, a cruz de Cristo e a habitação do Espírito.",
                lessons = listOf(
                    DiscipleshipLessonItem(1, "A Autoridade da Bíblia", "2 Timóteo 3:16-17", "2TI", 3, "Toda a Escritura é divinamente inspirada e suficiente para a salvação e santificação."),
                    DiscipleshipLessonItem(2, "A Graça Soberana", "Efésios 2:8-10", "EPH", 2, "Não fomos salvos por méritos próprios, mas unicamente pelo favor gracioso do Altíssimo."),
                    DiscipleshipLessonItem(3, "A Centralidade da Cruz", "Gálatas 2:20", "GAL", 2, "Fomos crucificados com Cristo; já não vivemos nós, mas Cristo vive em nós."),
                    DiscipleshipLessonItem(4, "O Fruto do Espírito", "Gálatas 5:22-26", "GAL", 5, "O Espírito opera o caráter e o amor de Cristo na caminhada do crente.")
                )
            ),
            DiscipleshipTrackItem(
                id = "t3",
                title = "Trilha 3: A Vida de Oração & Intimidade",
                description = "Como orar com perseverança, reverência e segundo a vontade revelada de Deus.",
                lessons = listOf(
                    DiscipleshipLessonItem(1, "Oração no Quarto Secreto", "Mateus 6:6", "MAT", 6, "A oração genuína nasce no silêncio da busca sincera pelo Pai."),
                    DiscipleshipLessonItem(2, "Intercessão e Ações de Graças", "Filipenses 4:6-7", "PHP", 4, "Apresentar a Deus todas as petições com súplicas e ações de graças dissipa a ansiedade."),
                    DiscipleshipLessonItem(3, "Orando as Escrituras", "Salmos 23:1", "PSA", 23, "Usar os salmos e promessas bíblicas como molde para nossas conversas com Deus.")
                )
            ),
            DiscipleshipTrackItem(
                id = "t4",
                title = "Trilha 4: Igreja, Comunhão e Missão",
                description = "O valor da membresia local, o discipulado bíblico mútuo e o testemunho de Cristo ao mundo.",
                lessons = listOf(
                    DiscipleshipLessonItem(1, "O Corpo de Cristo", "1 Coríntios 12:12-27", "1CO", 12, "Cada crente é membro indispensável do corpo da Igreja local."),
                    DiscipleshipLessonItem(2, "O Grande Mandamento e a Grande Comissão", "Mateus 28:18-20", "MAT", 28, "Fazer discípulos de todas as nações, batizando e ensinando a guardar os mandamentos de Jesus.")
                )
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("study_hub_screen")
    ) {
        // Hub Top Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Centro de Estudos Bíblicos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Aprofunde o conhecimento das Escrituras com foco na Pessoa de Jesus.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedTabIdx,
                    edgePadding = 0.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIdx]),
                            color = VividIndigo,
                            height = 3.dp
                        )
                    }
                ) {
                    tabTitles.forEachIndexed { idx, title ->
                        Tab(
                            selected = selectedTabIdx == idx,
                            onClick = { selectedTabIdx = idx },
                            selectedContentColor = VividIndigo,
                            unselectedContentColor = SubtitleText,
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTabIdx == idx) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                    }
                }
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when (selectedTabIdx) {
                0 -> TopicsTab(
                    topics = topics,
                    onSelect = { activeTopicDetail = it }
                )
                1 -> CharactersTab(
                    characters = characters,
                    onSelect = { activeCharacterDetail = it }
                )
                2 -> ReadingPlansTab(
                    plans = plansList,
                    onSelectPlan = { activePlanDetail = it },
                    onStartReading = { bookId, chapter ->
                        viewModel.selectBookAndChapter(bookId, chapter)
                        viewModel.setTab(NavTab.BIBLE)
                    }
                )
                3 -> PrayersTab(
                    prayers = prayers,
                    onToggleAnswered = { viewModel.togglePrayerAnswered(it) },
                    onDelete = { viewModel.deletePrayer(it) },
                    onAddClick = { showNewPrayerDialog = true }
                )
                4 -> DiscipleshipTab(
                    tracks = tracks,
                    onSelectTrack = { activeTrackDetail = it }
                )
            }
        }

        // Topic Detail Modal
        if (activeTopicDetail != null) {
            TopicDetailDialog(
                topic = activeTopicDetail!!,
                onDismiss = { activeTopicDetail = null },
                onReadInBible = {
                    viewModel.selectBookAndChapter("JHN", 3)
                    viewModel.setTab(NavTab.BIBLE)
                    activeTopicDetail = null
                }
            )
        }

        // Character Detail Modal
        if (activeCharacterDetail != null) {
            CharacterDetailDialog(
                character = activeCharacterDetail!!,
                onDismiss = { activeCharacterDetail = null }
            )
        }

        // Plan Detail Modal
        if (activePlanDetail != null) {
            PlanDetailDialog(
                plan = activePlanDetail!!,
                onDismiss = { activePlanDetail = null },
                onReadPassage = { bookId, chapter ->
                    viewModel.selectBookAndChapter(bookId, chapter)
                    viewModel.setTab(NavTab.BIBLE)
                    activePlanDetail = null
                },
                onToggleDayCompleted = {
                    val updated = plansList.map { p ->
                        if (p.id == activePlanDetail!!.id) {
                            val newCompleted = if (p.completedDays < p.totalDays) p.completedDays + 1 else p.completedDays
                            p.copy(completedDays = newCompleted)
                        } else p
                    }
                    plansList = updated
                    activePlanDetail = updated.find { it.id == activePlanDetail!!.id }
                }
            )
        }

        // Discipleship Track Detail Modal
        if (activeTrackDetail != null) {
            DiscipleshipTrackDialog(
                track = activeTrackDetail!!,
                onDismiss = { activeTrackDetail = null },
                onReadPassage = { bookId, chapter ->
                    viewModel.selectBookAndChapter(bookId, chapter)
                    viewModel.setTab(NavTab.BIBLE)
                    activeTrackDetail = null
                }
            )
        }

        // New Prayer Dialog
        if (showNewPrayerDialog) {
            NewPrayerDialog(
                onDismiss = { showNewPrayerDialog = false },
                onSave = { title, desc, cat ->
                    viewModel.savePrayer(title, desc, cat)
                    showNewPrayerDialog = false
                }
            )
        }
    }
}

@Composable
fun TopicsTab(
    topics: List<StudyTopic>,
    onSelect: (StudyTopic) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = remember(topics, searchQuery) {
        if (searchQuery.isBlank()) topics
        else topics.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.definition.contains(searchQuery, ignoreCase = true) ||
            it.christConnection.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Pesquisar temas (Fé, Graça, Oração...)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpar")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Temas Teológicos Canônicos (${filtered.size})",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum tema encontrado para '$searchQuery'.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { topic ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(topic) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = topic.title,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = SubtitleText
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = topic.definition,
                                fontSize = 13.sp,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                color = Color(0xFFFFFBEB),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFFDE68A))
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Cristo: ${topic.christConnection.take(70)}...",
                                        fontSize = 11.sp,
                                        color = Color(0xFF92400E),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharactersTab(
    characters: List<BiblicalCharacter>,
    onSelect: (BiblicalCharacter) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = remember(characters, searchQuery) {
        if (searchQuery.isBlank()) characters
        else characters.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.period.contains(searchQuery, ignoreCase = true) ||
            it.biography.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Pesquisar personagens (Abraão, Moisés, Davi...)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpar")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Grandes Personagens e Tipologia (${filtered.size})",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum personagem encontrado para '$searchQuery'.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { character ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(character) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = character.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = character.period,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = character.biography,
                                fontSize = 13.sp,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "✝️ Redenção: ${character.redemptiveRole}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingPlansTab(
    plans: List<ReadingPlanItem>,
    onSelectPlan: (ReadingPlanItem) -> Unit,
    onStartReading: (String, Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(plans) { plan ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectPlan(plan) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = plan.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${plan.completedDays}/${plan.totalDays} dias",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = plan.accentColor
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = plan.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val progress = (plan.completedDays.toFloat() / plan.totalDays).coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = plan.accentColor,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Passagem de hoje: ${plan.todayPassage}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GoldAccent
                        )
                        Button(
                            onClick = { onStartReading(plan.targetBookId, plan.targetChapter) },
                            colors = ButtonDefaults.buttonColors(containerColor = plan.accentColor),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ler Hoje", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PrayersTab(
    prayers: List<PrayerRequest>,
    onToggleAnswered: (PrayerRequest) -> Unit,
    onDelete: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(0) }
    val filters = listOf("Todas", "Em Oração", "Respondidas")

    val filteredPrayers = remember(prayers, selectedFilter) {
        when (selectedFilter) {
            1 -> prayers.filter { !it.isAnswered }
            2 -> prayers.filter { it.isAnswered }
            else -> prayers
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp)
    ) {
        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Novo Pedido de Oração", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEachIndexed { idx, label ->
                FilterChip(
                    selected = selectedFilter == idx,
                    onClick = { selectedFilter = idx },
                    label = { Text(label, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredPrayers.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhum pedido de oração encontrado nesta categoria.\nToque no botão acima para registrar suas orações ao Senhor.",
                    fontSize = 13.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredPrayers) { prayer ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Checkbox(
                                        checked = prayer.isAnswered,
                                        onCheckedChange = { onToggleAnswered(prayer) }
                                    )
                                    Column {
                                        Text(
                                            text = prayer.title,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = prayer.category,
                                            fontSize = 11.sp,
                                            color = GoldAccent,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                IconButton(onClick = { onDelete(prayer.id) }) {
                                    Icon(Icons.Outlined.Delete, contentDescription = "Excluir", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                }
                            }

                            if (prayer.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = prayer.description,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(start = 48.dp)
                                )
                            }

                            if (prayer.isAnswered) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    color = Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.padding(start = 48.dp)
                                ) {
                                    Text(
                                        text = "✓ Oração Respondida pelo Senhor!",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DiscipleshipTab(
    tracks: List<DiscipleshipTrackItem>,
    onSelectTrack: (DiscipleshipTrackItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(tracks) { track ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectTrack(track) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = track.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${track.lessons.size} lições",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = track.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Toque para abrir lições e passagens",
                            fontSize = 11.sp,
                            color = GoldAccent,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlanDetailDialog(
    plan: ReadingPlanItem,
    onDismiss: () -> Unit,
    onReadPassage: (String, Int) -> Unit,
    onToggleDayCompleted: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = plan.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Text(
                    text = plan.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                val progress = (plan.completedDays.toFloat() / plan.totalDays).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = plan.accentColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Text(
                    text = "${plan.completedDays} de ${plan.totalDays} dias completados (${(progress * 100).toInt()}%)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = plan.accentColor,
                    modifier = Modifier.padding(top = 4.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Text(
                    text = "Cronograma de Leituras:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(plan.dailySchedule) { index, schedule ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (index < plan.completedDays) "✓ " else "• ",
                                color = if (index < plan.completedDays) Color(0xFF166534) else Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = schedule,
                                fontSize = 12.sp,
                                color = if (index < plan.completedDays) Color(0xFF166534) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onToggleDayCompleted()
                            Toast.makeText(context, "Progresso do plano atualizado!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Avançar 1 Dia", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { onReadPassage(plan.targetBookId, plan.targetChapter) },
                        colors = ButtonDefaults.buttonColors(containerColor = plan.accentColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Ler Hoje", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DiscipleshipTrackDialog(
    track: DiscipleshipTrackItem,
    onDismiss: () -> Unit,
    onReadPassage: (String, Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = track.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Text(
                    text = track.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Text(
                    text = "Lições Bíblicas Estruturadas:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(track.lessons) { lesson ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${lesson.step}. ${lesson.title}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Button(
                                        onClick = { onReadPassage(lesson.bookId, lesson.chapter) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(lesson.passage, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lesson.keyTruth,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Concluir Estudo")
                }
            }
        }
    }
}

@Composable
fun TopicDetailDialog(
    topic: StudyTopic,
    onDismiss: () -> Unit,
    onReadInBible: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = topic.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Text("Definição Bíblica:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(topic.definition, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Passagens Chave:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(topic.mainScriptures, fontSize = 13.sp, color = GoldAccent)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("✝️ Conexão Cristocêntrica:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(topic.christConnection, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Aplicação Prática:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(topic.practicalApplications, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text("Perguntas para Reflexão:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(topic.studyQuestions, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = onReadInBible, modifier = Modifier.weight(1f)) {
                        Text("Ler na Bíblia", fontSize = 12.sp)
                    }
                    Button(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Concluído", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterDetailDialog(
    character: BiblicalCharacter,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = character.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = character.period,
                            fontSize = 11.sp,
                            color = GoldAccent
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Text("Biografia:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(character.biography, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Principais Eventos:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(character.keyEvents, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Virtudes e Falhas:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Virtudes: ${character.virtues}", fontSize = 13.sp, color = Color(0xFF166534))
                        Text("Falhas: ${character.flaws}", fontSize = 13.sp, color = Color(0xFF991B1B))
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("✝️ Papel Redentivo e Cristo:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(character.redemptiveRole, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Passagens Relacionadas:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(character.relatedVerses, fontSize = 13.sp, color = GoldAccent)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Lições para Nós:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(character.lessons, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Concluído")
                }
            }
        }
    }
}

@Composable
fun NewPrayerDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Família") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Registrar Pedido de Oração",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Motivo da Oração") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detalhes / Pedido específico") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Categoria:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Família", "Igreja", "Saúde", "Missões").forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(title, description, category)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Salvar Oração")
                    }
                }
            }
        }
    }
}

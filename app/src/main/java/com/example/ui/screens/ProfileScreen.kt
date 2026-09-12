package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.Favorite
import com.example.data.model.Note
import com.example.data.model.UserSettings
import com.example.ui.BibleViewModel
import com.example.ui.NavTab
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val userSettings by viewModel.userSettings.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val sermons by viewModel.sermons.collectAsState()
    val context = LocalContext.current

    var selectedSection by remember { mutableStateOf(0) }
    val sectionTabs = listOf(
        Triple("Favoritos", Icons.Default.Star, "profile_tab_0"),
        Triple("Anotações", Icons.Default.EditNote, "profile_tab_1"),
        Triple("Plano Pro", Icons.Default.WorkspacePremium, "profile_tab_2"),
        Triple("Liderança", Icons.Default.School, "profile_tab_3"),
        Triple("Ajustes", Icons.Default.Settings, "profile_tab_4")
    )

    var showEditNameDialog by remember { mutableStateOf(false) }
    var showStreakDialog by remember { mutableStateOf(false) }
    var showNewNoteDialog by remember { mutableStateOf(false) }
    var newNoteCategoryPreset by remember { mutableStateOf("Estudos") }
    var activeNoteDetail by remember { mutableStateOf<Note?>(null) }
    var showDoctrineDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen")
    ) {
        // Profile Header
        Surface(
            color = NavyPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // User info card row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showEditNameDialog = true }
                            .padding(vertical = 4.dp)
                            .testTag("btn_profile_edit_name_card")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(GoldAccent),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Avatar do Estudante",
                                tint = NavyDark,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = userSettings.userName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Outlined.Edit,
                                    contentDescription = "Editar Nome",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = if (userSettings.isPremium) "Membro Bíblia Viva Pro 💎" else "Estudante das Sagradas Escrituras",
                                fontSize = 12.sp,
                                color = GoldAccent,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Pro Toggle Badge
                    Surface(
                        color = if (userSettings.isPremium) Color(0xFF166534) else Color(0x33FFFFFF),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.togglePremium()
                                selectedSection = 2 // Navigate to Plano Pro
                                Toast.makeText(
                                    context,
                                    if (!userSettings.isPremium) "Recursos Bíblia Viva Pro Ativados! 💎" else "Modo Pro desativado.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .testTag("btn_header_pro_toggle")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diamond,
                                contentDescription = null,
                                tint = if (userSettings.isPremium) Color.White else GoldAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (userSettings.isPremium) "PRO ATIVO" else "OBTER PRO",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (userSettings.isPremium) Color.White else GoldAccent
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive Stats Badges Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InteractiveStatBadge(
                        label = "Sequência",
                        value = "🔥 ${userSettings.readingStreakDays}d",
                        sublabel = "Toque p/ ver",
                        modifier = Modifier.weight(1f),
                        testTag = "badge_streak",
                        onClick = { showStreakDialog = true }
                    )
                    InteractiveStatBadge(
                        label = "Capítulos",
                        value = "📖 ${userSettings.totalChaptersRead}",
                        sublabel = "Ler mais",
                        modifier = Modifier.weight(1f),
                        testTag = "badge_chapters",
                        onClick = { viewModel.setTab(NavTab.BIBLE) }
                    )
                    InteractiveStatBadge(
                        label = "Favoritos",
                        value = "⭐ ${favorites.size}",
                        sublabel = "Ver versos",
                        modifier = Modifier.weight(1f),
                        testTag = "badge_favorites",
                        onClick = { selectedSection = 0 }
                    )
                    InteractiveStatBadge(
                        label = "Notas",
                        value = "📝 ${notes.size}",
                        sublabel = "Caderno",
                        modifier = Modifier.weight(1f),
                        testTag = "badge_notes",
                        onClick = { selectedSection = 1 }
                    )
                }
            }
        }

        // Sub Navigation Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedSection,
            edgePadding = 12.dp,
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) },
            modifier = Modifier.fillMaxWidth()
        ) {
            sectionTabs.forEachIndexed { index, (title, icon, tag) ->
                Tab(
                    selected = selectedSection == index,
                    onClick = { selectedSection = index },
                    modifier = Modifier.testTag(tag),
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (selectedSection == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedSection == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedSection == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }

        // Content Area using weight(1f) to guarantee responsiveness and avoid layout clipping
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            when (selectedSection) {
                0 -> FavoritesSection(
                    favorites = favorites,
                    onGoToVerse = { bookId, chapter ->
                        viewModel.selectBookAndChapter(bookId, chapter)
                        viewModel.setTab(NavTab.BIBLE)
                    },
                    onExploreBible = {
                        viewModel.setTab(NavTab.BIBLE)
                    },
                    onAddSampleFavorite = {
                        viewModel.addExampleFavorite()
                        Toast.makeText(context, "Versículo exemplo adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
                    },
                    onDeleteFavorite = { id ->
                        viewModel.deleteFavorite(id)
                        Toast.makeText(context, "Versículo removido dos favoritos.", Toast.LENGTH_SHORT).show()
                    }
                )
                1 -> NotesSection(
                    notes = notes,
                    onNewNoteClick = {
                        newNoteCategoryPreset = "Estudos"
                        showNewNoteDialog = true
                    },
                    onNoteClick = { activeNoteDetail = it },
                    onDelete = { id ->
                        viewModel.deleteNote(id)
                        Toast.makeText(context, "Anotação excluída.", Toast.LENGTH_SHORT).show()
                    }
                )
                2 -> PremiumSection(
                    isPremium = userSettings.isPremium,
                    onTogglePremium = {
                        viewModel.togglePremium()
                        Toast.makeText(
                            context,
                            if (!userSettings.isPremium) "Parabéns! Recursos Pro Ativados 💎" else "Modo Pro desativado.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onOpenAiAssistant = { viewModel.setTab(NavTab.AI_TOOLS) },
                    onOpenSermonMaker = { viewModel.setTab(NavTab.AI_TOOLS) },
                    onPlayAudio = {
                        viewModel.playCurrentChapter()
                        Toast.makeText(context, "Reproduzindo áudio bíblico...", Toast.LENGTH_SHORT).show()
                    },
                    onOpenStudyHub = { viewModel.setTab(NavTab.STUDY) },
                    onOpenNewNote = {
                        newNoteCategoryPreset = "Estudos"
                        showNewNoteDialog = true
                    }
                )
                3 -> AdminLeadershipSection(
                    sermonsCount = sermons.size,
                    notesCount = notes.size,
                    chaptersRead = userSettings.totalChaptersRead,
                    onPrepareSermon = { viewModel.setTab(NavTab.AI_TOOLS) },
                    onStudyHub = { viewModel.setTab(NavTab.STUDY) },
                    onNewNote = {
                        newNoteCategoryPreset = "Sermões"
                        showNewNoteDialog = true
                    },
                    onShowDoctrines = { showDoctrineDialog = true }
                )
                4 -> SettingsSection(
                    userSettings = userSettings,
                    onFontSizeChange = { viewModel.updateFontSizeSp(it) },
                    onVersionChange = {
                        viewModel.selectVersion(it)
                        Toast.makeText(context, "Versão alterada para $it", Toast.LENGTH_SHORT).show()
                    },
                    onGoalChange = {
                        viewModel.updateReadingGoal(it)
                        Toast.makeText(context, "Meta atualizada para $it min/dia", Toast.LENGTH_SHORT).show()
                    },
                    onThemeChange = {
                        viewModel.setReadingTheme(it)
                        Toast.makeText(context, "Tema de leitura atualizado!", Toast.LENGTH_SHORT).show()
                    },
                    onEditNameClick = { showEditNameDialog = true },
                    onResetDefaults = {
                        viewModel.resetSettingsToDefault()
                        Toast.makeText(context, "Configurações restauradas para o padrão.", Toast.LENGTH_SHORT).show()
                    },
                    onShowAbout = { showAboutDialog = true }
                )
            }
        }
    }

    // Edit Name Dialog
    if (showEditNameDialog) {
        var newName by remember(userSettings.userName) { mutableStateOf(userSettings.userName) }
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nome do Estudante")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Personalize o nome que identifica seu perfil na Bíblia Viva:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        singleLine = true,
                        label = { Text("Seu Nome / Título") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_edit_name")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Estudante da Palavra", "Pastor", "Líder de Estudo").forEach { preset ->
                            SuggestionChip(
                                onClick = { newName = preset },
                                label = { Text(preset, fontSize = 11.sp) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.updateUserName(newName)
                            showEditNameDialog = false
                            Toast.makeText(context, "Nome atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.testTag("btn_save_user_name")
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Reading Streak Dialog
    if (showStreakDialog) {
        AlertDialog(
            onDismissRequest = { showStreakDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🔥 Sequência de Leitura")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Você está com ${userSettings.readingStreakDays} dias seguidos de comunhão com a Palavra!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "A perseverança diária nas Escrituras fortalece a fé e molda o caráter à imagem de Cristo (Romanos 10:17).",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Meta diária atual: ${userSettings.dailyReadingGoalMinutes} minutos por dia.",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GoldAccent
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showStreakDialog = false
                    viewModel.setTab(NavTab.BIBLE)
                }) {
                    Text("Ler Agora na Bíblia")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStreakDialog = false }) {
                    Text("Fechar")
                }
            }
        )
    }

    // New Note Dialog
    if (showNewNoteDialog) {
        NewNoteDialog(
            initialCategory = newNoteCategoryPreset,
            onDismiss = { showNewNoteDialog = false },
            onSave = { title, content, category ->
                viewModel.saveNote(title, content, category)
                showNewNoteDialog = false
                selectedSection = 1 // Switch to notes tab
                Toast.makeText(context, "Anotação salva com sucesso!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Note Detail Dialog
    if (activeNoteDetail != null) {
        NoteDetailDialog(
            note = activeNoteDetail!!,
            onDismiss = { activeNoteDetail = null },
            onDelete = {
                viewModel.deleteNote(activeNoteDetail!!.id)
                activeNoteDetail = null
                Toast.makeText(context, "Anotação excluída.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Doctrines Dialog
    if (showDoctrineDialog) {
        DoctrineGuideDialog(onDismiss = { showDoctrineDialog = false })
    }

    // About App Dialog
    if (showAboutDialog) {
        AboutAppDialog(onDismiss = { showAboutDialog = false })
    }
}

@Composable
fun InteractiveStatBadge(
    label: String,
    value: String,
    sublabel: String,
    modifier: Modifier = Modifier,
    testTag: String = "",
    onClick: () -> Unit
) {
    Surface(
        color = Color(0x22FFFFFF),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp)
        ) {
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            Text(text = label, fontSize = 10.sp, color = Color(0xFFCBD5E1))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = sublabel, fontSize = 9.sp, color = GoldAccent)
        }
    }
}

@Composable
fun FavoritesSection(
    favorites: List<Favorite>,
    onGoToVerse: (String, Int) -> Unit,
    onExploreBible: () -> Unit,
    onAddSampleFavorite: () -> Unit,
    onDeleteFavorite: (Long) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Versículos Marcados (${favorites.size})",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onExploreBible,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_explore_bible")
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ler Bíblia",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = null,
                        modifier = Modifier.size(52.dp),
                        tint = GoldAccent
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Nenhum versículo salvo ainda.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Toque em qualquer versículo durante a leitura bíblica e selecione 'Favoritar', ou adicione um exemplo agora:",
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onAddSampleFavorite,
                            modifier = Modifier.testTag("btn_add_sample_fav")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Inserir Versículo Exemplo")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favorites, key = { it.id }) { fav ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onGoToVerse(fav.bookId, fav.chapter) }
                            .testTag("favorite_card_${fav.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = GoldAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${fav.bookName} ${fav.chapter}:${fav.verse}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString("“${fav.text}” (${fav.bookName} ${fav.chapter}:${fav.verse})"))
                                            Toast.makeText(context, "Versículo copiado!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Outlined.ContentCopy, contentDescription = "Copiar", modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(
                                        onClick = {
                                            val sendIntent = Intent().apply {
                                                action = Intent.ACTION_SEND
                                                putExtra(Intent.EXTRA_TEXT, "“${fav.text}” — ${fav.bookName} ${fav.chapter}:${fav.verse} (Bíblia Viva)")
                                                type = "text/plain"
                                            }
                                            context.startActivity(Intent.createChooser(sendIntent, "Compartilhar Versículo"))
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Outlined.Share, contentDescription = "Compartilhar", modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(
                                        onClick = { onDeleteFavorite(fav.id) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Outlined.Delete, contentDescription = "Remover", tint = Color.Gray, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(
                                        onClick = { onGoToVerse(fav.bookId, fav.chapter) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Abrir na Bíblia", tint = NavyPrimary, modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "“${fav.text}”",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (fav.tags.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "🏷️ ${fav.tags}",
                                    fontSize = 11.sp,
                                    color = GoldAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotesSection(
    notes: List<Note>,
    onNewNoteClick: () -> Unit,
    onNoteClick: (Note) -> Unit,
    onDelete: (Long) -> Unit
) {
    var selectedCategoryFilter by remember { mutableStateOf("Todos") }
    val categories = listOf("Todos", "Estudos", "Sermões", "Devocional", "Oração", "EBD")

    val filteredNotes = remember(notes, selectedCategoryFilter) {
        if (selectedCategoryFilter == "Todos") notes
        else notes.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onNewNoteClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("btn_new_note")
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Nova Anotação ou Reflexão", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategoryFilter == cat,
                    onClick = { selectedCategoryFilter = cat },
                    label = { Text(cat, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (filteredNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = null,
                        modifier = Modifier.size(52.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (selectedCategoryFilter == "Todos") "Nenhuma anotação criada ainda." else "Nenhuma anotação em '$selectedCategoryFilter'.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Guarde suas reflexões devocionais, estudos bíblicos, esboços e orações.",
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onNewNoteClick) {
                        Text("Criar Anotação Agora")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredNotes, key = { it.id }) { note ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNoteClick(note) }
                            .testTag("note_card_${note.id}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = note.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = note.category,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        if (note.bookName != null) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "${note.bookName} ${note.chapter}:${note.verse}",
                                                fontSize = 11.sp,
                                                color = GoldAccent
                                            )
                                        }
                                    }
                                }
                                IconButton(
                                    onClick = { onDelete(note.id) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        Icons.Outlined.Delete,
                                        contentDescription = "Excluir",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = note.content,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumSection(
    isPremium: Boolean,
    onTogglePremium: () -> Unit,
    onOpenAiAssistant: () -> Unit,
    onOpenSermonMaker: () -> Unit,
    onPlayAudio: () -> Unit,
    onOpenStudyHub: () -> Unit,
    onOpenNewNote: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Pro Plan Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NavyPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bíblia Viva Pro 💎",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldAccent
                    )
                    Surface(
                        color = if (isPremium) Color(0xFF166534) else Color(0x33FFFFFF),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isPremium) "STATUS: ATIVO" else "GRATUITO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Apoie a difusão da Palavra e destrave ferramentas exegéticas avançadas para pregadores, líderes e estudantes das Escrituras.",
                    fontSize = 13.sp,
                    color = Color.White,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                listOf(
                    "✓ Assistente Bíblico Cristocêntrico ilimitado",
                    "✓ Gerador Completo de Sermões e Esboços Exegéticos",
                    "✓ Áudio da Bíblia narrado de todos os 66 livros",
                    "✓ Estudos de personagens e temas canônicos completos",
                    "✓ Dicionário teológico e referências cruzadas avançadas",
                    "✓ Caderno de anotações e pedidos de oração ilimitados"
                ).forEach { feature ->
                    Text(
                        text = feature,
                        fontSize = 12.sp,
                        color = Color(0xFFE2E8F0),
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onTogglePremium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPremium) Color(0xFF1B4931) else GoldAccent
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_toggle_premium")
                ) {
                    Text(
                        text = if (isPremium) "✓ Modo Pro Ativo (Toque para Alternar)" else "Ativar Bíblia Viva Pro Agora 💎",
                        fontWeight = FontWeight.Bold,
                        color = if (isPremium) Color.White else NavyDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ferramentas Rápidas do Plano",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick feature launch buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProQuickActionCard(
                title = "Assistente IA",
                desc = "Perguntas bíblicas",
                icon = Icons.Default.AutoAwesome,
                modifier = Modifier.weight(1f),
                onClick = onOpenAiAssistant
            )
            ProQuickActionCard(
                title = "Criar Sermão",
                desc = "Esboço exegético",
                icon = Icons.Default.RecordVoiceOver,
                modifier = Modifier.weight(1f),
                onClick = onOpenSermonMaker
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProQuickActionCard(
                title = "Ouvir Bíblia",
                desc = "Áudio narrado",
                icon = Icons.Default.VolumeUp,
                modifier = Modifier.weight(1f),
                onClick = onPlayAudio
            )
            ProQuickActionCard(
                title = "Centro Estudos",
                desc = "Temas e personagens",
                icon = Icons.Default.School,
                modifier = Modifier.weight(1f),
                onClick = onOpenStudyHub
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onOpenNewNote,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.EditNote, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Nova Nota no Caderno Ministerial")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ProQuickActionCard(
    title: String,
    desc: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = NavyPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(text = desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun AdminLeadershipSection(
    sermonsCount: Int,
    notesCount: Int,
    chaptersRead: Int,
    onPrepareSermon: () -> Unit,
    onStudyHub: () -> Unit,
    onNewNote: () -> Unit,
    onShowDoctrines: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Painel de Liderança e Governança",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Visão geral de estudos ministeriais, integridade teológica e conformidade autoral.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onPrepareSermon() }
                    .testTag("card_lead_sermon")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = NavyPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Criar Sermão", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Gerador IA homilético", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onStudyHub() }
                    .testTag("card_lead_study")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF1B4931))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Centro Teológico", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Temas & Personagens", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Action Buttons Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onNewNote() }
                    .testTag("card_lead_note")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Icon(Icons.Default.EditNote, contentDescription = null, tint = GoldAccent)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Nota Ministerial", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Registrar estudo", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onShowDoctrines() }
                    .testTag("card_lead_doctrines")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color(0xFF8B2635))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Confissão de Fé", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("5 Solas & Diretrizes", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Metrics Card with Copy Report Action
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Métricas Ministeriais",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = {
                            val report = """
Relatório Ministerial - Bíblia Viva
- Esboços e Sermões preparados: $sermonsCount
- Caderno de Estudos (Notas): $notesCount
- Capítulos lidos no app: $chaptersRead
- Status: Ativo em Cristo
                            """.trimIndent()
                            clipboardManager.setText(AnnotatedString(report))
                            Toast.makeText(context, "Relatório copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Outlined.ContentCopy, contentDescription = "Copiar Relatório", modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Esboços e Sermões preparados: $sermonsCount", fontSize = 12.sp)
                Text("• Caderno de Estudos (Notas ativas): $notesCount", fontSize = 12.sp)
                Text("• Capítulos lidos no aplicativo: $chaptersRead", fontSize = 12.sp)
                Text("• Aliança de Leitura Diária: Ativa", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Status Teológico
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Status Teológico e Autoral",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("• Versão Principal: Almeida Revista e Corrigida (Domínio Público)", fontSize = 12.sp)
                Text("• Versão Complementar: Bíblia Livre (Creative Commons CC-BY-SA)", fontSize = 12.sp)
                Text("• Diretriz de IA: Cristocêntrica, sem alegações de revelação extra-bíblica", fontSize = 12.sp)
                Text("• Salvaguarda Pastoral: Recomendações pastorais e profissionais ativas", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingsSection(
    userSettings: UserSettings,
    onFontSizeChange: (Int) -> Unit,
    onVersionChange: (String) -> Unit,
    onGoalChange: (Int) -> Unit,
    onThemeChange: (String) -> Unit,
    onEditNameClick: () -> Unit,
    onResetDefaults: () -> Unit,
    onShowAbout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Configurações de Estudo & Leitura",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Profile Identity Card
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Identificação do Estudante",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = userSettings.userName,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                OutlinedButton(
                    onClick = onEditNameClick,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Alterar", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Font Size
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tamanho da Fonte: ${userSettings.fontSizeSp}sp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { if (userSettings.fontSizeSp > 14) onFontSizeChange(userSettings.fontSizeSp - 2) },
                            enabled = userSettings.fontSizeSp > 14,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_font_dec")
                        ) {
                            Text("A-", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { if (userSettings.fontSizeSp < 28) onFontSizeChange(userSettings.fontSizeSp + 2) },
                            enabled = userSettings.fontSizeSp < 28,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_font_inc")
                        ) {
                            Text("A+", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = userSettings.fontSizeSp.toFloat(),
                    onValueChange = { onFontSizeChange(it.toInt()) },
                    valueRange = 14f..28f,
                    steps = 6,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Porque Deus amou o mundo de tal maneira...",
                        fontSize = userSettings.fontSizeSp.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Preferred Version
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Versão Bíblica Padrão",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ARC" to "Almeida ARC", "BLIVRE" to "Bíblia Livre", "BVE" to "BVE Estudo").forEach { (id, label) ->
                        FilterChip(
                            selected = userSettings.preferredVersionId == id,
                            onClick = { onVersionChange(id) },
                            label = { Text(label, fontSize = 12.sp) },
                            modifier = Modifier.testTag("chip_version_$id")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Daily Goal
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Meta Diária de Leitura",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(10, 15, 30, 45, 60).forEach { min ->
                        FilterChip(
                            selected = userSettings.dailyReadingGoalMinutes == min,
                            onClick = { onGoalChange(min) },
                            label = { Text("$min min", fontSize = 12.sp) },
                            modifier = Modifier.testTag("chip_goal_$min")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Reading Theme
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tema Visual de Leitura",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("parchment" to "Pergaminho", "light" to "Claro", "dark" to "Escuro").forEach { (t, label) ->
                        FilterChip(
                            selected = userSettings.readingTheme == t,
                            onClick = { onThemeChange(t) },
                            label = { Text(label, fontSize = 12.sp) },
                            modifier = Modifier.testTag("chip_theme_$t")
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reset and About buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onResetDefaults,
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_reset_settings")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Redefinir Ajustes", fontSize = 12.sp)
            }
            Button(
                onClick = onShowAbout,
                modifier = Modifier
                    .weight(1f)
                    .testTag("btn_about_app")
            ) {
                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sobre o App", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun NewNoteDialog(
    initialCategory: String = "Estudos",
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory) }
    val categories = listOf("Estudos", "Sermões", "Devocional", "Oração", "EBD")

    val presets = listOf(
        "Estudo Exegético" to "Passagem bíblica, contexto histórico e significado teológico.",
        "Reflexão Devocional" to "O que o Senhor falou ao meu coração nesta meditação.",
        "Esboço de Sermão" to "Tema, texto-base, introdução, 3 pontos e apelo.",
        "Motivo de Oração" to "Intercessão e agradecimento diante de Deus."
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nova Anotação de Estudo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text("Sugestões rápidas de preenchimento:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    items(presets) { (presetTitle, presetDesc) ->
                        SuggestionChip(
                            onClick = {
                                if (title.isBlank()) title = presetTitle
                                if (content.isBlank()) content = presetDesc
                            },
                            label = { Text(presetTitle, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título do Estudo") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_note_title")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Categoria:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Conteúdo da Anotação / Insights") },
                    minLines = 4,
                    maxLines = 8,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_note_content")
                )

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
                            val finalTitle = title.ifBlank { "Anotação de Estudo" }
                            val finalContent = content.ifBlank { "Reflexão bíblica registrada." }
                            onSave(finalTitle, finalContent, selectedCategory)
                        },
                        modifier = Modifier.testTag("btn_save_note")
                    ) {
                        Text("Salvar Anotação")
                    }
                }
            }
        }
    }
}

@Composable
fun NoteDetailDialog(
    note: Note,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = note.category,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (note.bookName != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${note.bookName} ${note.chapter}:${note.verse}",
                            fontSize = 12.sp,
                            color = GoldAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = note.content,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString("${note.title}\n\n${note.content}"))
                                Toast.makeText(context, "Anotação copiada!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Outlined.ContentCopy, contentDescription = "Copiar", modifier = Modifier.size(18.dp))
                        }
                        IconButton(
                            onClick = {
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.content}\n\n(Bíblia Viva)")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Compartilhar Anotação"))
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Outlined.Share, contentDescription = "Compartilhar", modifier = Modifier.size(18.dp))
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Excluir")
                        }
                        Button(onClick = onDismiss) {
                            Text("Fechar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DoctrineGuideDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(8.dp)
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
                        text = "Pilares Teológicos & Hermenêuticos",
                        fontSize = 17.sp,
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
                        Text("Os 5 Solas da Reforma:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = GoldAccent)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("1. Sola Scriptura: Somente a Bíblia é a regra infalível de fé e prática.", fontSize = 12.sp)
                        Text("2. Solus Christus: A salvação é unicamente através da pessoa e obra da cruz de Cristo.", fontSize = 12.sp)
                        Text("3. Sola Gratia: A justificação é fruto do favor imerecido de Deus, sem obras humanas.", fontSize = 12.sp)
                        Text("4. Sola Fide: A justiça de Cristo é imputada ao crente exclusivamente pela fé.", fontSize = 12.sp)
                        Text("5. Soli Deo Gloria: Toda a glória pertence somente a Deus em todas as coisas.", fontSize = 12.sp)

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Princípio Cristocêntrico (Lucas 24:27, 44):", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NavyPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Toda a Escritura — a Lei de Moisés, os Profetas e os Salmos — converge para Jesus Cristo. Os estudos e sermões devem discernir Sua pessoa na tipologia, promessa, profecia e cumprimento redentor, sem jamais recorrer a alegorias desprovidas de base textual.",
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Responsabilidade Ministerial & Pastoral:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "O aplicativo serve como apoio aos santos na leitura e meditação diárias. Recursos tecnológicos de assistência nunca substituem a liderança pastoral da igreja local nem o cuidado profissional de saúde.",
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_dismiss_doctrines")
                ) {
                    Text("Entendido")
                }
            }
        }
    }
}

@Composable
fun AboutAppDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Bíblia Viva — Estudo Cristocêntrico",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Versão 1.2.0 • Edição Ministerial", fontSize = 12.sp, color = GoldAccent, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "A Bíblia Viva foi desenvolvida para edificar o povo de Deus, unindo o rigor exegético reformado à melhor experiência digital móvel.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text("Traduções Bíblicas Utilizadas:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text("• ARC (Almeida Revista e Corrigida) - Domínio Público", fontSize = 12.sp)
                Text("• Bíblia Livre (BLIVRE) - Creative Commons CC-BY-SA 3.0", fontSize = 12.sp)
                Text("• BVE (Bíblia Viva de Estudo) - Textos Canônicos", fontSize = 12.sp)

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_close_about")
                ) {
                    Text("Fechar")
                }
            }
        }
    }
}

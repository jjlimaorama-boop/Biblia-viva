package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.ui.BibleViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleReaderScreen(
    viewModel: BibleViewModel,
    modifier: Modifier = Modifier
) {
    val books by viewModel.books.collectAsState()
    val versions by viewModel.versions.collectAsState()
    val selectedBookId by viewModel.selectedBookId.collectAsState()
    val selectedChapter by viewModel.selectedChapter.collectAsState()
    val selectedVersionId by viewModel.selectedVersionId.collectAsState()
    val fontSizeSp by viewModel.fontSizeSp.collectAsState()
    val readingTheme by viewModel.readingTheme.collectAsState()
    val verses by viewModel.currentVerses.collectAsState()
    val highlights by viewModel.chapterHighlights.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val activeVerse by viewModel.activeVerseAction.collectAsState()
    val crossReferences by viewModel.currentVerseCrossReferences.collectAsState()

    val currentBook = books.find { it.id == selectedBookId } ?: books.firstOrNull()

    var showBookSelector by remember { mutableStateOf(false) }
    var showVersionSelector by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var showChristStudyDialog by remember { mutableStateOf(false) }

    // Color theme for reading container
    val backgroundColor = when (readingTheme) {
        "dark" -> NavyDark
        "light" -> Color.White
        else -> ParchmentBg
    }

    val textColor = when (readingTheme) {
        "dark" -> Color(0xFFF1F5F9)
        "light" -> Color(0xFF1E293B)
        else -> ParchmentText
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .testTag("bible_reader_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar for Bible Reader
            Surface(
                color = if (readingTheme == "dark") NavySurface else Color.White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Book & Chapter Selector Trigger
                        OutlinedButton(
                            onClick = { showBookSelector = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("book_chapter_selector_btn")
                        ) {
                            Text(
                                text = "${currentBook?.name ?: "Livro"} $selectedChapter",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Selecionar Livro",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Version selector
                            TextButton(
                                onClick = { showVersionSelector = true },
                                modifier = Modifier.testTag("version_selector_btn")
                            ) {
                                Text(
                                    text = selectedVersionId,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = GoldAccent
                                )
                            }

                            // Font size minus
                            IconButton(
                                onClick = { viewModel.adjustFontSize(-2) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("A-", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            // Font size plus
                            IconButton(
                                onClick = { viewModel.adjustFontSize(2) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("A+", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            // Theme toggle button
                            IconButton(
                                onClick = {
                                    val nextTheme = when (readingTheme) {
                                        "parchment" -> "light"
                                        "light" -> "dark"
                                        else -> "parchment"
                                    }
                                    viewModel.setReadingTheme(nextTheme)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (readingTheme == "dark") Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Mudar Tema",
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Audio Chapter Button
                            FilledTonalIconButton(
                                onClick = { viewModel.playCurrentChapter() },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headphones,
                                    contentDescription = "Ouvir Capítulo",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Book & Chapter Header Context Banner
            if (currentBook != null) {
                Surface(
                    color = if (readingTheme == "dark") NavyContainer else Color(0xFFF1EDE4),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${currentBook.testament} • ${currentBook.category} • Autor: ${currentBook.author}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Quick Prev / Next Chapter Buttons
                            Row {
                                IconButton(
                                    onClick = {
                                        if (selectedChapter > 1) {
                                            viewModel.selectBookAndChapter(selectedBookId, selectedChapter - 1)
                                        }
                                    },
                                    enabled = selectedChapter > 1,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.ChevronLeft, "Capítulo anterior", modifier = Modifier.size(18.dp))
                                }

                                Text(
                                    text = "$selectedChapter / ${currentBook.chapterCount}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )

                                IconButton(
                                    onClick = {
                                        if (selectedChapter < currentBook.chapterCount) {
                                            viewModel.selectBookAndChapter(selectedBookId, selectedChapter + 1)
                                        }
                                    },
                                    enabled = selectedChapter < currentBook.chapterCount,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.ChevronRight, "Próximo capítulo", modifier = Modifier.size(18.dp))
                                }
                            }
                        }

                        // Christ-Centered Theme Pill
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "✝️ Cristo em ${currentBook.name}:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentBook.christCenteredTheme,
                                fontSize = 11.sp,
                                maxLines = 1,
                                color = textColor.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Verse List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 100.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "${currentBook?.name} $selectedChapter",
                        fontSize = (fontSizeSp + 6).sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                if (verses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = GoldAccent)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Abrindo ${currentBook?.name ?: "Livro"} $selectedChapter...",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textColor.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                } else {
                    items(verses) { verse ->
                        val highlight = highlights.find { it.verse == verse.verse }
                        val isFavorited = favorites.any {
                            it.bookId == verse.bookId && it.chapter == verse.chapter && it.verse == verse.verse
                        }

                        val verseBgColor = when (highlight?.category) {
                            "Promessas" -> HighlightPromise
                            "Doutrina" -> HighlightDoctrine
                            "Aplicação" -> HighlightApplication
                            "Evangelho" -> HighlightGospel
                            "Oração" -> HighlightPrayer
                            else -> Color.Transparent
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(verseBgColor)
                                .clickable { viewModel.selectVerseAction(verse) }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                // Verse Number
                                Text(
                                    text = "${verse.verse} ",
                                    fontSize = (fontSizeSp - 2).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                // Verse Text
                                Text(
                                    text = verse.text,
                                    fontSize = fontSizeSp.sp,
                                    lineHeight = (fontSizeSp * 1.5).sp,
                                    color = if (verseBgColor != Color.Transparent) ParchmentText else textColor,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isFavorited) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Favorito",
                                        tint = GoldAccent,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = {
                                    if (selectedChapter > 1) {
                                        viewModel.selectBookAndChapter(selectedBookId, selectedChapter - 1)
                                    }
                                },
                                enabled = selectedChapter > 1,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ChevronLeft, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Cap. ${selectedChapter - 1}", fontSize = 12.sp)
                            }

                            val maxChapter = currentBook?.chapterCount ?: 1
                            OutlinedButton(
                                onClick = {
                                    if (selectedChapter < maxChapter) {
                                        viewModel.selectBookAndChapter(selectedBookId, selectedChapter + 1)
                                    }
                                },
                                enabled = selectedChapter < maxChapter,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Cap. ${selectedChapter + 1}", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Verse Action Bottom Sheet
        if (activeVerse != null) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.selectVerseAction(null) },
                sheetState = rememberModalBottomSheetState(),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 32.dp)
                ) {
                    // Sheet Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${activeVerse?.bookName} ${activeVerse?.chapter}:${activeVerse?.verse}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Versão: $selectedVersionId",
                                fontSize = 12.sp,
                                color = GoldAccent
                            )
                        }

                        // Play Audio for Verse
                        FilledTonalIconButton(
                            onClick = {
                                activeVerse?.let { viewModel.playVerseAudio(it) }
                            }
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Ouvir versículo")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "“${activeVerse?.text}”",
                        fontSize = 14.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Primary Action: ✝️ Estudar Cristo neste texto
                    Button(
                        onClick = {
                            activeVerse?.let {
                                viewModel.performChristCenteredStudy(it)
                                showChristStudyDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VividBlue,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("christ_study_btn")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✝️ Estudar Cristo neste texto",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Secondary Actions: Favorite & Note
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                activeVerse?.let { viewModel.toggleFavorite(it) }
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Outlined.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Favoritar", fontSize = 13.sp)
                        }

                        OutlinedButton(
                            onClick = { showNoteDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Anotar", fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Highlight Palette
                    Text(
                        text = "Destacar com cor temática:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HighlightChip("Promessas", HighlightPromise) {
                            activeVerse?.let { viewModel.addHighlight(it, "Promessas", 0xFFFEF3C7) }
                        }
                        HighlightChip("Doutrina", HighlightDoctrine) {
                            activeVerse?.let { viewModel.addHighlight(it, "Doutrina", 0xFFE0F2FE) }
                        }
                        HighlightChip("Aplicação", HighlightApplication) {
                            activeVerse?.let { viewModel.addHighlight(it, "Aplicação", 0xFFDCFCE7) }
                        }
                        HighlightChip("Evangelho", HighlightGospel) {
                            activeVerse?.let { viewModel.addHighlight(it, "Evangelho", 0xFFFFE4E6) }
                        }
                        HighlightChip("Oração", HighlightPrayer) {
                            activeVerse?.let { viewModel.addHighlight(it, "Oração", 0xFFF3E8FF) }
                        }

                        // Remove Highlight
                        IconButton(
                            onClick = { activeVerse?.let { viewModel.removeHighlight(it) } },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.FormatColorReset, contentDescription = "Remover Destaque", modifier = Modifier.size(18.dp))
                        }
                    }

                    // Cross References List
                    if (crossReferences.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Referências Cruzadas:",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        crossReferences.forEach { ref ->
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${ref.toBookName} ${ref.toChapter}:${ref.toVerse}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = GoldAccent
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = ref.explanation,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Christ-Centered Study Modal Dialog
        if (showChristStudyDialog) {
            val studyResult by viewModel.christStudyResult.collectAsState()
            val isLoading by viewModel.isChristStudyLoading.collectAsState()

            Dialog(onDismissRequest = { showChristStudyDialog = false }) {
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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "✝️ Estudo Cristocêntrico",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(onClick = { showChristStudyDialog = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Fechar")
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    CircularProgressIndicator(color = GoldAccent)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Examinando as Escrituras em Cristo...",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = studyResult ?: "Nenhum resultado encontrado.",
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "“Examinais as Escrituras... e são elas mesmas que testificam de mim.” (João 5:39)",
                                        fontSize = 11.sp,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = { showChristStudyDialog = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Text("Fechar")
                        }
                    }
                }
            }
        }

        // Personal Note Dialog
        if (showNoteDialog && activeVerse != null) {
            var noteTitle by remember { mutableStateOf("Reflexão sobre ${activeVerse?.bookName} ${activeVerse?.chapter}:${activeVerse?.verse}") }
            var noteContent by remember { mutableStateOf("") }
            var selectedCategory by remember { mutableStateOf("Estudos") }

            Dialog(onDismissRequest = { showNoteDialog = false }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Nova Anotação Pessoal",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = noteTitle,
                            onValueChange = { noteTitle = it },
                            label = { Text("Título") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = noteContent,
                            onValueChange = { noteContent = it },
                            label = { Text("Conteúdo da Anotação") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category Selection
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Estudos", "Sermões", "Devocional", "Oração").forEach { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showNoteDialog = false }) {
                                Text("Cancelar")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (noteTitle.isNotBlank()) {
                                        viewModel.saveNote(noteTitle, noteContent, selectedCategory, activeVerse)
                                        showNoteDialog = false
                                        viewModel.selectVerseAction(null)
                                    }
                                }
                            ) {
                                Text("Salvar")
                            }
                        }
                    }
                }
            }
        }

        // Book and Chapter Selector Dialog
        if (showBookSelector) {
            BookChapterSelectorDialog(
                books = books,
                selectedBookId = selectedBookId,
                selectedChapter = selectedChapter,
                onDismiss = { showBookSelector = false },
                onSelect = { bookId, chapter ->
                    viewModel.selectBookAndChapter(bookId, chapter)
                    showBookSelector = false
                }
            )
        }

        // Version Selector Dialog
        if (showVersionSelector) {
            VersionSelectorDialog(
                versions = versions,
                selectedVersionId = selectedVersionId,
                onDismiss = { showVersionSelector = false },
                onSelect = { versionId ->
                    viewModel.selectVersion(versionId)
                    showVersionSelector = false
                }
            )
        }
    }
}

@Composable
fun HighlightChip(
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, Color(0x33000000), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Dot indicator
    }
}

@Composable
fun BookChapterSelectorDialog(
    books: List<BibleBook>,
    selectedBookId: String,
    selectedChapter: Int,
    onDismiss: () -> Unit,
    onSelect: (String, Int) -> Unit
) {
    val initialTestament = books.find { it.id == selectedBookId }?.testament ?: "AT"
    var selectedTab by remember(selectedBookId) { mutableStateOf(initialTestament) }
    var tempBookId by remember(selectedBookId) { mutableStateOf(selectedBookId) }
    var searchQuery by remember { mutableStateOf("") }

    val chosenBook = books.find { it.id == tempBookId } ?: books.firstOrNull { it.testament == selectedTab } ?: books.firstOrNull()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selecionar Livro e Capítulo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Search Bar for Books
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar livro (ex: Mateus, Salmos...)", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // AT / NT Switch (when not searching)
                if (searchQuery.isBlank()) {
                    TabRow(
                        selectedTabIndex = if (selectedTab == "AT") 0 else 1,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Tab(
                            selected = selectedTab == "AT",
                            onClick = {
                                selectedTab = "AT"
                                if (books.find { it.id == tempBookId }?.testament != "AT") {
                                    books.firstOrNull { it.testament == "AT" }?.let { tempBookId = it.id }
                                }
                            },
                            text = { Text("Antigo Testamento", fontSize = 12.sp) }
                        )
                        Tab(
                            selected = selectedTab == "NT",
                            onClick = {
                                selectedTab = "NT"
                                if (books.find { it.id == tempBookId }?.testament != "NT") {
                                    books.firstOrNull { it.testament == "NT" }?.let { tempBookId = it.id }
                                }
                            },
                            text = { Text("Novo Testamento", fontSize = 12.sp) }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Books & Chapters columns
                Row(modifier = Modifier.weight(1f)) {
                    // Books List
                    val filteredBooks = if (searchQuery.isNotBlank()) {
                        books.filter {
                            it.name.contains(searchQuery, ignoreCase = true) ||
                            it.abbreviation.contains(searchQuery, ignoreCase = true)
                        }
                    } else {
                        books.filter { it.testament == selectedTab }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .weight(1.1f)
                            .padding(end = 6.dp)
                    ) {
                        items(filteredBooks) { book ->
                            val isSelected = book.id == tempBookId
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { tempBookId = book.id }
                                    .padding(vertical = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = book.name,
                                        fontSize = 13.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${book.chapterCount}",
                                        fontSize = 10.sp,
                                        color = if (isSelected) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )

                    // Chapters Selector for chosen book
                    Column(
                        modifier = Modifier
                            .weight(1.3f)
                            .padding(start = 8.dp)
                    ) {
                        Text(
                            text = "${chosenBook?.name ?: "Livro"}: Capítulos",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        val count = chosenBook?.chapterCount ?: 1
                        val chapters = (1..count).toList()

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Render chapters in rows of 3
                            val chunked = chapters.chunked(3)
                            items(chunked) { rowChapters ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    for (ch in rowChapters) {
                                        val isCurrent = ch == selectedChapter && tempBookId == selectedBookId
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isCurrent) GoldAccent else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { onSelect(tempBookId, ch) }
                                        ) {
                                            Text(
                                                text = "$ch",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCurrent) NavyDark else MaterialTheme.colorScheme.onSurface,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 10.dp)
                                            )
                                        }
                                    }
                                    // Empty spacer if row has less than 3
                                    for (i in 0 until (3 - rowChapters.size)) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Shortcuts for Popular Books
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    AssistChip(
                        onClick = { onSelect("GEN", 1) },
                        label = { Text("Gn 1", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { onSelect("PSA", 23) },
                        label = { Text("Sl 23", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { onSelect("MAT", 5) },
                        label = { Text("Mt 5", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { onSelect("JHN", 3) },
                        label = { Text("Jo 3", fontSize = 10.sp) }
                    )
                    AssistChip(
                        onClick = { onSelect("ROM", 8) },
                        label = { Text("Rm 8", fontSize = 10.sp) }
                    )
                }
            }
        }
    }
}

@Composable
fun VersionSelectorDialog(
    versions: List<com.example.data.model.BibleVersion>,
    selectedVersionId: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Versões da Bíblia",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Todas as versões respeitam a legislação de direitos autorais e licenças públicas.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                versions.forEach { ver ->
                    val isSelected = ver.id == selectedVersionId
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(ver.id) }
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = ver.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = ver.abbreviation,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = GoldAccent
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = ver.copyrightInfo,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fechar")
                }
            }
        }
    }
}

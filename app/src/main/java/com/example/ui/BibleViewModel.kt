package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAiService
import com.example.audio.BibleAudioPlayer
import com.example.data.local.AppDatabase
import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.BibleVersion
import com.example.data.model.BiblicalCharacter
import com.example.data.model.CrossReference
import com.example.data.model.Devotional
import com.example.data.model.Favorite
import com.example.data.model.Highlight
import com.example.data.model.Note
import com.example.data.model.PrayerRequest
import com.example.data.model.ReadingPlanProgress
import com.example.data.model.Sermon
import com.example.data.model.StudyTopic
import com.example.data.model.UserSettings
import com.example.data.repository.BibleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavTab {
    HOME,
    BIBLE,
    STUDY,
    AI_TOOLS,
    PROFILE
}

class BibleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = BibleRepository(db.bibleDao())
    val audioPlayer = BibleAudioPlayer(application)

    // Navigation
    private val _currentTab = MutableStateFlow(NavTab.HOME)
    val currentTab: StateFlow<NavTab> = _currentTab.asStateFlow()

    fun setTab(tab: NavTab) {
        _currentTab.value = tab
    }

    // Reader state
    val books: StateFlow<List<BibleBook>> = repository.allBooks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val versions: StateFlow<List<BibleVersion>> = repository.allVersions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userSettings: StateFlow<UserSettings> = repository.userSettings
        .combine(MutableStateFlow(Unit)) { settings, _ -> settings ?: UserSettings() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    private val _selectedBookId = MutableStateFlow("JHN")
    val selectedBookId: StateFlow<String> = _selectedBookId.asStateFlow()

    private val _selectedChapter = MutableStateFlow(3)
    val selectedChapter: StateFlow<Int> = _selectedChapter.asStateFlow()

    private val _selectedVersionId = MutableStateFlow("ARC")
    val selectedVersionId: StateFlow<String> = _selectedVersionId.asStateFlow()

    private val _fontSizeSp = MutableStateFlow(18)
    val fontSizeSp: StateFlow<Int> = _fontSizeSp.asStateFlow()

    private val _readingTheme = MutableStateFlow("parchment") // "parchment", "light", "dark"
    val readingTheme: StateFlow<String> = _readingTheme.asStateFlow()

    init {
        viewModelScope.launch {
            userSettings.collect { settings ->
                _fontSizeSp.value = settings.fontSizeSp
                _readingTheme.value = settings.readingTheme
                _selectedVersionId.value = settings.preferredVersionId
            }
        }
    }

    val currentBook: StateFlow<BibleBook?> = combine(books, _selectedBookId) { bookList, bookId ->
        bookList.find { it.id == bookId }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentVerses: StateFlow<List<BibleVerse>> = combine(
        _selectedVersionId,
        _selectedBookId,
        _selectedChapter
    ) { versionId, bookId, chapter ->
        Triple(versionId, bookId, chapter)
    }.flatMapLatest { (versionId, bookId, chapter) ->
        repository.getVerses(versionId, bookId, chapter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val chapterHighlights: StateFlow<List<Highlight>> = combine(
        _selectedBookId,
        _selectedChapter
    ) { bookId, chapter ->
        Pair(bookId, chapter)
    }.flatMapLatest { (bookId, chapter) ->
        repository.getHighlightsForChapter(bookId, chapter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<Favorite>> = repository.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<Note>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val prayers: StateFlow<List<PrayerRequest>> = repository.allPrayers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sermons: StateFlow<List<Sermon>> = repository.allSermons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topics: StateFlow<List<StudyTopic>> = repository.allTopics
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val characters: StateFlow<List<BiblicalCharacter>> = repository.allCharacters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val devotionals: StateFlow<List<Devotional>> = repository.allDevotionals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Verse Selection for Action Bottom Sheet
    private val _activeVerseAction = MutableStateFlow<BibleVerse?>(null)
    val activeVerseAction: StateFlow<BibleVerse?> = _activeVerseAction.asStateFlow()

    fun selectVerseAction(verse: BibleVerse?) {
        _activeVerseAction.value = verse
    }

    // Cross references for currently selected verse
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentVerseCrossReferences: StateFlow<List<CrossReference>> = _activeVerseAction
        .flatMapLatest { verse ->
            if (verse != null) {
                repository.getCrossReferences(verse.bookId, verse.chapter, verse.verse)
            } else {
                MutableStateFlow(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectBookAndChapter(bookId: String, chapter: Int) {
        _selectedBookId.value = bookId
        _selectedChapter.value = chapter
        _activeVerseAction.value = null
        viewModelScope.launch {
            repository.saveUserSettings(
                userSettings.value.copy(
                    currentBookId = bookId,
                    currentChapter = chapter
                )
            )
        }
    }

    fun selectVersion(versionId: String) {
        _selectedVersionId.value = versionId
        viewModelScope.launch {
            repository.saveUserSettings(userSettings.value.copy(preferredVersionId = versionId))
        }
    }

    fun adjustFontSize(delta: Int) {
        val newSize = (_fontSizeSp.value + delta).coerceIn(14, 28)
        _fontSizeSp.value = newSize
        viewModelScope.launch {
            repository.saveUserSettings(userSettings.value.copy(fontSizeSp = newSize))
        }
    }

    fun setReadingTheme(theme: String) {
        _readingTheme.value = theme
        viewModelScope.launch {
            repository.saveUserSettings(userSettings.value.copy(readingTheme = theme))
        }
    }

    fun addHighlight(verse: BibleVerse, category: String, colorHex: Long) {
        viewModelScope.launch {
            repository.addHighlight(verse.bookId, verse.chapter, verse.verse, category, colorHex)
        }
    }

    fun removeHighlight(verse: BibleVerse) {
        viewModelScope.launch {
            repository.removeHighlight(verse.bookId, verse.chapter, verse.verse)
        }
    }

    fun toggleFavorite(verse: BibleVerse) {
        viewModelScope.launch {
            repository.toggleFavorite(
                verse.bookId,
                verse.bookName,
                verse.chapter,
                verse.verse,
                verse.text,
                _selectedVersionId.value
            )
        }
    }

    fun deleteFavorite(id: Long) {
        viewModelScope.launch {
            repository.deleteFavorite(id)
        }
    }

    fun removeFavorite(bookId: String, chapter: Int, verse: Int) {
        viewModelScope.launch {
            repository.removeFavorite(bookId, chapter, verse)
        }
    }

    fun addExampleFavorite() {
        viewModelScope.launch {
            repository.toggleFavorite(
                "JHN",
                "João",
                14,
                6,
                "Disse-lhe Jesus: Eu sou o caminho, e a verdade, e a vida. Ninguém vem ao Pai senão por mim.",
                "ARC"
            )
        }
    }

    fun resetSettingsToDefault() {
        viewModelScope.launch {
            repository.saveUserSettings(UserSettings())
            _fontSizeSp.value = 18
            _readingTheme.value = "parchment"
            _selectedVersionId.value = "ARC"
        }
    }

    // Audio controls
    fun playCurrentChapter() {
        val currentBook = books.value.find { it.id == _selectedBookId.value }
        val name = currentBook?.name ?: "Bíblia"
        audioPlayer.playChapter(name, _selectedChapter.value, currentVerses.value)
    }

    fun playVerseAudio(verse: BibleVerse) {
        audioPlayer.playSingleVerse(verse.bookName, verse.chapter, verse)
    }

    // AI & Cristocentric Study
    private val _christStudyResult = MutableStateFlow<String?>(null)
    val christStudyResult: StateFlow<String?> = _christStudyResult.asStateFlow()

    private val _isChristStudyLoading = MutableStateFlow(false)
    val isChristStudyLoading: StateFlow<Boolean> = _isChristStudyLoading.asStateFlow()

    fun performChristCenteredStudy(verse: BibleVerse) {
        _isChristStudyLoading.value = true
        _christStudyResult.value = null
        viewModelScope.launch {
            val prompt = """
Analise a passagem ${verse.bookName} ${verse.chapter}:${verse.verse} - "${verse.text}" com foco estritamente cristocêntrico e responsabilidade hermenêutica:
1. Contexto original e intenção do autor.
2. Mensagem principal e verdade sobre Deus e a condição humana.
3. Como a passagem aponta legitimamente para Cristo (sem alegorias arbitrárias, distinguindo profecia, tipologia ou cumprimento).
4. Relação com o Evangelho e a história da redenção.
5. Aplicação pessoal e para a igreja.
6. Ideias para pregar este texto.
            """.trimIndent()
            val instruction = "Você é o Assistente Bíblia Viva, teologicamente fiel, cristocêntrico, reverente e pastoral."
            val response = GeminiAiService.queryGemini(prompt, instruction)
            _christStudyResult.value = response
            _isChristStudyLoading.value = false
        }
    }

    // AI Chat
    private val _chatMessages = MutableStateFlow<List<Pair<String, String>>>(
        listOf("assistant" to "Paz seja convosco! Sou o Assistente Bíblia Viva. Como posso auxiliá-lo no estudo das Sagradas Escrituras hoje?")
    )
    val chatMessages: StateFlow<List<Pair<String, String>>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    fun sendChatMessage(userText: String) {
        if (userText.isBlank()) return
        val current = _chatMessages.value.toMutableList()
        current.add("user" to userText)
        _chatMessages.value = current
        _isChatLoading.value = true

        viewModelScope.launch {
            val instruction = """
Você é o Assistente Bíblia Viva. Responda com base no texto bíblico e teologia bíblica sólida. 
Sempre aponte para Cristo com discernimento. Não invente versículos. Não substitua conselho pastoral em casos graves. 
Inclua contexto, verdade central, conexão com Cristo e aplicação.
            """.trimIndent()
            val response = GeminiAiService.queryGemini(userText, instruction)
            val updated = _chatMessages.value.toMutableList()
            updated.add("assistant" to response)
            _chatMessages.value = updated
            _isChatLoading.value = false
        }
    }

    // Sermon Generator
    private val _generatedSermon = MutableStateFlow<String?>(null)
    val generatedSermon: StateFlow<String?> = _generatedSermon.asStateFlow()

    private val _isGeneratingSermon = MutableStateFlow(false)
    val isGeneratingSermon: StateFlow<Boolean> = _isGeneratingSermon.asStateFlow()

    fun generateSermon(
        passage: String,
        theme: String,
        audience: String,
        duration: String,
        messageType: String
    ) {
        _isGeneratingSermon.value = true
        _generatedSermon.value = null
        viewModelScope.launch {
            val prompt = """
Gere um esboço completo de pregação cristocêntrica com os seguintes dados:
- Texto-Base: $passage
- Tema Proposto: $theme
- Público: $audience
- Tempo Estimado: $duration
- Tipo de Mensagem: $messageType

Apresente:
1. Título Impactante
2. Texto-base e Tema central
3. Objetivo da Mensagem
4. Introdução
5. Contexto Histórico e Literário
6. Estrutura com Pontos Principais detalhados
7. Aplicação Prática
8. Conexão Explícita com Cristo e o Evangelho
9. Ilustrações sugeridas
10. Perguntas para Reflexão
11. Conclusão, Apelo e Oração Final
            """.trimIndent()
            val instruction = "Você é um mestre em homilética cristocêntrica, unindo profundidade exegética com paixão pelo Evangelho."
            val response = GeminiAiService.queryGemini(prompt, instruction)
            _generatedSermon.value = response
            _isGeneratingSermon.value = false
        }
    }

    // Biblical Counselor
    private val _counselResult = MutableStateFlow<String?>(null)
    val counselResult: StateFlow<String?> = _counselResult.asStateFlow()

    private val _isCounselLoading = MutableStateFlow(false)
    val isCounselLoading: StateFlow<Boolean> = _isCounselLoading.asStateFlow()

    fun requestBiblicalCounsel(userProblem: String) {
        if (userProblem.isBlank()) return
        _isCounselLoading.value = true
        _counselResult.value = null
        viewModelScope.launch {
            val prompt = """
O usuário compartilhou a seguinte angústia ou dúvida: "$userProblem".
Responda com compaixão e acolhimento pastoral:
1. Responda com empatia cristã sem julgamento.
2. Apresente princípios bíblicos consoladores com passagens textuais.
3. Não use linguagem de revelação extra-bíblica ("Deus me falou"), use "Um princípio bíblico que pode ajudar é...".
4. Sugira uma reflexão prática e uma oração para o momento.
5. Recomende comunhão na igreja local e aconselhamento pastoral/profissional qualificado.
            """.trimIndent()
            val instruction = "Você é um conselheiro cristão acolhedor, fundamentado na suficiência e amor das Escrituras."
            val response = GeminiAiService.queryGemini(prompt, instruction)
            _counselResult.value = response
            _isCounselLoading.value = false
        }
    }

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<BibleVerse>>(emptyList())
    val searchResults: StateFlow<List<BibleVerse>> = _searchResults.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.trim().length >= 2) {
            viewModelScope.launch {
                val results = repository.searchVerses(query.trim())
                _searchResults.value = results
            }
        } else {
            _searchResults.value = emptyList()
        }
    }

    // Note actions
    fun saveNote(title: String, content: String, category: String, verse: BibleVerse? = null) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                content = content,
                category = category,
                bookId = verse?.bookId,
                bookName = verse?.bookName,
                chapter = verse?.chapter,
                verse = verse?.verse
            )
            repository.saveNote(note)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    // Prayer actions
    fun savePrayer(title: String, description: String, category: String) {
        viewModelScope.launch {
            repository.savePrayer(PrayerRequest(title = title, description = description, category = category))
        }
    }

    fun togglePrayerAnswered(prayer: PrayerRequest) {
        viewModelScope.launch {
            repository.savePrayer(prayer.copy(isAnswered = !prayer.isAnswered, answeredAt = System.currentTimeMillis()))
        }
    }

    fun deletePrayer(id: Long) {
        viewModelScope.launch {
            repository.deletePrayer(id)
        }
    }

    // Discipleship & Reading Plan Progress
    fun togglePlanDay(planId: String, dayNumber: Int, completed: Boolean) {
        viewModelScope.launch {
            repository.setPlanDayCompleted(planId, dayNumber, completed)
        }
    }

    fun completeOnboarding(goal: String, preferredVersion: String) {
        viewModelScope.launch {
            repository.saveUserSettings(
                userSettings.value.copy(
                    userGoal = goal,
                    preferredVersionId = preferredVersion,
                    isOnboarded = true
                )
            )
        }
    }

    fun updateUserName(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.saveUserSettings(userSettings.value.copy(userName = name.trim()))
        }
    }

    fun togglePremium() {
        viewModelScope.launch {
            repository.saveUserSettings(userSettings.value.copy(isPremium = !userSettings.value.isPremium))
        }
    }

    fun updateFontSizeSp(size: Int) {
        _fontSizeSp.value = size
        viewModelScope.launch {
            repository.saveUserSettings(userSettings.value.copy(fontSizeSp = size))
        }
    }

    fun updateReadingGoal(minutes: Int) {
        viewModelScope.launch {
            repository.saveUserSettings(userSettings.value.copy(dailyReadingGoalMinutes = minutes))
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.shutdown()
    }
}

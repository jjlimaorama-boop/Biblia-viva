package com.example.data.repository

import com.example.data.local.BibleContentProvider
import com.example.data.local.BibleDao
import com.example.data.local.BibleSeedData
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BibleRepository(private val bibleDao: BibleDao) {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDatabaseIfEmpty()
        }
    }

    private suspend fun seedDatabaseIfEmpty() {
        if (bibleDao.getVersionCount() == 0) {
            bibleDao.insertVersions(BibleSeedData.versions)
        }
        if (bibleDao.getBookCount() == 0) {
            bibleDao.insertBooks(BibleSeedData.books)
        }
        if (bibleDao.getVerseCount() == 0) {
            bibleDao.insertVerses(BibleSeedData.keyVerses)
        }
        if (bibleDao.getStudyTopicCount() == 0) {
            bibleDao.insertStudyTopics(BibleSeedData.studyTopics)
        }
        if (bibleDao.getCharacterCount() == 0) {
            bibleDao.insertCharacters(BibleSeedData.biblicalCharacters)
        }
        if (bibleDao.getDevotionalCount() == 0) {
            bibleDao.insertDevotionals(BibleSeedData.devotionals)
        }
        if (bibleDao.getFavoriteCount() == 0) {
            BibleSeedData.initialFavorites.forEach { bibleDao.insertFavorite(it) }
        }
        if (bibleDao.getNoteCount() == 0) {
            BibleSeedData.initialNotes.forEach { bibleDao.insertNote(it) }
        }
        if (bibleDao.getPrayerCount() == 0) {
            BibleSeedData.initialPrayers.forEach { bibleDao.insertPrayer(it) }
        }
        
        val currentSettings = bibleDao.getUserSettings().firstOrNull()
        if (currentSettings == null) {
            bibleDao.saveUserSettings(UserSettings())
        }
    }

    // Books & Versions
    val allBooks: Flow<List<BibleBook>> = bibleDao.getAllBooks()
    val allVersions: Flow<List<BibleVersion>> = bibleDao.getAllVersions()
    val userSettings: Flow<UserSettings?> = bibleDao.getUserSettings()

    suspend fun getBook(bookId: String): BibleBook? = withContext(Dispatchers.IO) {
        bibleDao.getBookById(bookId)
    }

    fun getVerses(versionId: String, bookId: String, chapter: Int): Flow<List<BibleVerse>> {
        return flow {
            withContext(Dispatchers.IO) {
                val existing = bibleDao.getVersesForChapterDirect(versionId, bookId, chapter)
                if (existing.isEmpty()) {
                    val book = bibleDao.getBookById(bookId)
                    if (book != null) {
                        val generated = BibleContentProvider.getChapterVerses(versionId, book, chapter)
                        bibleDao.insertVerses(generated)
                    }
                }
            }
            emitAll(bibleDao.getVersesForChapter(versionId, bookId, chapter))
        }
    }

    suspend fun searchVerses(query: String): List<BibleVerse> = withContext(Dispatchers.IO) {
        bibleDao.searchVerses(query)
    }

    fun getCrossReferences(bookId: String, chapter: Int, verse: Int): Flow<List<CrossReference>> {
        return bibleDao.getCrossReferencesForVerse(bookId, chapter, verse)
    }

    // Notes
    val allNotes: Flow<List<Note>> = bibleDao.getAllNotes()

    fun getNotesForChapter(bookId: String, chapter: Int): Flow<List<Note>> {
        return bibleDao.getNotesForChapter(bookId, chapter)
    }

    suspend fun saveNote(note: Note): Long = withContext(Dispatchers.IO) {
        if (note.id == 0L) {
            bibleDao.insertNote(note)
        } else {
            bibleDao.updateNote(note)
            note.id
        }
    }

    suspend fun deleteNote(id: Long) = withContext(Dispatchers.IO) {
        bibleDao.deleteNote(id)
    }

    // Highlights
    fun getHighlightsForChapter(bookId: String, chapter: Int): Flow<List<Highlight>> {
        return bibleDao.getHighlightsForChapter(bookId, chapter)
    }

    val allHighlights: Flow<List<Highlight>> = bibleDao.getAllHighlights()

    suspend fun addHighlight(bookId: String, chapter: Int, verse: Int, category: String, colorHex: Long) = withContext(Dispatchers.IO) {
        bibleDao.removeHighlight(bookId, chapter, verse)
        bibleDao.insertHighlight(Highlight(bookId = bookId, chapter = chapter, verse = verse, category = category, colorHex = colorHex))
    }

    suspend fun removeHighlight(bookId: String, chapter: Int, verse: Int) = withContext(Dispatchers.IO) {
        bibleDao.removeHighlight(bookId, chapter, verse)
    }

    // Favorites
    val allFavorites: Flow<List<Favorite>> = bibleDao.getAllFavorites()

    fun isFavorite(bookId: String, chapter: Int, verse: Int): Flow<Favorite?> {
        return bibleDao.getFavorite(bookId, chapter, verse)
    }

    suspend fun removeFavorite(bookId: String, chapter: Int, verse: Int) = withContext(Dispatchers.IO) {
        bibleDao.removeFavorite(bookId, chapter, verse)
    }

    suspend fun deleteFavorite(id: Long) = withContext(Dispatchers.IO) {
        bibleDao.deleteFavoriteById(id)
    }

    suspend fun toggleFavorite(bookId: String, bookName: String, chapter: Int, verse: Int, text: String, translation: String) = withContext(Dispatchers.IO) {
        val existing = bibleDao.getFavorite(bookId, chapter, verse).firstOrNull()
        if (existing != null) {
            bibleDao.removeFavorite(bookId, chapter, verse)
        } else {
            bibleDao.insertFavorite(
                Favorite(
                    bookId = bookId,
                    bookName = bookName,
                    chapter = chapter,
                    verse = verse,
                    text = text,
                    translation = translation
                )
            )
        }
    }

    // Prayers
    val allPrayers: Flow<List<PrayerRequest>> = bibleDao.getAllPrayers()

    suspend fun savePrayer(prayer: PrayerRequest): Long = withContext(Dispatchers.IO) {
        if (prayer.id == 0L) bibleDao.insertPrayer(prayer) else { bibleDao.updatePrayer(prayer); prayer.id }
    }

    suspend fun deletePrayer(id: Long) = withContext(Dispatchers.IO) {
        bibleDao.deletePrayer(id)
    }

    // Sermons
    val allSermons: Flow<List<Sermon>> = bibleDao.getAllSermons()

    suspend fun saveSermon(sermon: Sermon): Long = withContext(Dispatchers.IO) {
        bibleDao.insertSermon(sermon)
    }

    suspend fun deleteSermon(id: Long) = withContext(Dispatchers.IO) {
        bibleDao.deleteSermon(id)
    }

    // Reading Plans
    fun getPlanProgress(planId: String): Flow<List<ReadingPlanProgress>> {
        return bibleDao.getPlanProgress(planId)
    }

    suspend fun setPlanDayCompleted(planId: String, dayNumber: Int, completed: Boolean) = withContext(Dispatchers.IO) {
        bibleDao.setPlanDayCompleted(
            ReadingPlanProgress(
                planId = planId,
                dayNumber = dayNumber,
                isCompleted = completed,
                completedAt = if (completed) System.currentTimeMillis() else null
            )
        )
    }

    // Topics & Characters & Devotionals
    val allTopics: Flow<List<StudyTopic>> = bibleDao.getAllStudyTopics()
    val allCharacters: Flow<List<BiblicalCharacter>> = bibleDao.getAllCharacters()
    val allDevotionals: Flow<List<Devotional>> = bibleDao.getAllDevotionals()

    // Settings
    suspend fun saveUserSettings(settings: UserSettings) = withContext(Dispatchers.IO) {
        bibleDao.saveUserSettings(settings)
    }
}

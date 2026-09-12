package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BiblicalCharacter
import com.example.data.model.BibleBook
import com.example.data.model.BibleVerse
import com.example.data.model.BibleVersion
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
import kotlinx.coroutines.flow.Flow

@Dao
interface BibleDao {

    // --- Versions ---
    @Query("SELECT * FROM bible_versions")
    fun getAllVersions(): Flow<List<BibleVersion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVersions(versions: List<BibleVersion>)

    // --- Books ---
    @Query("SELECT * FROM bible_books ORDER BY bookNumber ASC")
    fun getAllBooks(): Flow<List<BibleBook>>

    @Query("SELECT * FROM bible_books WHERE id = :bookId LIMIT 1")
    suspend fun getBookById(bookId: String): BibleBook?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BibleBook>)

    // --- Verses ---
    @Query("SELECT * FROM bible_verses WHERE versionId = :versionId AND bookId = :bookId AND chapter = :chapter ORDER BY verse ASC")
    fun getVersesForChapter(versionId: String, bookId: String, chapter: Int): Flow<List<BibleVerse>>

    @Query("SELECT * FROM bible_verses WHERE versionId = :versionId AND bookId = :bookId AND chapter = :chapter ORDER BY verse ASC")
    suspend fun getVersesForChapterDirect(versionId: String, bookId: String, chapter: Int): List<BibleVerse>

    @Query("SELECT * FROM bible_verses WHERE text LIKE '%' || :query || '%' LIMIT 100")
    suspend fun searchVerses(query: String): List<BibleVerse>

    @Query("SELECT * FROM bible_verses WHERE versionId = :versionId AND bookId = :bookId AND chapter = :chapter AND verse = :verse LIMIT 1")
    suspend fun getSingleVerse(versionId: String, bookId: String, chapter: Int, verse: Int): BibleVerse?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<BibleVerse>)

    @Query("SELECT COUNT(*) FROM bible_verses")
    suspend fun getVerseCount(): Int

    // --- Cross References ---
    @Query("SELECT * FROM cross_references WHERE fromBookId = :bookId AND fromChapter = :chapter AND fromVerse = :verse")
    fun getCrossReferencesForVerse(bookId: String, chapter: Int, verse: Int): Flow<List<CrossReference>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(refs: List<CrossReference>)

    // --- Notes ---
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE bookId = :bookId AND chapter = :chapter ORDER BY updatedAt DESC")
    fun getNotesForChapter(bookId: String, chapter: Int): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: Long)

    // --- Highlights ---
    @Query("SELECT * FROM highlights WHERE bookId = :bookId AND chapter = :chapter")
    fun getHighlightsForChapter(bookId: String, chapter: Int): Flow<List<Highlight>>

    @Query("SELECT * FROM highlights ORDER BY createdAt DESC")
    fun getAllHighlights(): Flow<List<Highlight>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: Highlight): Long

    @Query("DELETE FROM highlights WHERE bookId = :bookId AND chapter = :chapter AND verse = :verse")
    suspend fun removeHighlight(bookId: String, chapter: Int, verse: Int)

    // --- Favorites ---
    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE bookId = :bookId AND chapter = :chapter AND verse = :verse LIMIT 1")
    fun getFavorite(bookId: String, chapter: Int, verse: Int): Flow<Favorite?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite): Long

    @Query("DELETE FROM favorites WHERE bookId = :bookId AND chapter = :chapter AND verse = :verse")
    suspend fun removeFavorite(bookId: String, chapter: Int, verse: Int)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: Long)

    // --- Prayers ---
    @Query("SELECT * FROM prayer_requests ORDER BY createdAt DESC")
    fun getAllPrayers(): Flow<List<PrayerRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayer(prayer: PrayerRequest): Long

    @Update
    suspend fun updatePrayer(prayer: PrayerRequest)

    @Query("DELETE FROM prayer_requests WHERE id = :id")
    suspend fun deletePrayer(id: Long)

    // --- Reading Plans Progress ---
    @Query("SELECT * FROM reading_plan_progress WHERE planId = :planId")
    fun getPlanProgress(planId: String): Flow<List<ReadingPlanProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setPlanDayCompleted(progress: ReadingPlanProgress)

    // --- Sermons ---
    @Query("SELECT * FROM sermons ORDER BY createdAt DESC")
    fun getAllSermons(): Flow<List<Sermon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSermon(sermon: Sermon): Long

    @Query("DELETE FROM sermons WHERE id = :id")
    suspend fun deleteSermon(id: Long)

    // --- Topics ---
    @Query("SELECT * FROM study_topics ORDER BY title ASC")
    fun getAllStudyTopics(): Flow<List<StudyTopic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyTopics(topics: List<StudyTopic>)

    // --- Characters ---
    @Query("SELECT * FROM biblical_characters ORDER BY name ASC")
    fun getAllCharacters(): Flow<List<BiblicalCharacter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<BiblicalCharacter>)

    // --- Devotionals ---
    @Query("SELECT * FROM devotionals ORDER BY dayNumber ASC")
    fun getAllDevotionals(): Flow<List<Devotional>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevotionals(devotionals: List<Devotional>)

    // --- User Settings ---
    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    fun getUserSettings(): Flow<UserSettings?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserSettings(settings: UserSettings)

    // --- Table Counts for Seeding ---
    @Query("SELECT COUNT(*) FROM bible_versions")
    suspend fun getVersionCount(): Int

    @Query("SELECT COUNT(*) FROM bible_books")
    suspend fun getBookCount(): Int

    @Query("SELECT COUNT(*) FROM study_topics")
    suspend fun getStudyTopicCount(): Int

    @Query("SELECT COUNT(*) FROM biblical_characters")
    suspend fun getCharacterCount(): Int

    @Query("SELECT COUNT(*) FROM devotionals")
    suspend fun getDevotionalCount(): Int

    @Query("SELECT COUNT(*) FROM favorites")
    suspend fun getFavoriteCount(): Int

    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNoteCount(): Int

    @Query("SELECT COUNT(*) FROM prayer_requests")
    suspend fun getPrayerCount(): Int
}

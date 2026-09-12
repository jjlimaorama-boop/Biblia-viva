package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bible_versions")
data class BibleVersion(
    @PrimaryKey val id: String,
    val name: String,
    val abbreviation: String,
    val copyrightInfo: String,
    val isPublicDomain: Boolean = true,
    val isDefault: Boolean = false
)

@Entity(tableName = "bible_books")
data class BibleBook(
    @PrimaryKey val id: String, // e.g. "GEN", "MAT", "JHN"
    val bookNumber: Int,
    val name: String,
    val abbreviation: String,
    val testament: String, // "AT" or "NT"
    val category: String, // "Pentateuco", "Históricos", "Poéticos", "Profetas Maiores", "Profetas Menores", "Evangelhos", "Histórico", "Cartas de Paulo", "Cartas Gerais", "Profético"
    val chapterCount: Int,
    val author: String,
    val historicalContext: String,
    val christCenteredTheme: String
)

@Entity(tableName = "bible_verses")
data class BibleVerse(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val versionId: String,
    val bookId: String,
    val bookName: String,
    val chapter: Int,
    val verse: Int,
    val text: String
)

@Entity(tableName = "cross_references")
data class CrossReference(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fromBookId: String,
    val fromChapter: Int,
    val fromVerse: Int,
    val toBookName: String,
    val toChapter: Int,
    val toVerse: Int,
    val relationType: String, // "Cumprimento", "Profecia", "Tema Paralelo", "Citação NT", "Cristocêntrico"
    val explanation: String
)

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val bookId: String? = null,
    val bookName: String? = null,
    val chapter: Int? = null,
    val verse: Int? = null,
    val category: String = "Estudos", // "Sermões", "Estudos", "Devocional", "Ideias", "Oração", "Reflexões", "EBD", "Discipulado"
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "highlights")
data class Highlight(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val chapter: Int,
    val verse: Int,
    val category: String, // "Promessas", "Doutrina", "Aplicação", "Evangelho", "Oração"
    val colorHex: Long,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String,
    val bookName: String,
    val chapter: Int,
    val verse: Int,
    val text: String,
    val translation: String,
    val tags: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "prayer_requests")
data class PrayerRequest(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // "Família", "Igreja", "Trabalho", "Estudos", "Vida espiritual", "Missões", "Gratidão", "Intercessão"
    val isAnswered: Boolean = false,
    val testimony: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val answeredAt: Long? = null
)

@Entity(tableName = "reading_plan_progress")
data class ReadingPlanProgress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val planId: String,
    val dayNumber: Int,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)

@Entity(tableName = "sermons")
data class Sermon(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val baseText: String,
    val centralTheme: String,
    val targetAudience: String,
    val durationMinutes: Int,
    val messageType: String,
    val introduction: String,
    val historicalContext: String,
    val mainPoints: String, // JSON or structured text
    val christConnection: String,
    val gospelApplication: String,
    val illustrations: String,
    val conclusion: String,
    val prayer: String,
    val isChristCenteredValidated: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_topics")
data class StudyTopic(
    @PrimaryKey val id: String,
    val title: String,
    val definition: String,
    val mainScriptures: String,
    val historicalContext: String,
    val relatedCharacters: String,
    val christConnection: String,
    val practicalApplications: String,
    val studyQuestions: String
)

@Entity(tableName = "biblical_characters")
data class BiblicalCharacter(
    @PrimaryKey val id: String,
    val name: String,
    val period: String,
    val biography: String,
    val keyEvents: String,
    val virtues: String,
    val flaws: String,
    val lessons: String,
    val relatedVerses: String,
    val redemptiveRole: String,
    val application: String
)

@Entity(tableName = "devotionals")
data class Devotional(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayNumber: Int,
    val title: String,
    val scriptureRef: String,
    val scriptureText: String,
    val theme: String,
    val reflection: String,
    val practicalApplication: String,
    val reflectionQuestion: String,
    val prayer: String,
    val complementaryReading: String
)

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Estudante da Palavra",
    val preferredVersionId: String = "ARC",
    val fontSizeSp: Int = 18,
    val lineSpacingMultiplier: Float = 1.4f,
    val readingTheme: String = "parchment", // "light", "dark", "parchment"
    val currentBookId: String = "JHN",
    val currentChapter: Int = 3,
    val readingStreakDays: Int = 5,
    val totalChaptersRead: Int = 42,
    val isPremium: Boolean = false,
    val dailyReadingGoalMinutes: Int = 15,
    val notificationsEnabled: Boolean = true,
    val userGoal: String = "Conhecer melhor a Cristo através das Escrituras",
    val isOnboarded: Boolean = true
)

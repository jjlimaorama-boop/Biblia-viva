package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.BibleContentProvider
import com.example.data.model.BibleBook
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Bíblia Viva", appName)
  }

  @Test
  fun `verify bible content provider returns verses for any book and chapter`() {
    val genBook = BibleBook("GEN", 1, "Gênesis", "Gn", "AT", "Pentateuco", 50, "Moisés", "", "")
    val matBook = BibleBook("MAT", 40, "Mateus", "Mt", "NT", "Evangelhos", 28, "Mateus", "", "")

    val genVerses = BibleContentProvider.getChapterVerses("ARC", genBook, 1)
    val matVerses = BibleContentProvider.getChapterVerses("ARC", matBook, 1)

    assertTrue("Genesis 1 should have verses", genVerses.isNotEmpty())
    assertTrue("Matthew 1 should have verses", matVerses.isNotEmpty())
    assertEquals(1, genVerses.first().chapter)
    assertEquals("GEN", genVerses.first().bookId)
  }

  @Test
  fun `verify study topics, characters, and initial user items are properly configured`() {
    val topics = com.example.data.local.BibleSeedData.studyTopics
    val characters = com.example.data.local.BibleSeedData.biblicalCharacters
    val notes = com.example.data.local.BibleSeedData.initialNotes
    val favorites = com.example.data.local.BibleSeedData.initialFavorites
    val prayers = com.example.data.local.BibleSeedData.initialPrayers

    assertTrue("Study topics should be populated", topics.isNotEmpty())
    assertTrue("Biblical characters should be populated", characters.isNotEmpty())
    assertTrue("Initial notes should be populated", notes.isNotEmpty())
    assertTrue("Initial favorites should be populated", favorites.isNotEmpty())
    assertTrue("Initial prayers should be populated", prayers.isNotEmpty())
  }
}


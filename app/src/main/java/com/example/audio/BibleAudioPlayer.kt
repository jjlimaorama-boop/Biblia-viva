package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.data.model.BibleVerse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val currentVerseIndex: Int = 0,
    val totalVerses: Int = 0,
    val currentText: String = "",
    val currentReference: String = "",
    val speed: Float = 1.0f
)

class BibleAudioPlayer(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

    private var activeVerseList: List<BibleVerse> = emptyList()
    private var currentIdx: Int = 0

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("pt", "BR"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.language = Locale.getDefault()
            }
            tts?.setSpeechRate(_playerState.value.speed)
            isInitialized = true

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _playerState.value = _playerState.value.copy(isPlaying = true, isPaused = false)
                }

                override fun onDone(utteranceId: String?) {
                    playNextVerse()
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _playerState.value = _playerState.value.copy(isPlaying = false, isPaused = false)
                }
            })
        }
    }

    fun playChapter(bookName: String, chapter: Int, verses: List<BibleVerse>) {
        if (verses.isEmpty()) return
        stop()
        activeVerseList = verses
        currentIdx = 0
        val verse = verses[0]
        val ref = "$bookName $chapter"
        _playerState.value = _playerState.value.copy(
            isPlaying = true,
            isPaused = false,
            currentVerseIndex = 0,
            totalVerses = verses.size,
            currentReference = ref,
            currentText = "Versículo ${verse.verse}: ${verse.text}"
        )
        speakVerse(verse, ref)
    }

    fun playSingleVerse(bookName: String, chapter: Int, verse: BibleVerse) {
        stop()
        activeVerseList = listOf(verse)
        currentIdx = 0
        val ref = "$bookName $chapter:${verse.verse}"
        _playerState.value = _playerState.value.copy(
            isPlaying = true,
            isPaused = false,
            currentVerseIndex = 0,
            totalVerses = 1,
            currentReference = ref,
            currentText = verse.text
        )
        speakVerse(verse, ref)
    }

    private fun speakVerse(verse: BibleVerse, ref: String) {
        if (!isInitialized) return
        val textToSpeak = "Versículo ${verse.verse}. ${verse.text}"
        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "verse_${verse.verse}")
    }

    fun pause() {
        tts?.stop()
        _playerState.value = _playerState.value.copy(isPlaying = false, isPaused = true)
    }

    fun resume() {
        if (currentIdx in activeVerseList.indices) {
            val verse = activeVerseList[currentIdx]
            _playerState.value = _playerState.value.copy(isPlaying = true, isPaused = false)
            speakVerse(verse, _playerState.value.currentReference)
        }
    }

    fun stop() {
        tts?.stop()
        _playerState.value = _playerState.value.copy(isPlaying = false, isPaused = false)
    }

    fun playNextVerse() {
        if (currentIdx + 1 < activeVerseList.size) {
            currentIdx++
            val verse = activeVerseList[currentIdx]
            _playerState.value = _playerState.value.copy(
                currentVerseIndex = currentIdx,
                currentText = "Versículo ${verse.verse}: ${verse.text}"
            )
            speakVerse(verse, _playerState.value.currentReference)
        } else {
            _playerState.value = _playerState.value.copy(isPlaying = false, isPaused = false)
        }
    }

    fun playPreviousVerse() {
        if (currentIdx > 0) {
            currentIdx--
            val verse = activeVerseList[currentIdx]
            _playerState.value = _playerState.value.copy(
                currentVerseIndex = currentIdx,
                currentText = "Versículo ${verse.verse}: ${verse.text}"
            )
            speakVerse(verse, _playerState.value.currentReference)
        }
    }

    fun cycleSpeed(): Float {
        val nextSpeed = when (_playerState.value.speed) {
            0.75f -> 1.0f
            1.0f -> 1.25f
            1.25f -> 1.5f
            else -> 0.75f
        }
        tts?.setSpeechRate(nextSpeed)
        _playerState.value = _playerState.value.copy(speed = nextSpeed)
        return nextSpeed
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.BibleAudioPlayer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary

@Composable
fun AudioMiniPlayer(
    audioPlayer: BibleAudioPlayer,
    modifier: Modifier = Modifier
) {
    val state by audioPlayer.playerState.collectAsState()

    if (state.isPlaying || state.isPaused) {
        Surface(
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            color = NavyDark,
            shadowElevation = 12.dp,
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            listOf(Color(0xFF0B1329), Color(0xFF1E293B))
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Info
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NavyPrimary.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Headphones,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = state.currentReference.ifBlank { "Áudio Bíblico" },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = if (state.isPlaying) "Reproduzindo agora..." else "Pausado",
                                fontSize = 11.sp,
                                color = GoldAccent,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Playback Controls
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Speed button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0x22FFFFFF),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            TextButton(
                                onClick = { audioPlayer.cycleSpeed() },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${state.speed}x",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent
                                )
                            }
                        }

                        // Prev Verse
                        IconButton(
                            onClick = { audioPlayer.playPreviousVerse() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = "Versículo Anterior",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Play / Pause
                        FilledIconButton(
                            onClick = {
                                if (state.isPlaying) audioPlayer.pause() else audioPlayer.resume()
                            },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = GoldAccent),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (state.isPlaying) "Pausar" else "Continuar",
                                tint = NavyDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Next Verse
                        IconButton(
                            onClick = { audioPlayer.playNextVerse() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = "Próximo Versículo",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Stop
                        IconButton(
                            onClick = { audioPlayer.stop() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fechar Áudio",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

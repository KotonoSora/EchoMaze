package com.kotonosora.echomaze.engine

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Rect
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

data class Pulse(
    val id: Long,
    val centerX: Float,
    val centerY: Float,
    val maxRadius: Float,
    val duration: Long,
    val startTime: Long = System.currentTimeMillis()
) {
    fun getProgress(currentTime: Long): Float {
        val elapsed = currentTime - startTime
        return (elapsed.toFloat() / duration).coerceIn(0f, 1f)
    }

    fun getCurrentRadius(currentTime: Long): Float {
        return maxRadius * getProgress(currentTime)
    }

    fun isExpired(currentTime: Long): Boolean {
        return currentTime - startTime >= duration
    }

    fun getVisibilityAt(rect: Rect, currentTime: Long): Float {
        val radius = getCurrentRadius(currentTime)
        val thickness = 200f // Increased for better visual feedback
        
        // Closest point on rect to pulse center
        val closestX = max(rect.left, min(centerX, rect.right))
        val closestY = max(rect.top, min(centerY, rect.bottom))
        
        val dx = centerX - closestX
        val dy = centerY - closestY
        val distance = sqrt(dx * dx + dy * dy)
        
        if (distance <= radius) {
            val waveEdgeDist = radius - distance
            if (waveEdgeDist < thickness) {
                // Wall is hit by the pulse wave. 
                // Visibility is high at the edge and fades inward.
                val edgeAlpha = (1f - (waveEdgeDist / thickness)).coerceIn(0.2f, 1f)
                // Also fade the entire pulse out over time
                return edgeAlpha * (1f - getProgress(currentTime))
            }
        }
        return 0f
    }
}

class PulseEngine(private val scope: CoroutineScope) {
    private val _activePulses = mutableStateListOf<Pulse>()
    val activePulses: List<Pulse> get() = _activePulses

    private var pulseIdCounter = 0L

    fun triggerPulse(x: Float, y: Float, radius: Float, duration: Long) {
        val pulse = Pulse(
            id = pulseIdCounter++,
            centerX = x,
            centerY = y,
            maxRadius = radius,
            duration = duration
        )
        _activePulses.add(pulse)
        
        scope.launch {
            delay(duration)
            _activePulses.removeAll { it.id == pulse.id }
        }
    }
}

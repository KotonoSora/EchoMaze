package com.jn.echomaze.engine

import android.content.Context
import android.media.SoundPool
import com.jn.echomaze.R

class SoundManager(context: Context) {

    private val soundPool: SoundPool

    companion object {
        // Sound IDs will be loaded here
        var EXPLOSION_ID = 0
    }

    init {
        // Initialize SoundPool for modern Android versions (API 21+)
        soundPool = SoundPool.Builder().setMaxStreams(5).build()

        // Load sounds
        // NOTE: You must place an 'explosion.wav' file in app/src/main/res/raw/
        EXPLOSION_ID = soundPool.load(context, R.raw.sfx_start, 1)
    }

    fun playExplosion() {
        // Play sound at full volume, loop 0 times, priority 1, no repeat
        soundPool.play(EXPLOSION_ID, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    fun release() {
        soundPool.release()
    }
}
package com.jn.echomaze.engine

import android.content.Context
import android.media.SoundPool
import com.jn.echomaze.R

class SoundManager(context: Context) {

    private val soundPool: SoundPool
    private val sounds = mutableMapOf<String, Int>()

    init {
        soundPool = SoundPool.Builder().setMaxStreams(5).build()

        // Load generated sounds
        sounds["click"] = soundPool.load(context, R.raw.sfx_click, 1)
        sounds["move"] = soundPool.load(context, R.raw.sfx_move, 1)
        sounds["win"] = soundPool.load(context, R.raw.sfx_win, 1)
        sounds["lose"] = soundPool.load(context, R.raw.sfx_lose, 1)
    }

    fun playClick() = play("click")
    fun playMove() = play("move")
    fun playWin() = play("win")
    fun playLose() = play("lose")

    private fun play(key: String) {
        sounds[key]?.let { id ->
            soundPool.play(id, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    fun release() {
        soundPool.release()
    }
}

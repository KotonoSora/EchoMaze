package com.jn.echomaze.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.jn.echomaze.domain.repository.GameRepository

class SettingsViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {
    // Logic for sound, music, etc can be added here if moved to repository
}

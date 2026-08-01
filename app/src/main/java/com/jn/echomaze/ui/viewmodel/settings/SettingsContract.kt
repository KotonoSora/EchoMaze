package com.jn.echomaze.ui.viewmodel.settings

import com.jn.echomaze.domain.model.UserStats

data class SettingsUiState(
    val stats: UserStats? = null,
    val coinBalance: Int = 0
)

sealed interface SettingsEvent {
    data class OnSoundToggled(val enabled: Boolean) : SettingsEvent
    data class OnMusicToggled(val enabled: Boolean) : SettingsEvent
    data object OnResetStats : SettingsEvent
}

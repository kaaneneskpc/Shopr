package com.kaaneneskpc.shopr.ui.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaaneneskpc.domain.model.UserProfile
import com.kaaneneskpc.domain.network.ResultWrapper
import com.kaaneneskpc.domain.usecase.GetUserProfileUseCase
import com.kaaneneskpc.domain.usecase.UpdatePasswordUseCase
import com.kaaneneskpc.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _passwordUpdateState = MutableStateFlow<PasswordUpdateState>(PasswordUpdateState.Idle)
    val passwordUpdateState: StateFlow<PasswordUpdateState> = _passwordUpdateState.asStateFlow()

    // Geçici olarak sabit bir userId kullanıyoruz, gerçek uygulamada bu değer oturum yönetiminden gelmelidir
    private val userId = 1

    init {
        getUserProfile()
    }

    fun getUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            when (val result = getUserProfileUseCase.execute(userId)) {
                is ResultWrapper.Success -> {
                    _uiState.value = ProfileUiState.Success(result.value)
                }
                is ResultWrapper.Failure -> {
                    _uiState.value = ProfileUiState.Error(result.exception.message ?: "Bir hata oluştu")
                }
            }
        }
    }

    fun updateUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            when (val result = updateUserProfileUseCase.execute(userProfile)) {
                is ResultWrapper.Success -> {
                    _uiState.value = ProfileUiState.Success(result.value)
                }
                is ResultWrapper.Failure -> {
                    _uiState.value = ProfileUiState.Error(result.exception.message ?: "Bir hata oluştu")
                }
            }
        }
    }

    fun updatePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _passwordUpdateState.value = PasswordUpdateState.Loading
            when (val result = updatePasswordUseCase.execute(userId, oldPassword, newPassword)) {
                is ResultWrapper.Success -> {
                    _passwordUpdateState.value = PasswordUpdateState.Success
                }
                is ResultWrapper.Failure -> {
                    _passwordUpdateState.value = PasswordUpdateState.Error(result.exception.message ?: "Bir hata oluştu")
                }
            }
        }
    }

    fun resetPasswordUpdateState() {
        _passwordUpdateState.value = PasswordUpdateState.Idle
    }
}

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val userProfile: UserProfile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

sealed class PasswordUpdateState {
    data object Idle : PasswordUpdateState()
    data object Loading : PasswordUpdateState()
    data object Success : PasswordUpdateState()
    data class Error(val message: String) : PasswordUpdateState()
} 
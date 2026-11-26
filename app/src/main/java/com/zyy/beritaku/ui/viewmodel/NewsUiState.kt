package com.zyy.beritaku.ui.viewmodel

import com.zyy.beritaku.data.model.Article

sealed interface NewsUiState {
    object Loading : NewsUiState
    object ApiKeyMissing : NewsUiState
    data class Success(
        val articles: List<Article>,
        val selectedCategory: String = "general"
    ) : NewsUiState
    data class Error(val message: String) : NewsUiState
}

package com.ramoncinp.tvmaze.ui.show

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.tvmaze.domain.model.ShowDetailState
import com.ramoncinp.tvmaze.domain.repository.TvMazeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShowDetailViewModel @Inject constructor(
    private val repository: TvMazeRepository
): ViewModel() {

    private val _showDetailState = MutableStateFlow(ShowDetailState())
    val showDetailState = _showDetailState.asStateFlow()

    fun getShowDetail(showId: Int) {
        viewModelScope.launch {
            val response = repository.getShow(showId.toString())
            if (response.error != null) {
                _showDetailState.update {
                    it.copy(
                        error = response.error,
                        isLoading = false
                    )
                }
            } else {
                response.data?.let { show ->
                    Timber.d("Show detail: $show")
                    _showDetailState.update {
                        it.copy(
                            data = show,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}

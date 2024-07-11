package com.laurens.klasifikasibanana.view.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.laurens.klasifikasibanana.Repository.AnalysisResultRepository
import com.laurens.klasifikasibanana.database.AnalysisResult
import com.laurens.klasifikasibanana.database.AppDatabase
import kotlinx.coroutines.launch

class HistoryViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: AnalysisResultRepository
    val allResults: LiveData<List<AnalysisResult>>

    init {
        val analysisResultDao = AppDatabase.getDatabase(application).analysisResultDao()
        repository = AnalysisResultRepository(analysisResultDao)
        allResults = repository.allResults.asLiveData()
    }


    fun delete(analysisResult: AnalysisResult) = viewModelScope.launch {
        repository.delete(analysisResult)
    }
}

class AnalysisResultViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
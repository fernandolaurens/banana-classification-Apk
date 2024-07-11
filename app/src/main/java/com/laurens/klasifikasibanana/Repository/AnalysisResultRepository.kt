package com.laurens.klasifikasibanana.Repository

import androidx.annotation.WorkerThread
import com.laurens.klasifikasibanana.database.AnalysisResult
import com.laurens.klasifikasibanana.database.AnalysisResultDao
import kotlinx.coroutines.flow.Flow

class AnalysisResultRepository(private val analysisResultDao: AnalysisResultDao) {

    val allResults: Flow<List<AnalysisResult>> = analysisResultDao.getAll()

    @WorkerThread
    suspend fun delete(analysisResult: AnalysisResult) {
        analysisResultDao.delete(analysisResult)
    }
}
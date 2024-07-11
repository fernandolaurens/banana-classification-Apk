package com.laurens.klasifikasibanana.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.laurens.klasifikasibanana.R
import com.laurens.klasifikasibanana.adapter.HistoryAdapter
import com.laurens.klasifikasibanana.database.AnalysisResult
import com.laurens.klasifikasibanana.databinding.FragmentHistoryBinding
import kotlinx.coroutines.launch

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter

    private val analysisResultViewModel: HistoryViewModel by viewModels {
        AnalysisResultViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(emptyList()) { analysisResult ->
            deleteResult(analysisResult)
        }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun observeData() {
        analysisResultViewModel.allResults.observe(viewLifecycleOwner) { results ->
            historyAdapter.updateData(results)
        }
    }

    private fun deleteResult(analysisResult: AnalysisResult) {
        lifecycleScope.launch {
            analysisResultViewModel.delete(analysisResult)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
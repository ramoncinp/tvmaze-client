package com.ramoncinp.tvmaze.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.ramoncinp.tvmaze.R
import com.ramoncinp.tvmaze.databinding.FragmentScheduleBinding
import com.ramoncinp.tvmaze.domain.model.ScheduleState
import com.ramoncinp.tvmaze.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

    private val adapter: ScheduleListAdapter by lazy { ScheduleListAdapter() }
    private val viewModel: ScheduleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
        viewModel.getSchedule()
    }

    private fun initViews() {
        binding.scheduleList.adapter = adapter
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.scheduleState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { onScheduleState(it) }
        }
    }

    private fun onScheduleState(state: ScheduleState) {
        with(binding) {
            progressBar.isVisible = state.isLoading
            scheduleList.isVisible = !state.isLoading && state.error == null
            errorText.isVisible = state.error != null

            if (state.error != null) {
                errorText.text = state.error
            } else if (state.data.isNotEmpty()) {
                adapter.submitList(state.data)
            } else {
                errorText.text = getString(R.string.there_is_no_schedule)
            }
        }
    }
}

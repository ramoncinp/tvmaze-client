package com.ramoncinp.tvmaze.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.ramoncinp.tvmaze.R
import com.ramoncinp.tvmaze.databinding.FragmentScheduleBinding
import com.ramoncinp.tvmaze.domain.model.ScheduleState
import com.ramoncinp.tvmaze.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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
    }

    private fun initViews() {
        initChipGroup()
        binding.scheduleList.adapter = adapter
    }

    private fun initChipGroup() {
        binding.datesChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip = group[checkedIds.first() - 1] as Chip
            viewModel.selectDate(chip.text.toString())
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.scheduleState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { onScheduleState(it) }
        }

        viewModel.availableDates.observe(viewLifecycleOwner) {
            setChipsOptions(it)
        }
    }

    private fun setChipsOptions(dates: List<String>) {
        binding.datesChipGroup.isVisible = true
        binding.datesChipGroup.removeAllViews()
        dates.forEachIndexed { index, date ->
            binding.datesChipGroup.addView(
                createChip(date, index == 0)
            )
        }
    }

    private fun createChip(date: String, isFirst: Boolean): View {
        return Chip(requireContext()).apply {
            isCheckable = true
            text = date
            isChecked = isFirst
            chipBackgroundColor = resources.getColorStateList(R.color.chip_background_color, null)
        }
    }

    private fun onScheduleState(state: ScheduleState) {
        with(binding) {
            val contentVisible = !state.isLoading && state.error == null
            progressBar.isVisible = state.isLoading
            scheduleContainer.isVisible = contentVisible
            errorText.isVisible = state.error != null

            if (state.error != null) {
                errorText.text = state.error
            } else if (state.data.isNotEmpty()) {
                adapter.submitList(state.data)
                scheduleList.post { scheduleList.scrollToPosition(0) }
            } else {
                errorText.text = getString(R.string.there_is_no_schedule)
            }
        }
    }
}

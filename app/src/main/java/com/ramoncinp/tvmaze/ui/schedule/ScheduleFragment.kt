package com.ramoncinp.tvmaze.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.ramoncinp.tvmaze.R
import com.ramoncinp.tvmaze.databinding.FragmentScheduleBinding
import com.ramoncinp.tvmaze.domain.model.ChipState
import com.ramoncinp.tvmaze.domain.model.ScheduleState
import com.ramoncinp.tvmaze.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

    private val adapter: ScheduleListAdapter by lazy { ScheduleListAdapter { navigateToShowDetail(it.show.id) } }
    private val viewModel: ScheduleViewModel by viewModels()
    private var shouldScrollToTop = false

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
        binding.scheduleList.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private fun initChipGroup() {
        binding.datesChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val idx = (checkedIds.first() - 1) % group.size
            val chip = group[idx] as Chip
            viewModel.selectDate(chip.text.toString())
            shouldScrollToTop = true
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

    private fun setChipsOptions(dates: List<ChipState>) {
        binding.datesChipGroup.isVisible = true
        binding.datesChipGroup.removeAllViews()
        dates.forEach { date ->
            binding.datesChipGroup.addView(
                createChip(date)
            )
        }
    }

    private fun createChip(chipState: ChipState): View {
        return Chip(requireContext()).apply {
            isCheckable = true
            text = chipState.name
            isChecked = chipState.checked
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
                processScrollToTop()
            } else {
                errorText.text = getString(R.string.there_is_no_schedule)
            }
        }
    }

    private fun processScrollToTop() {
        if (shouldScrollToTop) {
            binding.scheduleList.post { binding.scheduleList.smoothScrollToPosition(0) }
            shouldScrollToTop = false
        }
    }

    private fun navigateToShowDetail(id: Int) {
        findNavController().navigate(
            ScheduleFragmentDirections.scheduleToShowDetail(id)
        )
    }
}

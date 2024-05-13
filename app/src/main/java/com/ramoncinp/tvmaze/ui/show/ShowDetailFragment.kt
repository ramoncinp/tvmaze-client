package com.ramoncinp.tvmaze.ui.show

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ramoncinp.tvmaze.data.model.Image
import com.ramoncinp.tvmaze.data.model.Network
import com.ramoncinp.tvmaze.data.model.Rating
import com.ramoncinp.tvmaze.databinding.FragmentShowDetailBinding
import com.ramoncinp.tvmaze.domain.model.ShowDetailState
import com.ramoncinp.tvmaze.ui.BaseFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowDetailFragment : BaseFragment<FragmentShowDetailBinding>() {

    private val viewModel: ShowDetailViewModel by viewModels()
    private val args: ShowDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        getData()
    }

    private fun getData() {
        val showId = args.showId
        viewModel.getShowDetail(showId)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.showDetailState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { state ->
                    bindShowData(state)
                }
        }
    }

    private fun bindShowData(state: ShowDetailState) {
        with(binding) {
            if (state.isLoading) {
                progressBar.isVisible = true
            } else {
                progressBar.isVisible = false
                state.data?.let { show ->
                    showName.text = show.name
                    setSummary(show.summary)
                    setRating(show.rating)
                    setNetwork(show.network)
                    setShowImage(show.image)
                }
            }
        }
    }

    private fun setSummary(summary: String?) {
        summary?.let {
            binding.summaryTv.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.summaryTv.isVisible = true
        }
    }

    private fun setRating(rating: Rating?) {
        rating?.average?.let {
            binding.ratingGroup.isVisible = true
            binding.ratingTv.text = it.toString()
        }
    }

    private fun setNetwork(network: Network?) {
        network?.let {
            binding.networkGroup.isVisible = true
            binding.networkTv.text = it.name
        }
    }

    private fun setShowImage(image: Image?) {
        image?.let {
            Picasso.get().load(it.original).into(binding.showImage)
        } ?: run {
            binding.showImage.isVisible = false
        }
    }
}

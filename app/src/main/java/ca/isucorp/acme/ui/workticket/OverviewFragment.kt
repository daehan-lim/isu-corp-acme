package ca.isucorp.acme.ui.workticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.isucorp.acme.databinding.FragmentOverviewBinding


class OverviewFragment:  Fragment() {
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater)

        return binding.root
    }
}
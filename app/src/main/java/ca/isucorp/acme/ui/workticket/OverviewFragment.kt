package ca.isucorp.acme.ui.workticket

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ca.isucorp.acme.databinding.FragmentOverviewBinding
import ca.isucorp.acme.util.makeScrollableInsideScrollView


class OverviewFragment:  Fragment() {
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOverviewBinding.inflate(layoutInflater)

        binding.textNotes.movementMethod = ScrollingMovementMethod()
        binding.textNotes.makeScrollableInsideScrollView()

        return binding.root
    }
}
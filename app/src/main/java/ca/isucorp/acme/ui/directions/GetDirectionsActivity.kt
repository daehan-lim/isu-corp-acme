package ca.isucorp.acme.ui.directions

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityGetDirectionsBinding
import ca.isucorp.acme.util.DEFAULT_GO_BACK_ANIMATION
import ca.isucorp.acme.util.setUpInActivity

class GetDirectionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGetDirectionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetDirectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.layoutSimpleAppBar.toolbar
        val toolBarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        toolBarTitle.text = getString(R.string.get_directions)
        toolbar.setUpInActivity(this, DEFAULT_GO_BACK_ANIMATION)


    }
}
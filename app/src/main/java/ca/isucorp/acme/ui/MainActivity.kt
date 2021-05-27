package ca.isucorp.acme.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ca.isucorp.acme.R
import ca.isucorp.acme.databinding.ActivityMainBinding
import ca.isucorp.acme.util.increaseMenuItemTextSize
import java.util.*


const val PHONE_PERMISSION_CODE = 102
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val toolbar = binding.layoutAppBar.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val menuButton = toolbar.findViewById<ImageView>(R.id.button_menu)
        menuButton.setOnClickListener {
            showDropdownMenu(menuButton)
        }
    }

    private fun showDropdownMenu(menuButton: ImageView?) {
        val popup = PopupMenu(this, menuButton)
        popup.inflate(R.menu.dropdown_menu)
        increaseMenuItemTextSize(popup, R.id.action_work_ticker)
        increaseMenuItemTextSize(popup, R.id.action_get_directions)

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_work_ticker -> {
                    Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.action_get_directions -> {
                    Toast.makeText(this@MainActivity, item.title, Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

        popup.show()
    }




}
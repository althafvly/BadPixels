package tk.al54.dev.badpixels

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import tk.al54.dev.badpixels.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private val colors = intArrayOf(
        Color.BLACK, Color.RED, Color.GREEN, Color.BLUE,
        Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.WHITE
    )
    private var colorIndex = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.versionText.text = getString(
            R.string.version_display,
            getString(R.string.version),
            BuildConfig.VERSION_NAME,
            getString(R.string.build),
            BuildConfig.VERSION_CODE
        )

        binding.rootView.onSwipeOrClick = { direction ->
            nextColor(direction)
        }
    }

    private fun nextColor(direction: Int) {
        if (binding.infoOverlay.visibility == View.VISIBLE) {
            binding.infoOverlay.visibility = View.GONE
        }
        colorIndex = (colorIndex + direction + colors.size) % colors.size
        binding.rootView.setBackgroundColor(colors[colorIndex])
    }
}

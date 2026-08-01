package tk.al54.dev.badpixels

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import kotlin.math.abs

class TouchFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var onSwipeOrClick: ((Int) -> Unit)? = null
    private var startX = 0f
    private var startY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_UP -> {
                val dx = event.x - startX
                val dy = event.y - startY
                if (abs(dx) > 120f || abs(dy) > 120f) {
                    val dir = if (abs(dx) > abs(dy)) if (dx < 0) 1 else -1 else if (dy < 0) 1 else -1
                    onSwipeOrClick?.invoke(dir)
                } else {
                    onSwipeOrClick?.invoke(1)
                }
                performClick()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}

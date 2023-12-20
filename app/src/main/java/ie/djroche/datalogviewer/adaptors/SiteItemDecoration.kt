package ie.djroche.datalogviewer.adaptors
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ie.djroche.datalogviewer.R

// reference https://www.tutorialspoint.com/how-to-properly-highlight-selected-items-on-android-recyclerview-using-kotlin

class SiteItemDecoration (context: Context) : RecyclerView.ItemDecoration() {
    private val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.line_divider)!!
    override fun onDrawOver(
        @NonNull canvas: Canvas,
        parent: RecyclerView,
        @NonNull state: RecyclerView.State
    )
    {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + drawable.intrinsicHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }
}
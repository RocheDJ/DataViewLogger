package ie.djroche.datalogviewer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
//ref:https://stackoverflow.com/questions/11734803/load-an-image-from-assets-folder
fun String.assetsToBitmap(context: Context): Bitmap? {
    return try {
        val assetManager = context.assets
        val inputStream = assetManager.open(this)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
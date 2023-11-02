package ie.djroche.datalogviewer.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.helpers.assetsToBitmap
import ie.djroche.datalogviewer.models.SiteKPIModel

internal class GridRVAdapter(private val kpiList: List<SiteKPIModel>,
                             private val context: Context
                            ) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var titleTV: TextView
    private lateinit var iconIV: ImageView
    private lateinit var valueTV: TextView
    private lateinit var unitTV: TextView
    // below method is use to return the count of course list
    override fun getCount(): Int {
        return kpiList.size
    }

    //  return the item of grid view.
    override fun getItem(position: Int): Any? {
        return null
    }

    // return item id of grid view.
    override fun getItemId(position: Int): Long {
        return 0
    }

    // get individual item of grid view.
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        // check if layout inflater
        // is null, if it is null  initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        // check if convert view is null.
        // If it is initializing it.
        if (convertView == null) {
            // pass the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.gridview_item, null)
        }
        // on below line we are initializing our course image view
        // and course text view with their ids.
        titleTV = convertView!!.findViewById(R.id.idTvTitle)
        valueTV = convertView!!.findViewById(R.id.idTVValue)
        unitTV = convertView!!.findViewById(R.id.idTVUnit)
        iconIV = convertView!!.findViewById(R.id.idIVIcon)
        // setting image view.
        var assetFilename = kpiList.get(position).icon
        val myBitmap = assetFilename.assetsToBitmap(context)
        iconIV.setImageBitmap(myBitmap)
       // iconIV.setImageResource(kpiList.get(position).icon)
        // set title.
        titleTV.setText(kpiList.get(position).title)
        //set unit
        unitTV.setText(kpiList.get(position).unit)
        // set value.
        //ToDo: Round the value to be displaied
        valueTV.setText(kpiList.get(position).value.toString())
        //  return our convert view.
        return convertView
    }

}

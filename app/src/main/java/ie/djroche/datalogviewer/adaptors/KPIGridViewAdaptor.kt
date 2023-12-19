package ie.djroche.datalogviewer.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.CardKpiBinding
import ie.djroche.datalogviewer.utils.assetsToBitmap
import ie.djroche.datalogviewer.models.SiteKPIModel

interface KPIGridClickListener {
    fun onKPIGridClick(kpi: SiteKPIModel)
}

internal class KPIGridViewAdaptor(private val kpiList: List<SiteKPIModel>,
                             private val context: Context,private val listener: KPIGridClickListener
) :
    BaseAdapter() {
    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var iconIV: ImageView
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
            convertView = layoutInflater!!.inflate(R.layout.card_kpi, null)
        }

        val binding = CardKpiBinding
            .inflate(LayoutInflater.from(parent!!.context), parent, false)

        val  myKPI = kpiList.get(position)
        binding.kpi= myKPI
        binding.root.setOnClickListener { listener.onKPIGridClick(kpiList.get(position)) }
        iconIV = convertView!!.findViewById(R.id.idIVIcon)
        // setting image view.
        val assetFilename = myKPI.icon
        val myBitmap = assetFilename.assetsToBitmap(context)
        binding.idIVIcon.setImageBitmap(myBitmap)
        return binding.root
    }

}
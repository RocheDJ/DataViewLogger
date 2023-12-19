package ie.djroche.datalogviewer.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.databinding.CardSiteBinding

interface SiteClickListener {
    fun onSiteClick(site: SiteModel)
}

class SiteAdaptor constructor(private var sites: ArrayList<SiteModel>,
                              private val listener: SiteClickListener) :
    RecyclerView.Adapter<SiteAdaptor.MainHolder>() {

    //------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSiteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }
    //------------------------------------------
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val site = sites[holder.adapterPosition]
        holder.bind(site, listener)
    }
    //------------------------------------------
    override fun getItemCount(): Int = sites.size
    //------------------------------------------
    fun removeAt(position: Int) {
        sites.removeAt(position)
        notifyItemRemoved(position)
    }
    //----------------------------------------------
    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<SiteModel>) {
        // below line is to add our filtered
        // list in our course array list.
        sites = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }
    //------------------------------------------
    class MainHolder(private val binding : CardSiteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(site: SiteModel, listener: SiteClickListener) {
            binding.root.tag =site
            binding.site = site // site is linked to the card_site.xml data declaration
            binding.siteImageView.setImageResource(R.drawable.industry40)
            binding.root.setOnClickListener { listener.onSiteClick(site) }
        }

    }

    }
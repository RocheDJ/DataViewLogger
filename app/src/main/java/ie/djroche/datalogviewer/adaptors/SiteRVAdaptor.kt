package ie.djroche.datalogviewer.adaptors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.CardSiteBinding
import ie.djroche.datalogviewer.models.SiteModel

interface SiteListener {
    fun onSiteClick(site: SiteModel)
}

class SiteRVAdaptor constructor(private var sites: List<SiteModel>,
                                private val listener: SiteListener) :
    RecyclerView.Adapter<SiteRVAdaptor.MainHolder>() {

    //------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSiteBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }
    //------------------------------------------
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val placemark = sites[holder.adapterPosition]
        holder.bind(placemark, listener)
    }
    //------------------------------------------
    override fun getItemCount(): Int = sites.size
    //------------------------------------------
    class MainHolder(private val binding : CardSiteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(site: SiteModel, listener: SiteListener) {
            binding.siteDescription.text = site.description
            binding.siteImageView.setImageResource(R.drawable.industry40)
            binding.root.setOnClickListener { listener.onSiteClick(site) }
        }
    }
    }
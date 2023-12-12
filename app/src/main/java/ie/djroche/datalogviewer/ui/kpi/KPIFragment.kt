package ie.djroche.datalogviewer.ui.kpi

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.databinding.FragmentKPIBinding
import ie.djroche.datalogviewer.databinding.FragmentSiteBinding
import timber.log.Timber

class KPIFragment : Fragment() {
    private var _fragBinding: FragmentKPIBinding? = null
    private val fragBinding get() = _fragBinding!!
    companion object {
        fun newInstance() = KPIFragment()
    }

    private lateinit var viewModel: KPIViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentKPIBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        fragBinding.fabScan.setOnClickListener {
            //ToDo: add QR Scan Action
            Timber.i("QR Scan Pressed from KPI")
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(KPIViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
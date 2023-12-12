package ie.djroche.datalogviewer.ui.site

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.djroche.datalogviewer.databinding.FragmentSiteBinding
import timber.log.Timber

class SiteFragment : Fragment() {
    private var _fragBinding: FragmentSiteBinding? = null
    private val fragBinding get() = _fragBinding!!


    companion object {
        fun newInstance() = SiteFragment()
    }

    private lateinit var viewModel: SiteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentSiteBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        fragBinding.fabScan.setOnClickListener {
            //ToDo: add QR Scan Action
            Timber.i("QR Scan Pressed from site")
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SiteViewModel::class.java)
        // TODO: Use the ViewModel
    }
    /* ------------------------------------------------------------------------------------------ */

    /* -------------------------------------------------------------------------------------------- */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}
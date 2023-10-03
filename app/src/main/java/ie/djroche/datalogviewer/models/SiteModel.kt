package ie.djroche.datalogviewer.models

import java.util.Date

data class SiteModel(
            var id : Long =0,
            var description : String ="",
            var updated : Int = 0,
            var modelVersion : Int = 1,
            val data: Array<SiteData>
)

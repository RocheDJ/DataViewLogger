package ie.djroche.datalogviewer.helpers

import ie.djroche.datalogviewer.R
import ie.djroche.datalogviewer.main.MainApp
import ie.djroche.datalogviewer.models.SiteKPIModel
import ie.djroche.datalogviewer.models.SiteModel
import ie.djroche.datalogviewer.models.UserModel
import timber.log.Timber

fun loadDummyUserData(app: MainApp) {
    var myNewUser_1: UserModel = UserModel()
    var myNewUser_2: UserModel = UserModel()
    try {
        myNewUser_1.email = "homer@simpson.com"
        myNewUser_1.firstName = "homer"
        myNewUser_1.lastName = "simpson"
        myNewUser_1.password = "secret"

        app.users.create(myNewUser_1)

        myNewUser_2.email = "marge@simpson.com"
        myNewUser_2.firstName = "marge"
        myNewUser_2.lastName = "simpson"
        myNewUser_2.password = "secrettoo"

        app.users.create(myNewUser_2)
    } catch (e: Exception) {
        Timber.e("Cannot loadDummy Site Data: %s", e.toString());
    }
}

fun loadDummySiteData(app: MainApp){
        var  myNewSite : SiteModel = SiteModel(data=mutableListOf<SiteKPIModel>())
        var  myNewSite1 : SiteModel = SiteModel(data=mutableListOf<SiteKPIModel>())
        var id :String
        try {
            // create the site data for 1st site
            myNewSite.description = "Homer Site 001"
            myNewSite.data.add(SiteKPIModel(1,"Temperature", R.drawable.temp,23.4,"Deg C").copy())
            myNewSite.data.add(SiteKPIModel(1,"Temperature", R.drawable.temp,23.4,"Deg C").copy())
            myNewSite.data.add(SiteKPIModel(2,"Motor RPM", R.drawable.speedometer,800.0,"RPM").copy())
            myNewSite.data.add(SiteKPIModel(3,"Valve Status", R.drawable.valve,0.0,"OFF").copy())
            myNewSite.data.add(SiteKPIModel(4,"Flow Rate", R.drawable.flow,125.0,"L/m").copy())
            myNewSite.data.add(SiteKPIModel(5,"Tank Level", R.drawable.tank,65.0,"%").copy())
            myNewSite.qrcode = "QR-000006-000001"
            myNewSite.userid = app.users.findUserByEmail("homer@simpson.com")!!.id

            id = app.sites.create(myNewSite.copy())

            // create the site data for 2nd site
            myNewSite1.description = "Marge Site 002"
            myNewSite1.data.add(SiteKPIModel(3,"Temperature", R.drawable.temp,13.4,"Deg C").copy())
            myNewSite1.data.add(SiteKPIModel(4,"Motor RPM", R.drawable.speedometer,100.0,"RPM").copy())
            myNewSite1.qrcode = "QR-000006-000002"
            myNewSite1.userid = app.users.findUserByEmail("marge@simpson.com")!!.id
            id = app.sites.create(myNewSite1.copy())

        }catch (e: Exception) {
            Timber.e("Cannot loadDummy Site Data: %s", e.toString());
        }


}
package com.digitap.inappupdate
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play .core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

class MainActivityFlexible: AppCompatActivity () {

    private var   appUpdateManager: AppUpdateManager?=null
     private var  listener: InstallStateUpdatedListener?=null

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        setContentView (R.layout. activity_main )

        appUpdateManager = AppUpdateManagerFactory.create (this)
        listener = InstallStateUpdatedListener {
            if (it.installStatus () == InstallStatus. DOWNLOADED ) {
                showInfoUpdateSuccess ()
            }
        }
        appUpdateManager!!.registerListener (listener)
        val appUpdateInfoTask = appUpdateManager!!.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if ( it .updateAvailability () == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))
             {
                 Toast.makeText(this,"Download is  Available",Toast.LENGTH_SHORT).show()
                appUpdateManager!!.startUpdateFlowForResult (
                    it ,
                    AppUpdateType. FLEXIBLE ,
                    this,
                    999
                )



            } else {
                Toast.makeText(this,"No download Available",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult (requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == Activity. RESULT_OK ) {
            // TODO: do something in here if in-app updates success
            Toast.makeText(this,"update availble sucess",Toast.LENGTH_LONG).show()
        } else {
            // TODO: do something in here if in-app updates failure

            Toast.makeText(this,"update availble failure",Toast.LENGTH_LONG).show()
        }
    }
    override fun onResume () {
        appUpdateManager!!. appUpdateInfo
            .addOnSuccessListener {
                if ( it .installStatus () == InstallStatus.DOWNLOADED ) {
                    showInfoUpdateSuccess ()
                }
//                else if ( it .updateAvailability () == UpdateAvailability. DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS ) {
//                    appUpdateManager!!.startUpdateFlowForResult (
//                        it ,
//                        AppUpdateType. FLEXIBLE ,
//                        this,
//                        999
//                    )
//                }
            }
        super.onResume ()
    }

    override fun onDestroy () {
        super.onDestroy ()
        appUpdateManager!!.unregisterListener (listener)
    }

    private fun showInfoUpdateSuccess () {
        Snackbar.make (findViewById (android.R.id. content ), "Restart to update", Snackbar.LENGTH_INDEFINITE )
        .setAction ("Restart") {
            appUpdateManager!!.completeUpdate ()
            appUpdateManager!!.unregisterListener (listener)

        }.show ()
    }

}
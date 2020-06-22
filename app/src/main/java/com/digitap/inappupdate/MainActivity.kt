package com.digitap.inappupdate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core .appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability


class MainActivity: AppCompatActivity () {

    private lateinit var appUpdateManager: AppUpdateManager

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate (savedInstanceState)
        setContentView (R.layout. activity_main )

        appUpdateManager = AppUpdateManagerFactory.create (this)
        val appUpdateInfoTask = appUpdateManager. appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if ( it .updateAvailability () == UpdateAvailability.UPDATE_AVAILABLE
                    && it .isUpdateTypeAllowed (AppUpdateType. IMMEDIATE )
            ) {
                appUpdateManager.startUpdateFlowForResult (
                        it ,
                        AppUpdateType. FLEXIBLE ,
                        this,
                        999
                )
                Toast.makeText(this,"update availble",Toast.LENGTH_LONG).show()
            } else {
                //TODO: do something in here if an update is not available

                Toast.makeText(this,"No update availble",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume () {
        appUpdateManager. appUpdateInfo
                .addOnSuccessListener {
                    if ( it .updateAvailability () == UpdateAvailability. DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS ) {
                        appUpdateManager.startUpdateFlowForResult (
                                it ,
                                AppUpdateType. IMMEDIATE ,
                                this,
                                999
                        )
                    }
                }
        super.onResume ()
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
}
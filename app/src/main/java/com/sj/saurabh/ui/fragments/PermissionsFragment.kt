package com.sj.saurabh.ui.fragments
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.sj.saurabh.R
/**
 **@author Saurabh Kumar 27-03-2023
 */
private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

/**
 * The only purpose of this fragment is to request permissions and, once granted, display the
 * camera fragment to the user.
 */
class PermissionsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // add the storage access permission request for Android 9 and below.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val permissionList = PERMISSIONS_REQUIRED.toMutableList()
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            PERMISSIONS_REQUIRED = permissionList.toTypedArray()
        }

        if (!hasPermissions(requireContext())) {
            // Request camera-related permissions
            activityResultLauncher.launch(PERMISSIONS_REQUIRED)
        } else {

//            navigateToCamera()
            navigateToHome()
        }
    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_CameraFragment)
        }
    }

    private fun navigateToHome() {
        lifecycleScope.launchWhenStarted {
            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(R.id.navigation_HomeFragment)
        }
    }



    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->

            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in PERMISSIONS_REQUIRED && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Permission Required!")
                builder.setMessage("We need camera permission in order to perform this action.")
                builder.setPositiveButton("OK") { dialog, _ ->
                    showPermissionSetting()
                    dialog.cancel()
                }
                builder.setNegativeButton("CANCEL") { dialog, _ ->
                    requireActivity().finish()
                    dialog.cancel()
                }
                builder.show()
                Toast.makeText(context, "Camera permission is required!", Toast.LENGTH_LONG).show()
            } else {
//                navigateToCamera()
                navigateToHome()
            }
        }

    companion object {
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun showPermissionSetting() {
        requireActivity().finish()

        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        intent.addCategory(CATEGORY_DEFAULT)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        requireActivity().startActivity(intent)
    }
}
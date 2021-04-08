package com.raxdenstudios.commons.permissions

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.commons.ext.showDialog
import com.raxdenstudios.commons.permissions.model.Permission
import com.raxdenstudios.commons.util.SDK

class PermissionManager(
  private val activity: FragmentActivity
) : DefaultLifecycleObserver {

  private val registry: ActivityResultRegistry = activity.activityResultRegistry
  private lateinit var permissionResultLauncher: ActivityResultLauncher<String>

  private var onPermissionGranted: () -> Unit = {}
  private var onPermissionDenied: () -> Unit = {}

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    registerPermissionsResultContract(owner)
  }

  private fun registerPermissionsResultContract(owner: LifecycleOwner) {
    permissionResultLauncher = registry.register(
      "requestPermissions",
      owner,
      ActivityResultContracts.RequestPermission()
    ) { isGranted ->
      if (isGranted) permissionGranted() else permissionDenied()
    }
  }

  fun requestPermission(
    permission: Permission,
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
  ) {
    this.onPermissionGranted = onPermissionGranted
    this.onPermissionDenied = onPermissionDenied

    when {
      hasPermission(permission) -> permissionGranted()
      shouldShowRequestPermissionRationale(permission) -> showRequestPermissionRationale(permission)
      else -> performRequestPermission(permission)
    }
  }

  private fun permissionGranted() {
    onPermissionGranted()
  }

  private fun permissionDenied() {
    onPermissionDenied()
  }

  private fun performRequestPermission(permission: Permission) {
    permissionResultLauncher.launch(permission.name)
  }

  private fun shouldShowRequestPermissionRationale(permission: Permission) =
    if (SDK.hasMarshmallow()) activity.shouldShowRequestPermissionRationale(permission.name)
    else false

  private fun hasPermission(permission: Permission) =
    checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

  private fun checkSelfPermission(permission: Permission) =
    ContextCompat.checkSelfPermission(activity, permission.name)

  private fun showRequestPermissionRationale(permission: Permission) {
    activity.showDialog(
      permission.rationaleDialog.reason,
      permission.rationaleDialog.reasonDescription,
      positiveButton = permission.rationaleDialog.acceptLabel,
      onPositiveClickButton = { performRequestPermission(permission) },
      negativeButton = permission.rationaleDialog.deniedLabel,
      onNegativeClickButton = { permissionDenied() }
    )
  }
}


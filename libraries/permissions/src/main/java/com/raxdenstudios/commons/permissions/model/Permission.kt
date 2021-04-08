package com.raxdenstudios.commons.permissions.model

import android.Manifest

sealed class Permission(
  val name: String,
  val rationaleDialog: RationaleDialog
) {
  object Camera :
    Permission(Manifest.permission.CAMERA, RationaleDialog.Camera)

  object AccessFineLocation :
    Permission(Manifest.permission.ACCESS_FINE_LOCATION, RationaleDialog.AccessFineLocation)
}

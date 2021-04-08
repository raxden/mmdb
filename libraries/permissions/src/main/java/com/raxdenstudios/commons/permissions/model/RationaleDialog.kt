package com.raxdenstudios.commons.permissions.model

import androidx.annotation.StringRes
import com.raxdenstudios.commons.permissions.R

sealed class RationaleDialog(
  @StringRes val reason: Int,
  @StringRes val reasonDescription: Int,
  @StringRes val acceptLabel: Int,
  @StringRes val deniedLabel: Int
) {

  object Camera : RationaleDialog(
    R.string.permission_rationale_camera_title,
    R.string.permission_rationale_camera_message,
    R.string.permission_rationale_camera_positive,
    R.string.permission_rationale_camera_negative,
  )

  object AccessFineLocation : RationaleDialog(
    R.string.permission_rationale_access_fine_location_title,
    R.string.permission_rationale_access_fine_location_message,
    R.string.permission_rationale_access_fine_location_positive,
    R.string.permission_rationale_access_fine_location_negative,
  )
}

package com.raxdenstudios.commons.preferences

fun AdvancedPreferences.edit(
  commit: Boolean = false,
  action: AdvancedPreferences.Editor.() -> Unit
) {
  val editor = edit()
  action(editor)
  if (commit) {
    editor.commit()
  } else {
    editor.apply()
  }
}

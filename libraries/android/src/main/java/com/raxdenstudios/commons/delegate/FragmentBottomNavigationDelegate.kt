package com.raxdenstudios.commons.delegate

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentBottomNavigationDelegate<TFragment : Fragment>(
  activity: FragmentActivity,
  private val callback: Callback<TFragment>
) {

  interface Callback<TFragment> {
    fun onCreateBottomNavigationView(): BottomNavigationView
    fun onLoadFragmentContainer(): View
    fun onCreateFragment(itemId: Int): TFragment
    fun onFragmentLoaded(itemId: Int, fragment: TFragment)
  }

  private val fragmentManager: FragmentManager = activity.supportFragmentManager
  private var containerFragmentMap: MutableMap<Int, TFragment?> = mutableMapOf()
  private lateinit var bottomNavigationView: BottomNavigationView
  private lateinit var containerView: View

  var onAddFragmentTransaction: (
    transaction: FragmentTransaction,
    fragmentToAdd: TFragment
  ) -> Unit = { _, _ -> }

  var onShowFragmentTransaction: (
    transaction: FragmentTransaction,
    fragmentToShow: TFragment
  ) -> Unit = { _, _ -> }

  var onItemSelected: (itemId: Int) -> Unit = {}

  var itemSelected: Int
    get() = bottomNavigationView.selectedItemId
    set(value) {
      bottomNavigationView.selectedItemId = value
    }

  fun onSaveInstanceState(outState: Bundle) {
    outState.putInt("selectedItemId", bottomNavigationView.selectedItemId)
    outState.putIntArray("itemIds", containerFragmentMap.keys.toIntArray())
  }

  fun onCreate(savedInstanceState: Bundle?) {
    initFragmentTargetContainer()
    initBottomNavigationView(savedInstanceState)

    if (savedInstanceState == null) initFragments()
    else restoreFragments(savedInstanceState)
  }

  private fun initFragmentTargetContainer() {
    containerView = callback.onLoadFragmentContainer()
  }

  private fun initBottomNavigationView(savedInstanceState: Bundle?) {
    bottomNavigationView = callback.onCreateBottomNavigationView()
    bottomNavigationView.setUp(savedInstanceState)
  }

  private fun initFragments() {
    val fragmentId = itemSelected
    callback.onCreateFragment(fragmentId).also { fragment -> createFragment(fragment, fragmentId) }
  }

  private fun createFragment(fragment: TFragment, fragmentId: Int) {
    fragmentManager.commit { replace(containerView.id, fragment, "fragment_$fragmentId") }
    callback.onFragmentLoaded(fragmentId, fragment)
    saveFragmentInMap(fragmentId, fragment)
  }

  private fun restoreFragments(savedInstanceState: Bundle) {
    val fragmentIds = savedInstanceState.getIntArray("itemIds")
    fragmentIds?.forEach { fragmentId -> restoreFragment(fragmentId) }
  }

  @Suppress("UNCHECKED_CAST")
  private fun restoreFragment(fragmentId: Int) {
    val fragment = fragmentManager.findFragmentByTag("fragment_$fragmentId") as? TFragment ?: return
    callback.onFragmentLoaded(fragmentId, fragment)
    saveFragmentInMap(fragmentId, fragment)
  }

  private fun BottomNavigationView.setUp(savedInstanceState: Bundle?) {
    restoreSelectedItemIdFromSavedInstanceState(savedInstanceState)
    setOnNavigationItemSelectedListener { itemSelected -> navigationMenuItemSelected(itemSelected) }
  }

  private fun BottomNavigationView.restoreSelectedItemIdFromSavedInstanceState(savedInstanceState: Bundle?) {
    savedInstanceState?.getInt("selectedItemId")?.also { id -> selectedItemId = id }
  }

  private fun navigationMenuItemSelected(menuItem: MenuItem): Boolean {
    if (itemSelected == menuItem.itemId) return true
    val fragmentToHide = loadFragmentFromMap(itemSelected) ?: throw IllegalStateException("")
    val fragmentToShow = loadFragmentFromMap(menuItem.itemId)?.also { fragment ->
      showFragment(fragmentToHide, fragment)
    } ?: callback.onCreateFragment(menuItem.itemId).also { fragment ->
      addFragment(fragmentToHide, fragment, menuItem.itemId)
    }
    callback.onFragmentLoaded(menuItem.itemId, fragmentToShow)
    saveFragmentInMap(menuItem.itemId, fragmentToShow)
    onItemSelected(menuItem.itemId)
    return true
  }

  private fun addFragment(fragmentToHide: TFragment, fragmentToAdd: TFragment, fragmentId: Int) {
    fragmentManager.commit {
      onAddFragmentTransaction(this, fragmentToAdd)
      hide(fragmentToHide)
      add(containerView.id, fragmentToAdd, "fragment_$fragmentId")
    }
  }

  private fun showFragment(fragmentToHide: TFragment, fragmentToShow: TFragment) {
    fragmentManager.commit {
      onShowFragmentTransaction(this, fragmentToShow)
      hide(fragmentToHide)
      show(fragmentToShow)
    }
  }

  private fun loadFragmentFromMap(fragmentId: Int) = containerFragmentMap[fragmentId]

  private fun saveFragmentInMap(fragmentId: Int, fragment: TFragment) {
    containerFragmentMap[fragmentId] = fragment
  }
}

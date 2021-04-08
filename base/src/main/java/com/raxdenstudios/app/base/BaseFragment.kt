package com.raxdenstudios.app.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment(layoutId)

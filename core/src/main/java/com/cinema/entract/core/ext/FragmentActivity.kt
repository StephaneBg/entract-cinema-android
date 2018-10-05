/*
 * Copyright 2018 Stéphane Baiget
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cinema.entract.core.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.addFragment(frameId: Int, fragment: Fragment, addBackStack: Boolean = false) {
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        if (addBackStack) addToBackStack(null)
    }
}

fun FragmentActivity.addFragment(
    frameId: Int,
    fragment: Fragment,
    addBackStack: Boolean = false,
    animIn: Int,
    animOut: Int
) {
    supportFragmentManager.inTransaction {
        setCustomAnimations(animIn, animOut, animIn, animOut)
        add(frameId, fragment)
        if (addBackStack) addToBackStack(null)
    }
}

fun FragmentActivity.replaceFragment(
    frameId: Int,
    fragment: Fragment,
    sharedElement: View? = null,
    addBackStack: Boolean = false
) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        sharedElement?.let { addSharedElement(it, it.transitionName) }
        if (addBackStack) addToBackStack(null)
    }
}

fun FragmentActivity.replaceFragment(
    frameId: Int,
    fragment: Fragment,
    addBackStack: Boolean = false,
    animIn: Int,
    animOut: Int
) {
    supportFragmentManager.inTransaction {
        setCustomAnimations(animIn, animOut, animIn, animOut)
        replace(frameId, fragment)
        if (addBackStack) addToBackStack(null)
    }
}

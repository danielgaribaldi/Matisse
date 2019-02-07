/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhihu.matisse.internal.entity

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.zhihu.matisse.R
import com.zhihu.matisse.internal.ui.widget.IncapableDialog

fun Context.getColorFromAttr(@AttrRes attrColor: Int, typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true): Int {

    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

class IncapableCause(private val type: DialogType = DialogType.TOAST,
                     private val title: String = "",
                     private val message: String = "") {


    enum class DialogType { TOAST, DIALOG, NONE }

    companion object {

        fun handleCause(context: Context, cause: IncapableCause?) {

            cause?.let {

                when (it.type) {

                    DialogType.TOAST -> Snackbar.make((context as FragmentActivity).window.decorView.findViewById<View>(android.R.id.content),
                                                      cause.message, Snackbar.LENGTH_SHORT).apply {

                        view.setBackgroundColor(context.getColorFromAttr(R.attr.snackbar_errorBgColor))

                        (view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView).apply {

                            setTextColor(context.getColorFromAttr(R.attr.snackbar_errorTextColor))
                        }
                    }.show()


                    DialogType.DIALOG -> IncapableDialog.newInstance(cause.title, cause.message)
                            .show((context as FragmentActivity).supportFragmentManager, IncapableDialog::class.java.name)

                    else -> Unit
                }
            }
        }
    }
}

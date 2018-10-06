package com.cinema.entract.app.ui.event

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.load
import com.cinema.entract.core.ext.find
import com.google.android.material.button.MaterialButton

class EventDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        find<ImageView>(R.id.cover).load(
            arguments?.getString(COVER_URL) ?: error("Use newInstance !")
        )

        find<MaterialButton>(R.id.close).setOnClickListener {
            dialog.dismiss()
        }
    }

    companion object {
        private const val COVER_URL = "COVER_URL"

        fun show(fragmentManager: FragmentManager, url: String) = EventDialogFragment().apply {
            arguments = bundleOf(COVER_URL to url)
        }.show(fragmentManager, url)
    }
}
package com.sifac_qr_manager.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.google.android.material.internal.ContextUtils.getActivity
import com.sifac_qr_manager.R


class AgreementDialog(private val activity: MainActivity) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)
        dialog.setTitle("注意事項")

        val window = dialog.window ?: return super.onCreateDialog(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )

        dialog.setContentView(R.layout.agreement_dialog)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val button = dialog.findViewById<Button>(R.id.agree_button)
        dialog.findViewById<CheckBox>(R.id.agree_check).setOnCheckedChangeListener { buttonView, isChecked ->
            button.isEnabled = isChecked
        }

        button.setOnClickListener {
            setAgreedState()
            dismiss()
        }

        button.isEnabled = false

        return dialog
    }

    companion object {
        const val PREFERENCE_INIT = 0
        const val PREFERENCE_AGREED = 1
        const val AGREED_FLAG_NAME = "attention agreed"
    }
    private fun setAgreedState() {
        val editer = PreferenceManager.getDefaultSharedPreferences(this.activity).edit()
        editer.putInt(AGREED_FLAG_NAME, PREFERENCE_AGREED)
        editer.commit()
    }
    private fun getAgreedState(): Int {
        val sp = PreferenceManager.getDefaultSharedPreferences(this.activity)
        val state = sp.getInt(AGREED_FLAG_NAME, PREFERENCE_INIT)
        return state
    }
    val isAgreed = (getAgreedState() == PREFERENCE_AGREED)
}
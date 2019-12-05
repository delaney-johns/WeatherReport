package com.ait.weatherreport

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ait.weatherreport.data.City
import kotlinx.android.synthetic.main.city_dialog.view.*

class CityDialog : DialogFragment() {
    interface CityHandler {
        fun cityCreated(item: City)
    }

    private lateinit var cityHandler: CityHandler

    override fun onAttach(context: Context) {
        super.onAttach(context)


        if (context is CityHandler) {
            cityHandler = context
        } else {
            throw RuntimeException(
                context.getString(R.string.city_handler_msg)
            )
        }
    }

    private lateinit var etCityName: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.dialog_title))

        val rootView = requireActivity().layoutInflater.inflate(
            R.layout.city_dialog, null
        )

        etCityName = rootView.etCity
        builder.setView(rootView)


        builder.setPositiveButton(getString(R.string.positive_btn_text)) { dialog, witch ->

        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etCityName.text.isNotEmpty()) {

                handleCityCreate()


                dialog!!.dismiss()
            } else {
                etCityName.error = getString(R.string.empty_city_error_msg)
            }
        }
    }

    private fun handleCityCreate() {
        cityHandler.cityCreated(
            City(
                null,
                etCityName.text.toString()
            )
        )
    }
}
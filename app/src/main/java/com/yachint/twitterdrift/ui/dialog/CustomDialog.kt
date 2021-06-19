package com.yachint.twitterdrift.ui.dialog

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.yachint.twitterdrift.R

class CustomDialog(
    private val activity: Activity
) {
    private lateinit var dialog: AlertDialog

    companion object {
        var isShowing = false
    }

    fun showLoadingDialog(){
        if(!isShowing){
            val builder = AlertDialog.Builder(activity)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.custom_dialog_loading, null)
            builder.setView(view)

            builder.setCancelable(false)
            dialog = builder.create()
            dialog.show()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isShowing = true
        }
    }

    fun showDialogDouble(
        title: String,
        body: String,
        successText: String,
        rejectText: String,
        handleSuccess: () -> Unit,
        handleReject: () -> Unit,
        isNullable: Boolean,
        type: String
    ){
        if(!isShowing){
            val builder = AlertDialog.Builder(activity)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.material_you_dialog_double, null)
            builder.setView(view)

            init(view, title, body, successText, rejectText, handleSuccess, handleReject, type, 2)
            builder.setCancelable(isNullable)
            dialog = builder.create()
            dialog.show()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isShowing = true
        }
    }

    fun showDialogSingle(
        title: String,
        body: String,
        successText: String,
        handleSuccess: (() -> Unit)? = null,
        isNullable: Boolean,
        type: String
    ){
        if(!isShowing){
            val builder = AlertDialog.Builder(activity)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.material_you_dialog_single, null)
            builder.setView(view)

            init(view, title, body, successText, "", handleSuccess, null, type, 1)
            builder.setCancelable(isNullable)
            dialog = builder.create()
            dialog.show()

            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isShowing = true
        }
    }

    private fun init(
        view: View,
        title: String,
        body: String,
        successText: String,
        rejectText: String,
        handleSuccess: (() -> Unit)? = null,
        handleReject: (() -> Unit)? = null,
        type: String = "null",
        mode: Int
    ){
        when(mode){
            2 -> {
                val successTextHolder = view.findViewById<TextView>(R.id.acceptText)
                val rejectTextHolder = view.findViewById<TextView>(R.id.rejectText)
                val successBtn = view.findViewById<CardView>(R.id.successBtn)
                val rejectBtn = view.findViewById<CardView>(R.id.rejectBtn)

                successTextHolder.text = successText
                rejectTextHolder.text = rejectText

                successBtn.setOnClickListener {
                    dismiss()
                    handleSuccess?.invoke()
                }

                rejectBtn.setOnClickListener {
                    dismiss()
                    handleReject?.invoke()
                }
            }

            1 -> {
                val singleTextHolder = view.findViewById<TextView>(R.id.singleBtnText)
                val singleBtn = view.findViewById<CardView>(R.id.singleBtn)

                singleTextHolder.text = successText
                singleBtn.setOnClickListener {
                    dismiss()
                    handleSuccess?.invoke()
                }
            }
        }

        val titleTextHolder = view.findViewById<TextView>(R.id.textSingle)
        val bodyTextHolder = view.findViewById<TextView>(R.id.textBody)
        val helperImage = view.findViewById<ImageView>(R.id.helperImage)

        titleTextHolder.text = title
        bodyTextHolder.text = body

        when(type){
            "info" -> {
                helperImage.setImageResource(R.drawable.ic_outline_info)
            }

            "error" -> {
                helperImage.setImageResource(R.drawable.ic_error)
                helperImage.imageTintList = ColorStateList.valueOf(activity.getColor(R.color.colorRed))
            }

            "success" -> {
                helperImage.setImageResource(R.drawable.ic_outline_check)
                helperImage.imageTintList = ColorStateList.valueOf(activity.getColor(R.color.green_color_picker))
            }
        }
    }

    fun dismiss(){
        try {
            if(isShowing){
                dialog.dismiss()
                isShowing = false
            }
        } catch (e: Exception){
            Log.i("DIALOG ERROR", "Progress Dialog: ${e.localizedMessage}")
        }
    }
}
package uz.kmax.a15puzzle.dialog

import android.R
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import uz.kmax.a15puzzle.databinding.LayoutWinDialogBinding

class WinDialog {
    private var nextClickListener: (() -> Unit)? = null
    fun setOnClickListener(f: () -> Unit) {
        nextClickListener = f
    }

    fun show(context: Context,winScore:Int,winTime:Int){
        val dialog = Dialog(context, R.style.Theme_Black_NoTitleBar_Fullscreen)
        val binding = LayoutWinDialogBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        binding.winTime.text = timeFormat(winTime)
        binding.winScore.text = winScore.toString()
        binding.dialogWinClose.setOnClickListener {
            nextClickListener?.invoke()
            dialog.dismiss()
        }
        binding.dialogWinNewGame.setOnClickListener {
            nextClickListener?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun timeFormat(time: Int): String {
        val minute = time / 60
        val second = time % 60
        val hour = time / 3600
        val secondFormat = if (second < 10) "0${second}" else "$second"
        val minuteFormat = if (minute < 10) "0${minute}" else "$minute"
        val hourFormat = if (hour < 10) "0${hour}" else "$hour"
        return "${hourFormat}:${minuteFormat}:${secondFormat}"
    }
}
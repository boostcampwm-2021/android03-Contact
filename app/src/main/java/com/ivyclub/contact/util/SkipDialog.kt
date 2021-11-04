package com.ivyclub.contact.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface

class SkipDialog(
    private val ok: DialogInterface.OnClickListener,
    private val context: Context?
) {
    fun showDialog() {
        AlertDialog.Builder(context)
            .setTitle("건너뛰기")
            .setMessage("정말 초기 설정을 종료하시겠습니까?")
            .setPositiveButton("네", ok)
            .setNegativeButton("아니오", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {

                }
            })
            .create()
            .show()
    }
}
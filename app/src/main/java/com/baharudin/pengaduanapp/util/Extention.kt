package com.baharudin.pengaduanapp.util

import android.content.Context
import android.os.Looper
import android.widget.Toast


fun withDelay(delay: Long = 100, block: () -> Unit) {
    android.os.Handler(Looper.getMainLooper()).postDelayed(Runnable(block), delay)
}
fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
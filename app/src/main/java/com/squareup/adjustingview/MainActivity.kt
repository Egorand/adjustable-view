package com.squareup.adjustingview

import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  private lateinit var editText: EditText

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    editText = findViewById(R.id.edit_text)
  }

  override fun onResume() {
    super.onResume()
    editText.post {
      AdjustableView.attach(editText)
    }
  }

  private class AdjustableView(context: Context) : View(context) {
    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
      Log.d(TAG, "In onApplyWindowInsets()")
      Log.d(TAG, "systemWindowInsetBottom: " + insets.systemWindowInsetBottom)
      return super.onApplyWindowInsets(insets)
    }

    companion object {
      private const val TAG = "AdjustableView"

      fun attach(view: View) {
        val adjustableView = AdjustableView(view.context)
        val params = WindowManager.LayoutParams()
        params.gravity = Gravity.START or Gravity.TOP
        params.x = 0
        params.y = 0
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        params.alpha = 0f
        params.format = PixelFormat.TRANSPARENT
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA
        params.flags = params.flags or (WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
            or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR)
        params.token = view.windowToken
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        (view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .addView(adjustableView, params)
      }
    }
  }
}

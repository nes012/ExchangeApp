package nesty.anzhy.exchangeapp.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import nesty.anzhy.exchangeapp.R

class JarvisLoader(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_jarvis_loader)

        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window?.setBackgroundDrawableResource(R.color.black)
    }
}
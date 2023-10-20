package com.coding.receivetextandimage

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ViewFlipper

class MainActivity : AppCompatActivity() {


    private lateinit var singleImageView : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val receivingTxt = findViewById<TextView>(R.id.receivingTxt)
        singleImageView = findViewById(R.id.singleImageView)
        when(intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                        receivingTxt.text = it
                    }
                }else if (intent.type?.startsWith("image/") == true) {
                    handleSendImage(intent) // Handle single image being sent
                }
            }
            Intent.ACTION_SEND_MULTIPLE ->{
                if(intent.type?.startsWith("image/") == true ){
                    handleSendMultipleImages(intent) // Handle multiple images being sent
                }
            }
        }
    }
    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
            singleImageView.setImageURI(it)
        }
    }

    private fun handleSendMultipleImages(intent: Intent) {
        intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
            // Update UI to reflect multiple images being shared

            val viewFlipper = findViewById<ViewFlipper>(R.id.viewFlipper)
            for (image in it){
                val imageView = ImageView(this)
                imageView.setImageURI(image as Uri?)
                imageView.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                )
                viewFlipper.addView(imageView)
            }
            val previousBtn = findViewById<Button>(R.id.previousBtn)
            previousBtn.setOnClickListener {
                viewFlipper.showPrevious()
            }
            val nextBtn = findViewById<Button>(R.id.nextBtn)
            nextBtn.setOnClickListener {
                viewFlipper.showNext()
            }
        }
    }
}
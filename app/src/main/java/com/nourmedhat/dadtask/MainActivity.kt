package com.nourmedhat.dadtask

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.nourmedhat.dadtask.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding!!.btnCamera.setOnClickListener {

            ImagePicker
                .with(this)
                .cameraOnly()
                .cropSquare()
                .createIntent {
                    startImageResult.launch(it)
                }
        }
        binding!!.btnGallery.setOnClickListener {

            ImagePicker
                .with(this)
                .galleryOnly()
                .cropSquare()
                .createIntent {
                    startImageResult.launch(it)
                }
        }


    }


    private val startImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    binding!!.imageView.setImageURI(fileUri)
                    imageToByteArray(binding!!.imageView)

                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        ImagePicker.getError(data),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


    fun imageToByteArray(imageView: ImageView): ByteArray? {
        return try {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytesData = stream.toByteArray()
            stream.close()
            bytesData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return null
    }
}
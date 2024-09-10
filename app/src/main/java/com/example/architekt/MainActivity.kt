package com.example.architekt

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity

import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.imgcodecs.Imgcodecs

class MainActivity : ComponentActivity() {

    private lateinit var imageView: ImageView
    private val SELECT_IMAGE = 100

    companion object {
        init {
            if (!OpenCVLoader.initDebug()) {
                Log.e("OpenCV", "Unable to load OpenCV")
            } else {
                Log.d("OpenCV", "OpenCV loaded successfully")
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        // Launch gallery to select an image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                // Convert image to Bitmap
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)

                // Preprocess the image
                val processedBitmap = preprocessImage(bitmap)

                // Display the processed image
                imageView.setImageBitmap(processedBitmap)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Preprocessing function
    private fun preprocessImage(bitmap: Bitmap): Bitmap {
        // Convert bitmap to OpenCV Mat
        val img = Mat()
        Utils.bitmapToMat(bitmap, img)

        // Noise Removal (Gaussian Blur)
        Imgproc.GaussianBlur(img, img, Size(3.0, 3.0), 0.0)

        // Edge Detection (Canny)
        val edges = Mat()
        Imgproc.Canny(img, edges, 100.0, 200.0)

        // Thresholding (Binary Image)
        val binary = Mat()
        Imgproc.threshold(edges, binary, 128.0, 255.0, Imgproc.THRESH_BINARY)

        // Convert Mat back to Bitmap
        val processedBitmap =
            Bitmap.createBitmap(binary.cols(), binary.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(binary, processedBitmap)

        return processedBitmap  // Return the processed image
    }
}

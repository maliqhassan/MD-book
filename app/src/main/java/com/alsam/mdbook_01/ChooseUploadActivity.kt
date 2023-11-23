package com.alsam.mdbook_01

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChooseUploadActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 101
    private val REQUEST_PICK_IMAGE = 102

    var back =""
    var front =""
    var  id ="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_upload)


        val intent = intent

        // Retrieve data from the intent
        id = intent.getStringExtra("id").toString()

        back =  intent.getStringExtra( "back").toString();
        front =  intent.getStringExtra( "front").toString();

        val openGalleryButton: Button = findViewById(R.id.openGallery)
        val openCameraButton: Button = findViewById(R.id.openCamera)

        openGalleryButton.setOnClickListener {
            openGallery()
        }

        openCameraButton.setOnClickListener {
            openCamera()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_PICK_IMAGE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    // Capture image from camera
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    if (imageBitmap != null) {
                        val photoFile: File? = try {
                            createImageFile()
                        } catch (ex: IOException) {
                            // Handle error
                            null
                        }

                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "com.alsam.mdbook_01.fileprovider",
                                it
                            )
                            // Save the photo URI or use it as needed
                            uploadUriToFirestoreStorage( photoURI)
                            finish()
                        }
                    }
                }
                REQUEST_PICK_IMAGE -> {
                    // Pick image from gallery
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        // Use the selectedImageUri as needed
                        uploadUriToFirestoreStorage(selectedImageUri)
                        finish()
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == RESULT_OK) {
//            when (requestCode) {
//                REQUEST_IMAGE_CAPTURE -> {
//                    val imageBitmap = data?.extras?.get("data") as Bitmap?
//                 //   Toast.makeText(this, "Photo OK", Toast.LENGTH_SHORT).show()
//                    // Finish the activity after displaying the toast
//                    progressDialog = ProgressDialog(this)
//                    progressDialog.setMessage("Loading...")
//                    progressDialog.setCancelable(false)
//                    progressDialog.show()
//                    if (imageBitmap != null) {
//                        uploadBitmapToFirestoreStorage(imageBitmap)
//                    };
//                    finish()
//                }
//                REQUEST_PICK_IMAGE -> {
//                    val selectedImage = data?.data
//                    // Display a toast message when an image is selected from the gallery
//                  //  Toast.makeText(this, "Photo OK", Toast.LENGTH_SHORT).show()
//                    progressDialog = ProgressDialog(this)
//                    progressDialog.setMessage("Loading...")
//                    progressDialog.setCancelable(false)
//                    progressDialog.show()
//                    // Finish the activity after displaying the toast
//                    uploadBitmapToFirestoreStorage(imageBitmap);
//                    //finish()
//                }
//            }
//        }
//    }
fun uploadUriToFirestoreStorage(imageUri: Uri) {
    progressDialog = ProgressDialog(this)
    progressDialog.setMessage("Loading...")
    progressDialog.setCancelable(false)
    progressDialog.show()

    val storage = FirebaseStorage.getInstance()
    val storageRef: StorageReference = storage.reference

    // Generate a unique name for the image
    val imageName = UUID.randomUUID().toString()

    // Create a reference to 'images/imageName.jpg'
    val imageRef: StorageReference = storageRef.child("images/$imageName.jpg")

    // Upload the image file from the URI
    imageRef.putFile(imageUri)
        .addOnSuccessListener { taskSnapshot ->
            // Image upload success
            // You can get the download URL of the uploaded image if needed
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()

                saveUrl(downloadUrl)
                // Now you can use the downloadUrl as needed (e.g., save it to Firestore database)
            }
        }
        .addOnFailureListener { e ->
            // Handle unsuccessful uploads
            Log.e("FirestoreStorage", "Error uploading image", e)
        }
}

    private lateinit var progressDialog: ProgressDialog
    private val db = FirebaseFirestore.getInstance()
    private fun saveUrl(downloadUrl: String) {


        val collectionRef = db.collection("problems")

        // Reference to the specific document
        val documentRef = collectionRef.document(id)
        if(front.equals("1"))
        {
            val updates = hashMapOf<String, Any>(
                "front_url" to downloadUrl
                // Add any other fields you want to update here
            )

            // Update the document
            documentRef.update(updates)
                .addOnSuccessListener {
                    progressDialog.cancel()
                    finish();
                    // Document updated successfully
                    // You can perform additional actions here if needed
                }
                .addOnFailureListener { e ->
                    progressDialog.cancel()
                    // Handle failures
                    // Log the error or perform any other actions
                }




        }
        else{


            val updates = hashMapOf<String, Any>(
                "back_url" to downloadUrl
                // Add any other fields you want to update here
            )

            // Update the document


            documentRef.update(updates)
                .addOnSuccessListener {
                    progressDialog.cancel()
                    finish();
                    // Document updated successfully
                    // You can perform additional actions here if needed
                }
                .addOnFailureListener { e ->
                    progressDialog.cancel()
                    // Handle failures
                    // Log the error or perform any other actions
                }
        }





            // Reference to the collection


            // Create a map to update the document




    }

}

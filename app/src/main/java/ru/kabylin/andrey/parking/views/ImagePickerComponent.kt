package ru.kabylin.andrey.parking.views

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import ru.kabylin.andrey.parking.R
import ru.kabylin.andrey.parking.ext.createTempImageFile
import ru.kabylin.andrey.parking.ext.permissions
import java.io.File
import java.io.IOException

class ImagePickerComponent(val activity: Activity) {
    companion object {
        const val REQUEST_FOR_RESULT_PICK_IMAGE = 1001
        const val REQUEST_FOR_RESULT_CAPTURE_IMAGE = 1002
    }

    var capturedPhotoPath: String? = null
        private set

    var photoUri: Uri? = null
        private set

    fun openCamera() {
        activity.permissions.request(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        ).subscribe { granted ->
            if (granted) {
                openCameraDialog()
            } else {
                activity.longToast(R.string.permission_not_granted).show()
            }
        }
    }

    fun openGallery() {
        activity.permissions.request(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe { granted ->
                if (granted) {
                    openGalleryDialog()
                } else {
                    activity.longToast(R.string.permission_not_granted).show()
                }
            }
    }

    private fun openGalleryDialog() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        activity.startActivityForResult(chooserIntent, REQUEST_FOR_RESULT_PICK_IMAGE)
    }

    private fun openCameraDialog() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity.packageManager) == null) {
            activity.toast(R.string.canceled).show()
            return
        }

        val photoFile: File

        try {
            photoFile = createTempImageFile(activity)
            capturedPhotoPath = photoFile.absolutePath
        } catch (ex: IOException) {
            activity.toast(R.string.canceled).show()
            return
        }

        val authority = activity.getString(R.string.file_provider)
        val photoURI = FileProvider.getUriForFile(activity, authority, photoFile)

        // Open dialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            val resInfoList = activity.packageManager
                .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                activity.grantUriPermission(
                    packageName,
                    photoURI,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        activity.startActivityForResult(takePictureIntent, REQUEST_FOR_RESULT_CAPTURE_IMAGE)
    }

    fun isComponentResult(requestCode: Int): Boolean =
        requestCode == ImagePickerComponent.REQUEST_FOR_RESULT_PICK_IMAGE ||
            requestCode == ImagePickerComponent.REQUEST_FOR_RESULT_CAPTURE_IMAGE

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        when (requestCode) {
            ImagePickerComponent.REQUEST_FOR_RESULT_PICK_IMAGE -> {
                if (resultCode != AppCompatActivity.RESULT_OK)
                    return false

                val imageUri = data?.data ?: return false

                photoUri = imageUri
                return true
            }

            ImagePickerComponent.REQUEST_FOR_RESULT_CAPTURE_IMAGE -> {
                if (resultCode != AppCompatActivity.RESULT_OK)
                    return false

                if (capturedPhotoPath == null)
                    return false

                photoUri = Uri.parse(capturedPhotoPath)
                return true
            }
        }

        return false
    }
}

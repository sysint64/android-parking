package ru.kabylin.andrey.parking

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_form_add_parking.*
import org.jetbrains.anko.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import ru.kabylin.andrey.parking.client.Client
import ru.kabylin.andrey.parking.client.LogicError
import ru.kabylin.andrey.parking.forms.fields.editText
import ru.kabylin.andrey.parking.forms.fields.location
import ru.kabylin.andrey.parking.forms.validators.PhoneValidator
import ru.kabylin.andrey.parking.forms.validators.RequiredValidator
import ru.kabylin.andrey.parking.models.Location
import ru.kabylin.andrey.parking.models.Photo
import ru.kabylin.andrey.parking.router.Router
import ru.kabylin.andrey.parking.services.ParkingService
import ru.kabylin.andrey.parking.views.ClientViewState
import ru.kabylin.andrey.parking.views.FormAppCompatActivity
import ru.kabylin.andrey.parking.views.ImagePickerComponent
import ru.kabylin.andrey.parking.views.attachToActivity

class MainActivity : FormAppCompatActivity<ClientViewState>(), KodeinAware {
    override val kodeinContext = kcontext(this)
    override val kodein by closestKodein()

    override val router = Router(this)
    override val viewState by lazy { ClientViewState(client, this) }
    override val client: Client by instance()

    override val progressBarView: View by lazy { progressBar }

    private val parkingService: ParkingService by instance()
    private val imagePickerComponent = ImagePickerComponent(this)

    companion object {
        const val PHOTO_MAX_SIZE = 1024
    }

    private val photoOptions = RequestOptions()
        .override(PHOTO_MAX_SIZE, PHOTO_MAX_SIZE)
        .centerCrop()
        .placeholder(R.color.colorAccent)
        .error(R.color.colorAccent)

    private var photo: Photo? = null
    private var photoUri: Uri? = null

    private lateinit var selectedLocation: () -> Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_add_parking)

        toolbar.attachToActivity(this, displayHomeButton = true)
        errorsView.attach(container)

        cameraButton.setOnClickListener {
            imagePickerComponent.openCamera()
        }

        initForm()
    }

    override fun initForm() {
        super.initForm()

        form {
            location("location", listOf(RequiredValidator())) {
                field.label = "Место парковки"
                selectedLocation = { value as Location }
            }

            editText("phone", listOf(RequiredValidator(), PhoneValidator())) {
                field.label = "Телефон"
                editTextAttrs {
                    inputType = InputType.TYPE_CLASS_PHONE
                }
            }

            editText("comment", listOf(RequiredValidator())) {
                field.label = "Комментари"
                editTextAttrs {
                    maxLines = 10
                    inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    setSingleLine(false)
                }
            }

            attachSubmitButton(submitButton, ::onSubmit)
        }

        inflateDefaultForm()
    }

    private fun onSubmit() {
        if (photo == null) {
            client.onError(LogicError(R.string.photo_required_error))
            return
        }

        val query = parkingService.addParkingPlace(
            ParkingService.ParkingInfo(
                location = form.getValue("location") as Location,
                phone = form.getValue("phone") as String,
                comment = form.getValue("comment") as String,
                photo = photo!!
            )
        )

        client.execute(query) {
            form.clear()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable("avatar", photoUri)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        photoUri = savedInstanceState.getParcelable("avatar")
        photoUri?.let { updatePhotoViewFromUri(it) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (imagePickerComponent.onActivityResult(requestCode, resultCode, data)) {
            updatePhotoViewFromUri(imagePickerComponent.photoUri!!)
        }
        else if (imagePickerComponent.isComponentResult(requestCode)) {
            toast(R.string.canceled).show()
        }
    }

    private fun updatePhotoViewFromUri(uri: Uri) {
        photo = null

        val query = Single.fromCallable {
            val future = Glide
                .with(this)
                .asBitmap()
                .load(uri.toString())
                .apply(photoOptions)
                .submit()

            Photo.bitmap(future.get())
        }

        client.execute(query) {
            photo = it.payload
            photoUri = uri

            Glide
                .with(this)
                .load(uri.toString())
                .apply(photoOptions.transform(CircleCrop()))
                .into(photoImageView)
        }
    }
}

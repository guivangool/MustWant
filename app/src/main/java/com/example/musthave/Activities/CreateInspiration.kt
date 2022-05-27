package com.example.musthave.Activities

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.musthave.DataEntities.InspirationEntity
import com.example.musthave.MustWantApp
import com.example.musthave.R
import com.example.musthave.databinding.ActivityCreateInspirationBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class CreateInspiration : AppCompatActivity() {
    private var binding: ActivityCreateInspirationBinding? = null
    private var goalList = ArrayList<Int>()
    private var isNew = true
    private var currentInspiration: InspirationEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityCreateInspirationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Action Bar
        setSupportActionBar(binding?.tbCreateInspiration)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding?.tbCreateInspiration?.setNavigationOnClickListener {
            onBackPressed()
        }

        createRadioButtons()

        loadInspiration(goalList[0])

        binding?.fabLoadPicture?.setOnClickListener {
            requestStoragePermission()
        }

        binding?.fabRemovePicture?.setOnClickListener {
            binding?.ivGoalImage?.setImageDrawable(null)
        }

        binding?.rgSelectedGoals?.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                binding?.etSetPhrase?.text?.clear()
                binding?.ivGoalImage?.setImageDrawable(null)
                val radio: RadioButton = findViewById(checkedId)
                loadInspiration(
                    when (radio.text) {
                        "Yo" -> 1
                        "Hogar" -> 2
                        "Trabajo" -> 3
                        "Relaciones" -> 4
                        else -> 0
                    }
                )
            })

        binding?.btnCancel?.setOnClickListener {
            finish()
        }
        binding?.btnAccept?.setOnClickListener {
            var message = binding?.etSetPhrase?.text?.toString()
            if (binding?.etSetPhrase?.text?.toString() != "" &&
                binding?.ivGoalImage?.drawable != null
            ) {
                val filePath = saveImageToInternalStorage(binding?.ivGoalImage?.drawable)
                lifecycleScope.launch {
                    val inspirationDao = (application as MustWantApp).db.inspitationDao()

                    if (isNew) {
                        val radioButtonSelected =
                            findViewById(binding?.rgSelectedGoals?.checkedRadioButtonId as Int) as RadioButton
                        val goalID: Int = when (radioButtonSelected.text) {
                            "Yo" -> 1
                            "Hogar" -> 2
                            "Trabajo" -> 3
                            "Relaciones" -> 4
                            else -> 0
                        }
                        inspirationDao.insert(
                            InspirationEntity(
                                null,
                                goalID,
                                binding?.etSetPhrase?.text.toString(),
                                filePath.toString()
                            )
                        )
                    } else {
                        inspirationDao.update(
                            InspirationEntity(
                                currentInspiration!!.id,
                                currentInspiration!!.goalId,
                                binding?.etSetPhrase?.text.toString(),
                                filePath.toString()
                            )
                        )
                    }
                    finish()
                }

            } else {
                Toast.makeText(
                    this,
                    "Debe ingresar una frase y una imagen para crear la inspiración",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun loadInspiration(goalId: Int) {
        val inspirationDao = (application as MustWantApp).db.inspitationDao()
        //Load image from first goal
        lifecycleScope.launch {
            inspirationDao.getGoalInspiration(goalId).collect {
                if (it != null) {
                    var bitmap: Bitmap = BitmapFactory.decodeFile(it.image)
                    binding?.ivGoalImage?.setImageBitmap(bitmap)
                    binding?.etSetPhrase?.setText(it.phrase.toString())
                    currentInspiration = it
                    isNew = false
                } else
                    isNew = true
            }
        }
    }

    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                //val imageBachground: ImageView = findViewById(R.id.iv_GoalImage)
                val imageBachground: ImageView? = binding?.ivGoalImage
                imageBachground?.setImageURI(result.data?.data)
            }
        }

    private val requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    Toast.makeText(
                        this,
                        "Permission granted for write external storage.",
                        Toast.LENGTH_LONG
                    ).show()
                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)

                } else {
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        Toast.makeText(
                            this,
                            "Permission denies for read external storage.",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
        }

    fun showRationaleDialog(
        title: String,
        message: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun requestStoragePermission() {
        // Check if the permission was denied and show rationale
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //call the rationale dialog to tell the user why they need to allow permission request
            showRationaleDialog(
                "Must|Want App", "Must|Want App " +
                        "needs to Access Your External Storage"
            )
        } else {
            // You can directly ask for the permission.
            //if it has not been denied then request for permission
            //  The registered ActivityResultCallback gets the result of this request.
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }

    }

    private fun createRadioButtons() {

        goalList = getIntent().extras?.get("goalList") as ArrayList<Int>
        var firstGoal = true

        for (goal in goalList) {
            val radioButton = RadioButton(this)
            radioButton.text = when (goal) {
                1 -> "Yo"
                2 -> "Hogar"
                3 -> "Trabajo"
                4 -> "Relaciones"
                else -> ""
            }

            //Set properties
            radioButton.setBackgroundResource(R.drawable.radio_button_background)
            radioButton.setTextColor(R.drawable.radio_button_background_selector)
            radioButton.buttonDrawable = null
            radioButton.gravity = Gravity.CENTER
            radioButton.layoutParams = ViewGroup.LayoutParams(250, 60)
            radioButton.setTextColor(getResources().getColor(R.color.black))
            radioButton.setTypeface(null, Typeface.BOLD)
            radioButton.isChecked = firstGoal
            firstGoal = false
            
            //Add to the radio group
            binding?.rgSelectedGoals?.addView(radioButton)
            binding?.rgSelectedGoals?.check((binding?.rgSelectedGoals?.getChildAt(0) as RadioButton).id)

        }
    }

    // Method to save an image to internal storage
    private fun saveImageToInternalStorage(drawable: Drawable?): Uri {
        // Get the bitmap from drawable object
        val bitmap = (drawable as BitmapDrawable).bitmap

        // Get the context wrapper instance
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)


        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }

        // Return the saved image uri
        return Uri.parse(file.absolutePath)
    }
}






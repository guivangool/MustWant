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
import android.util.Log
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
import androidx.lifecycle.ViewModelProvider
import com.example.musthave.DataEntities.GoalEntity
import com.example.musthave.DataEntities.InspirationEntity
import com.example.musthave.Enums.GoalTypeEnum
import com.example.musthave.Factories.InspirationViewModelFactory
import com.example.musthave.Fragments.AcceptCancel
import com.example.musthave.Interfaces.OnAcceptCancelButtonClickListener
import com.example.musthave.MustWantApp
import com.example.musthave.R
import com.example.musthave.Repositories.InspirationRepository
import com.example.musthave.databinding.ActivityCreateInspirationBinding
import com.example.musthave.viewModels.InspirationViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class CreateInspiration : AppCompatActivity(),OnAcceptCancelButtonClickListener {

    private var binding: ActivityCreateInspirationBinding? = null
    private var goalList = ArrayList<Int>()
    private lateinit var inspirationList : List<InspirationEntity>
    private var isNew = true
    private var currentInspiration: InspirationEntity? = null
    private lateinit var inspirationViewModel: InspirationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Binding
        binding = ActivityCreateInspirationBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        //Action Bar
        try {
            setSupportActionBar(binding?.tbCreateInspiration)
        }
        catch (ex:Exception)
        {
            Log.d("ERROR",ex.message.toString())
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        createRadioButtons()

        //Add the fragment AcceptCancel programmatically
        if (savedInstanceState == null) {
            val fragment = AcceptCancel()
            val fragmentTransaction  = supportFragmentManager.beginTransaction()
            fragment.setOnAcceptCancelButtonClickListener(this)
            fragmentTransaction.add(R.id.fragment_accept_cancel,fragment)
            fragmentTransaction.commit()
        }

        //Create ViewModel
        //Dao
        val inspirationDao = (application as MustWantApp).db.inspitationDao()
        //Repository
        val repository = InspirationRepository(inspirationDao)
        //Factory
        val factory = InspirationViewModelFactory(repository)
        //ViewModel
        inspirationViewModel =
            ViewModelProvider(this, factory).get(InspirationViewModel::class.java)
        //binding?.obstacleViewModel = obstacleViewModel
        //binding?.lifecycleOwner = this

        //Observe ViewModel
        inspirationViewModel.inspirations.observe(
            this,
            androidx.lifecycle.Observer { inspirations ->
                this.inspirationList = inspirations

                loadInspiration(goalList[0])

            })

        binding?.tbCreateInspiration?.setNavigationOnClickListener {
            onBackPressed()
        }

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
                    GoalTypeEnum.values().find { it.label == radio.text }?.number
                )
            })
    }

    private fun loadInspiration(goalId: Int?) {
        if (!inspirationList.isEmpty()) {
            currentInspiration = inspirationList.find { it.goalId == goalId }
            if (currentInspiration != null) {
                var bitmap: Bitmap = BitmapFactory.decodeFile(currentInspiration?.image)
                binding?.ivGoalImage?.setImageBitmap(bitmap)
                binding?.etSetPhrase?.setText(currentInspiration?.phrase.toString())
                isNew = false
            } else {
                isNew = true
            }
        }
    }

    val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
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
                        getString(R.string.PERMISSIONS_GRANTED_EXTERNAL_STORAGE_MESSAGE),
                        Toast.LENGTH_LONG
                    ).show()
                    val pickIntent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGalleryLauncher.launch(pickIntent)

                } else {
                    if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                        Toast.makeText(
                            this,
                            getString(R.string.PERMISSIONS_DENIED_EXTERNAL_STORAGE_MESSAGE),
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
                getString(R.string.APPLICATION_NAME), getString(R.string.APPLICATION_NAME) + " "
                        + getString(R.string.NEEDS_ACCESS_EXTERNAL_STORAGE_MESSAGE)
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
            radioButton.text = GoalTypeEnum.values().find { it.number == goal }?.label

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
        var file = wrapper.getDir(getString(R.string.IMAGES_DIRECTORY_NAME), Context.MODE_PRIVATE)

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

    override fun onAcceptButtonCLicked() {
        if (binding?.etSetPhrase?.text?.toString() != "" &&
            binding?.ivGoalImage?.drawable != null
        ) {
            val filePath = saveImageToInternalStorage(binding?.ivGoalImage?.drawable)

            //Update ViewModel Data
            inspirationViewModel.image.value = filePath.toString()
            inspirationViewModel.phrase.value = binding?.etSetPhrase?.text.toString()

            if (isNew) {
                val radioButtonSelected =
                    findViewById(binding?.rgSelectedGoals?.checkedRadioButtonId as Int) as RadioButton
                var goalID = GoalTypeEnum.values().find { it.label == radioButtonSelected.text }?.number as Int
                //Update ViewModel Data for INSERT
                inspirationViewModel.goalId.value = goalID
                inspirationViewModel.insert()
            } else {
                //Update ViewModel Data for UPDATE
                inspirationViewModel.id.value = currentInspiration!!.id
                inspirationViewModel.goalId.value = currentInspiration!!.goalId
                inspirationViewModel.update()
            }
            finish()
        } else {
            Toast.makeText(
                this,
                getString(R.string.CREATE_INSPIRATION_SET_FIELDS_VALIDATION),
                Toast.LENGTH_LONG
            ).show()
        }
    }
    override fun onCancelButtonCLicked() {
        finish()
    }
}






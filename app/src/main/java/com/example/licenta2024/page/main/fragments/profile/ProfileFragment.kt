package com.example.licenta2024.page.main.fragments.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import com.example.licenta2024.R
import com.example.licenta2024.data.DatabaseManager
import com.example.licenta2024.data.User
import com.example.licenta2024.page.start.StartActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var currentUser: User
    private var userId = -1L
    private lateinit var firstName: TextView
    private lateinit var lastName: TextView
    private lateinit var age: TextView
    private lateinit var activityLevel: TextView
    private lateinit var calorieGoal: TextView
    private lateinit var bmr: TextView
    private lateinit var tdee: TextView
    private lateinit var logOut: CardView
    private lateinit var editObjectives: CardView
    private lateinit var caloriesGoal: TextView
    private lateinit var proteinGoal: TextView
    private lateinit var carbsGoal: TextView
    private lateinit var fatsGoal: TextView
    private lateinit var profilePicture: ImageView
    private lateinit var placeHolder: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = getCurrentUserId()
        if (userId == -1L) {
            goToLogin()
        } else {
            getCurrentUser()
        }
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstName = view.findViewById(R.id.first_name)
        lastName = view.findViewById(R.id.last_name)
        age = view.findViewById(R.id.age)
        activityLevel = view.findViewById(R.id.activity_level)
        calorieGoal = view.findViewById(R.id.calorie_goal)
        bmr = view.findViewById(R.id.bmr_value)
        tdee = view.findViewById(R.id.tdee_value)
        logOut = view.findViewById(R.id.log_out_card)
        caloriesGoal = view.findViewById(R.id.text_calorie_value)
        proteinGoal = view.findViewById(R.id.text_protein_value)
        carbsGoal = view.findViewById(R.id.text_carbs_value)
        fatsGoal = view.findViewById(R.id.text_fats_value)
        editObjectives = view.findViewById(R.id.edit_objectives_card)
        editObjectives.setOnClickListener { editObjectivesDialog() }
        profilePicture = view.findViewById(R.id.profile_bk)
        profilePicture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
        placeHolder = view.findViewById(R.id.no_profile_picture)
        logOut.setOnClickListener {
            logOut()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, 1)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // Get the captured image
            val imageBitmap = data?.extras?.get("data") as Bitmap
            editProfilePicture(imageBitmap)
            val circularDrawable = transformToCircularDrawable(imageBitmap)
            profilePicture.setImageDrawable(circularDrawable)
            placeHolder.visibility = View.GONE
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            2
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    private fun editObjectivesDialog() {
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(20, 10, 5, 5)

        fun createLabeledEditText(labelText: String, editTextValue: String?): EditText {
            val label = TextView(requireContext())
            label.text = labelText
            layout.addView(label)

            val editText = EditText(requireContext())
            editText.setText(editTextValue)
            layout.addView(editText)

            return editText
        }

        val caloriesInput =
            createLabeledEditText("Calories goal", currentUser.goals?.calories?.toString())
        val proteinInput =
            createLabeledEditText("Protein goal", currentUser.goals?.protein?.toString())
        val fatsInput = createLabeledEditText("Fats goal", currentUser.goals?.fats?.toString())
        val carbsInput = createLabeledEditText("Carbs goal", currentUser.goals?.carbs?.toString())
        val waterIntake = createLabeledEditText(
            "Water intake goal",
            currentUser.goals?.waterIntakeGoal?.toString()
        )
        val stepsInput =
            createLabeledEditText("Steps goal", currentUser.goals?.stepGoal?.toString())

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Objectives")
            .setView(layout)
            .setPositiveButton("Save") { dialog, _ ->
                val calories = caloriesInput.text.toString()
                val protein = proteinInput.text.toString()
                val fats = fatsInput.text.toString()
                val carbs = carbsInput.text.toString()
                val water = waterIntake.text.toString()
                val steps = stepsInput.text.toString()
                editObjectives(calories, protein, fats, carbs, water, steps)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.window?.setBackgroundDrawableResource(R.color.light_gray)
        dialog.show()
    }


    private fun editObjectives(
        calories: String,
        protein: String,
        fats: String,
        carbs: String,
        waterIntake: String,
        steps: String
    ) {
        val updatedUser = currentUser
        val updatedGoals = currentUser.goals
        updatedGoals.let {
            it?.calories = calories.toInt()
            it?.protein = protein.toInt()
            it?.fats = fats.toInt()
            it?.carbs = carbs.toInt()
            it?.waterIntakeGoal = waterIntake.toInt()
            it?.stepGoal = steps.toInt()
        }
        updatedUser.goals = updatedGoals
        postUpdatedUser(updatedUser)
    }

    private fun editProfilePicture(
        picture: Bitmap
    ) {
        val updatedUser = currentUser
        updatedUser.image = picture
        postUpdatedUser(updatedUser)
    }

    private fun postUpdatedUser(updatedUser: User) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseManager.updateUser(updatedUser)
        }
    }

    private fun logOut() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", "-1")
        editor.apply()
        goToLogin()
    }

    private fun updateProfileData() {
        firstName.text = currentUser.firstName
        lastName.text = currentUser.lastName
        age.text = currentUser.age.toString()
        activityLevel.text = currentUser.activityLevel
        calorieGoal.text = currentUser.goals?.calories.toString()
        bmr.text = currentUser.goals?.bmr.toString()
        tdee.text = currentUser.goals?.tdee.toString()
        caloriesGoal.text = currentUser.goals?.calories.toString()
        proteinGoal.text = currentUser.goals?.protein.toString()
        carbsGoal.text = currentUser.goals?.carbs.toString()
        fatsGoal.text = currentUser.goals?.fats.toString()
        currentUser.image?.let {
            val circularDrawable = transformToCircularDrawable(it)
            profilePicture.setImageDrawable(circularDrawable)
            placeHolder.visibility = View.GONE
        }
    }

    private fun getCurrentUser() {
        DatabaseManager.getUser(userId).observe(viewLifecycleOwner) { user ->
            currentUser = user
            updateProfileData()
        }
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), StartActivity::class.java)
        startActivity(intent)
    }

    private fun transformToCircularDrawable(bitmap: Bitmap): Drawable {
        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        circularBitmapDrawable.isCircular = true
        return circularBitmapDrawable
    }

    private fun getCurrentUserId(): Long {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userIdString: String? = sharedPreferences.getString("userId", null)
        return userIdString?.toLongOrNull() ?: -1L
    }
}
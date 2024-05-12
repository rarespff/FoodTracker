package com.example.licenta2024.page.main.fragments.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
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
    private lateinit var profileBk: View
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
        profileBk = view.findViewById(R.id.profile_bk)
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
        logOut.setOnClickListener {
            logOut()
        }
    }

    private fun editObjectivesDialog() {
        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL

        val caloriesInput = EditText(requireContext())
        caloriesInput.hint = "Calories goal"
        layout.addView(caloriesInput)

        val proteinInput = EditText(requireContext())
        proteinInput.hint = "Protein goal"
        layout.addView(proteinInput)

        val fatsInput = EditText(requireContext())
        fatsInput.hint = "Fats goal"
        layout.addView(fatsInput)

        val carbsInput = EditText(requireContext())
        carbsInput.hint = "Carbs goal"
        layout.addView(carbsInput)

        val waterIntake = EditText(requireContext())
        waterIntake.hint = "Water intake goal"
        layout.addView(waterIntake)

        val steps = EditText(requireContext())
        steps.hint = "Steps goal"
        layout.addView(steps)
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Objectives")
            .setView(layout)
            .setPositiveButton("Save") { dialog, _ ->
                val calories = caloriesInput.text.toString()
                val protein = proteinInput.text.toString()
                val fats = fatsInput.text.toString()
                val carbs = carbsInput.text.toString()
                val water = waterIntake.text.toString()
                val step = steps.text.toString()
                editObjectives(calories, protein, fats, carbs, water, step)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
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

    private fun getCurrentUserId(): Long {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userIdString: String? = sharedPreferences.getString("userId", null)
        return userIdString?.toLongOrNull() ?: -1L
    }
}
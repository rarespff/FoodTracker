package com.example.licenta2024.page.main.fragments.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.licenta2024.R
import com.example.licenta2024.data.DatabaseManager
import com.example.licenta2024.data.User
import com.example.licenta2024.page.start.StartActivity

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
        logOut.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", id.toString())
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
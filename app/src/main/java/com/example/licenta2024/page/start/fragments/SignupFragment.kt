import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.licenta2024.R
import com.example.licenta2024.data.DatabaseManager
import com.example.licenta2024.data.Goals
import com.example.licenta2024.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {

    private lateinit var etEmail: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etAge: EditText
    private lateinit var etWeight: EditText
    private lateinit var etHeight: EditText
    private lateinit var spinnerGender: Spinner
    private lateinit var spinnerActivityLevel: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etEmail = view.findViewById(R.id.etEmail)
        etFirstName = view.findViewById(R.id.etFirstName)
        etLastName = view.findViewById(R.id.etLastName)
        etPassword = view.findViewById(R.id.etPassword)
        etAge = view.findViewById(R.id.etAge)
        etWeight = view.findViewById(R.id.etWeight)
        etHeight = view.findViewById(R.id.etHeight)
        spinnerGender = view.findViewById(R.id.spinnerGender)
        spinnerActivityLevel = view.findViewById(R.id.spinnerActivityLevel)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.genders_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGender.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.activity_levels_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerActivityLevel.adapter = adapter
        }

        val btnSignup = view.findViewById<Button>(R.id.btnSignup)
        val btnLoginInstead = view.findViewById<Button>(R.id.btnLoginInstead)

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                btnSignup.isEnabled = areFieldsNotEmpty()
            }
        }

        etEmail.addTextChangedListener(textWatcher)
        etFirstName.addTextChangedListener(textWatcher)
        etLastName.addTextChangedListener(textWatcher)
        etPassword.addTextChangedListener(textWatcher)
        etAge.addTextChangedListener(textWatcher)
        etWeight.addTextChangedListener(textWatcher)
        etHeight.addTextChangedListener(textWatcher)

        btnSignup.setOnClickListener {
            val email = etEmail.text.toString()
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val password = etPassword.text.toString()
            val age = etAge.text.toString().toIntOrNull() ?: 0
            val weight = etWeight.text.toString().toIntOrNull() ?: 0
            val height = etHeight.text.toString().toIntOrNull() ?: 0
            val gender = spinnerGender.selectedItem.toString()
            val activityLevel = spinnerActivityLevel.selectedItem.toString()
            val goals = calculateGoals(weight, height, age, gender, activityLevel)
            val user = User(
                email = email,
                firstName = firstName,
                lastName = lastName,
                password = password,
                age = age,
                weight = weight,
                height = height,
                activityLevel = activityLevel,
                gender = gender,
                image = null,
                goals = goals,
                days = listOf()
            )
            Toast.makeText(
                requireContext(),
                "Account created, you can log in now!",
                Toast.LENGTH_SHORT
            ).show()
            requireActivity().supportFragmentManager.popBackStack()
            CoroutineScope(Dispatchers.IO).launch {
                DatabaseManager.addUser(user)
            }
        }

        btnLoginInstead.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun calculateGoals(
        weight: Int,
        height: Int,
        age: Int,
        gender: String,
        activityLevel: String
    ): Goals {
        val protein: Int
        val carbs: Int
        val fats: Int
        val bmr: Double = if (gender.equals("Male", ignoreCase = true)) {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }
        val tdee: Double = when (activityLevel) {
            "Sedentary" -> bmr * 1.2
            "Lightly Active" -> bmr * 1.375
            "Moderately Active" -> bmr * 1.55
            "Very Active" -> bmr * 1.725
            "Extra Active" -> bmr * 1.9
            else -> bmr // Default to sedentary level
        }
        protein = (0.25 * tdee / 4).toInt()
        carbs = (0.5 * tdee / 4).toInt()
        fats = (0.25 * tdee / 9).toInt()

        return Goals(
            calories = tdee.toInt(),
            protein = protein,
            carbs = carbs,
            fats = fats,
            stepGoal = 10000,
            waterIntakeGoal = 2000,
            bmr = bmr.toInt(),
            tdee = tdee.toInt()
        )
    }

    private fun areFieldsNotEmpty(): Boolean {
        return etEmail.text.isNotBlank() &&
                etFirstName.text.isNotBlank() &&
                etLastName.text.isNotBlank() &&
                etPassword.text.isNotBlank() &&
                etAge.text.isNotBlank() &&
                etWeight.text.isNotBlank() &&
                etHeight.text.isNotBlank()
    }
}
package com.example.licenta2024.page.main.fragments.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.licenta2024.R
import com.example.licenta2024.data.DatabaseManager
import com.example.licenta2024.data.Day
import com.example.licenta2024.data.Goals
import com.example.licenta2024.data.User
import com.example.licenta2024.page.main.MainActivity
import com.example.licenta2024.page.start.StartActivity
import com.github.lzyzsd.circleprogress.DonutProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var calendarRv: RecyclerView
    private lateinit var adapter: JournalDayAdapter
    private lateinit var currentDay: Day
    private lateinit var currentGoals: Goals
    private lateinit var caloriesGoal: TextView
    private lateinit var consumedCalories: TextView
    private lateinit var burnedCalories: TextView
    private lateinit var caloriesProgressBar: DonutProgress
    private lateinit var proteinProgressBar: ProgressBar
    private lateinit var carbsProgressBar: ProgressBar
    private lateinit var fatsProgressBar: ProgressBar
    private lateinit var proteinValue: TextView
    private lateinit var carbsValue: TextView
    private lateinit var fatsValue: TextView
    private lateinit var proteinGoal: TextView
    private lateinit var carbsGoal: TextView
    private lateinit var fatsGoal: TextView
    private lateinit var stepsCount: TextView
    private lateinit var stepsGoal: TextView
    private lateinit var stepsProgressBar: ProgressBar
    private lateinit var waterProgressBar: DonutProgress
    private lateinit var addBreakfastButton: ImageView
    private lateinit var addLunchButton: ImageView
    private lateinit var addDinnerButton: ImageView
    private lateinit var addWater250: Button
    private lateinit var addWater500: Button
    private lateinit var addWater750: Button
    private lateinit var breakfastRv: RecyclerView
    private lateinit var lunchRv: RecyclerView
    private lateinit var dinnerRv: RecyclerView
    private lateinit var noFoodAddedBreakfast: TextView
    private lateinit var noFoodAddedLunch: TextView
    private lateinit var noFoodAddedDinner: TextView
    private val breakfastAdapter = FoodAdapter(listOf())
    private val lunchAdapter = FoodAdapter(listOf())
    private val dinnerAdapter = FoodAdapter(listOf())
    private var isToday = true
    private lateinit var currentUser: User
    private var userId = -1L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.home_fragment, container, false)
        userId = getCurrentUserId()
        if (userId == -1L) {
            goToLogin()
        }
        calendarRv = rootView.findViewById(R.id.calendar_rv)
        setUpViews(rootView)
        getCurrentUser()
        getStepCount { }
        return rootView
    }

    private fun getStepCount(callback: (Int) -> Unit) {
        val sensorManager: SensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Log.e("STEP SENSOR", "sensor not available")
        }
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // Extract step count from the sensor event
                val steps = event.values[0].toInt()
                Log.e("STEP SENSOR", steps.toString())
                callback(steps)
                // Unregister listener after receiving the first step count update
                sensorManager.unregisterListener(this)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Handle accuracy change if needed
            }
        }

        // Register sensor listener
        stepSensor?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun updateViews() {
        addDinnerButton.setOnClickListener { (activity as? MainActivity)?.navigateToRecipesFragment() }
        addLunchButton.setOnClickListener { (activity as? MainActivity)?.navigateToRecipesFragment() }
        addBreakfastButton.setOnClickListener { (activity as? MainActivity)?.navigateToRecipesFragment() }
        addDinnerButton.isEnabled = isToday
        addBreakfastButton.isEnabled = isToday
        addLunchButton.isEnabled = isToday
        addWater250.isEnabled = isToday
        addWater500.isEnabled = isToday
        addWater750.isEnabled = isToday
        if (isToday) {
            addWater750.setBackgroundColor(resources.getColor(R.color.yellow))
            addWater250.setBackgroundColor(resources.getColor(R.color.yellow))
            addWater500.setBackgroundColor(resources.getColor(R.color.yellow))
            addWater500.setOnClickListener {
                addWater(500)
            }
            addWater250.setOnClickListener {
                addWater(250)
            }
            addWater750.setOnClickListener {
                addWater(750)
            }
            addDinnerButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.plus
                )
            )
            addLunchButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.plus
                )
            )
            addBreakfastButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.plus
                )
            )
        } else {
            addWater750.setBackgroundColor(resources.getColor(R.color.dark_gray))
            addWater250.setBackgroundColor(resources.getColor(R.color.dark_gray))
            addWater500.setBackgroundColor(resources.getColor(R.color.dark_gray))
            addDinnerButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.plus_disabled
                )
            )
            addLunchButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.plus_disabled
                )
            )
            addBreakfastButton.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.plus_disabled
                )
            )
        }
        caloriesGoal.text = currentGoals.calories.toString()
        consumedCalories.text = currentDay.totalConsumedCalories.toString()
        burnedCalories.text = (currentDay.steps * 0.45).toString()
        caloriesProgressBar.max = currentGoals.calories
        var currentCalories =
            currentDay.totalConsumedCalories - currentDay.totalBurnedCalories
        if (currentCalories < 0) {
            currentCalories = 0
        }
        if (currentCalories > caloriesProgressBar.max) {
            caloriesProgressBar.progress = caloriesProgressBar.max
        } else {
            caloriesProgressBar.progress = currentCalories
        }
        proteinProgressBar.max = currentGoals.protein
        if (currentDay.proteinIntake > proteinProgressBar.max) {
            proteinProgressBar.progress = proteinProgressBar.max
        } else {
            proteinProgressBar.progress = currentDay.proteinIntake
        }
        carbsProgressBar.max = currentGoals.carbs
        if (currentDay.carbsIntake > carbsProgressBar.max) {
            carbsProgressBar.progress = carbsProgressBar.max
        } else {
            carbsProgressBar.progress = currentDay.carbsIntake
        }
        fatsProgressBar.max = currentGoals.fats
        if (currentDay.fatsIntake > fatsProgressBar.max) {
            fatsProgressBar.progress = fatsProgressBar.max
        } else {
            fatsProgressBar.progress = currentDay.fatsIntake
        }
        proteinValue.text = currentDay.proteinIntake.toString()
        carbsValue.text = currentDay.carbsIntake.toString()
        fatsValue.text = currentDay.fatsIntake.toString()
        proteinGoal.text = currentGoals.protein.toString()
        carbsGoal.text = currentGoals.carbs.toString()
        fatsGoal.text = currentGoals.fats.toString()
        stepsCount.text = currentDay.steps.toString()
        stepsGoal.text = currentGoals.stepGoal.toString()
        stepsProgressBar.max = currentGoals.stepGoal
        if (currentDay.steps > stepsProgressBar.max) {
            stepsProgressBar.progress = stepsProgressBar.max
        } else {
            stepsProgressBar.progress = currentDay.steps
        }
        waterProgressBar.max = currentGoals.waterIntakeGoal
        if (currentDay.waterIntake > waterProgressBar.max) {
            waterProgressBar.progress = waterProgressBar.max
        } else {
            waterProgressBar.progress = currentDay.waterIntake
        }
        if (currentDay.breakfast.isNotEmpty()) {
            breakfastAdapter.updateData(currentDay.breakfast)
            noFoodAddedBreakfast.visibility = View.GONE
        } else {
            noFoodAddedBreakfast.visibility = View.VISIBLE
            breakfastAdapter.updateData(listOf())
        }
        if (currentDay.lunch.isNotEmpty()) {
            lunchAdapter.updateData(currentDay.lunch)
            noFoodAddedLunch.visibility = View.GONE
        } else {
            noFoodAddedLunch.visibility = View.VISIBLE
            lunchAdapter.updateData(listOf())
        }
        if (currentDay.dinner.isNotEmpty()) {
            dinnerAdapter.updateData(currentDay.dinner)
            noFoodAddedDinner.visibility = View.GONE
        } else {
            noFoodAddedDinner.visibility = View.VISIBLE
            dinnerAdapter.updateData(listOf())
        }
    }

    private fun updateCurrentDayData(dayItem: DayItem) {
        if (currentUser.goals != null) {
            currentGoals = currentUser.goals!!
        }
        currentDay = currentUser.days.find { it.dateId == dayItem.dateId } ?: Day(
            "invalid",
            dayItem.day,
            dayItem.dayName,
            dayItem.dayMonth,
            0,
            0,
            0,
            0,
            0,
            0,
            listOf(),
            listOf(),
            listOf(),
            0
        )
        updateViews()
        Log.e("currentDay: ", currentDay.toString())
    }

    private fun setUpViews(rootView: View) {
        val customToolbar = rootView.findViewById<LinearLayout>(R.id.customToolbar)
        val dayNameTextView = customToolbar.findViewById<TextView>(R.id.dayNameTextView)
        val days = generateDayItems()
        adapter = JournalDayAdapter(calendarRv, days, dayNameTextView) { day, position ->
            if (position >= 3) {
                calendarRv.scrollToPosition(position - 3)
            } else {
                calendarRv.scrollToPosition(0)
            }
            dayNameTextView.text = getFullDayName(day.dayName)
            isToday = getCurrentDayItem().dateId == day.dateId
            updateViews()
            updateCurrentDayData(day)
        }
        calendarRv.adapter = adapter
        caloriesGoal = rootView.findViewById(R.id.calories_goal)
        consumedCalories = rootView.findViewById(R.id.consumed_calories)
        burnedCalories = rootView.findViewById(R.id.burned_calories)
        caloriesProgressBar = rootView.findViewById(R.id.calories_progress)
        proteinProgressBar = rootView.findViewById(R.id.protein_progress_bar)
        proteinValue = rootView.findViewById(R.id.protein_value)
        carbsProgressBar = rootView.findViewById(R.id.carbs_progress_bar)
        carbsValue = rootView.findViewById(R.id.carbs_value)
        fatsProgressBar = rootView.findViewById(R.id.fats_progress_bar)
        fatsValue = rootView.findViewById(R.id.fats_value)
        proteinGoal = rootView.findViewById(R.id.protein_goal)
        carbsGoal = rootView.findViewById(R.id.carbs_goal)
        fatsGoal = rootView.findViewById(R.id.fats_goal)
        stepsCount = rootView.findViewById(R.id.step_count)
        stepsGoal = rootView.findViewById(R.id.step_goal)
        stepsProgressBar = rootView.findViewById(R.id.steps_count_progress)
        waterProgressBar = rootView.findViewById(R.id.water_intake_progress)
        addBreakfastButton = rootView.findViewById(R.id.add_breakfast_button)
        addDinnerButton = rootView.findViewById(R.id.add_dinner_button)
        addLunchButton = rootView.findViewById(R.id.add_lunch_button)
        addWater250 = rootView.findViewById(R.id.plus_250)
        addWater500 = rootView.findViewById(R.id.plus_500)
        addWater750 = rootView.findViewById(R.id.plus_750)
        breakfastRv = rootView.findViewById(R.id.breakfast_rv)
        lunchRv = rootView.findViewById(R.id.lunch_rv)
        dinnerRv = rootView.findViewById(R.id.dinner_rv)
        breakfastRv.adapter = breakfastAdapter
        lunchRv.adapter = lunchAdapter
        dinnerRv.adapter = dinnerAdapter
        noFoodAddedBreakfast = rootView.findViewById(R.id.no_breakfast_added)
        noFoodAddedLunch = rootView.findViewById(R.id.no_lunch_added)
        noFoodAddedDinner = rootView.findViewById(R.id.no_dinner_added)
    }

    private fun addWater(quantity: Int) {
        val currentDay = currentUser.days.find { it.dateId == getCurrentDayItem().dateId }
        if (currentDay != null) {
            val updatedWaterIntake = currentDay.waterIntake + quantity
            val updatedDay = currentDay.copy(waterIntake = updatedWaterIntake)
            val updatedDays =
                currentUser.days.map { if (it.dateId == updatedDay.dateId) updatedDay else it }
            val updatedUser = currentUser.copy(days = updatedDays)
            postUpdatedUser(updatedUser)
        } else {
            val today = getCurrentDayItem()
            val newDay = Day(
                today.dateId,
                today.day,
                today.dayName,
                today.dayMonth,
                0,
                0,
                0,
                0,
                0,
                quantity,
                listOf(),
                listOf(),
                listOf(),
                0
            )
            val updatedDays = currentUser.days as MutableList
            updatedDays.add(newDay)
            val updatedUser = currentUser.copy(days = updatedDays)
            postUpdatedUser(updatedUser)
        }
    }

    private fun postUpdatedUser(updatedUser: User) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseManager.updateUser(updatedUser)
        }
    }

    private fun getCurrentUser() {
        DatabaseManager.getUser(userId).observe(viewLifecycleOwner) { user ->
            if (user != null) {
                currentUser = user
                updateCurrentDayData(getCurrentDayItem())
            }
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

    private fun generateDayItems(): List<DayItem> {
        val calendar = Calendar.getInstance()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayItems = mutableListOf<DayItem>()
        val dateFormat = SimpleDateFormat("E", Locale.getDefault())

        for (i in 1..daysInMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, i)
            val dayName = dateFormat.format(calendar.time)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val year = calendar.get(Calendar.YEAR)
            val dateId = "$day${month.toString().padStart(2, '0')}$year"

            dayItems.add(DayItem(dateId, day.toString(), dayName, month.toString()))
        }
        return dayItems
    }

    private fun getCurrentDayItem(): DayItem {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("E", Locale.getDefault())
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        val currentDayName = dateFormat.format(calendar.time)
        val currentDateId = "$currentDay${currentMonth.toString().padStart(2, '0')}$currentYear"
        return DayItem(
            currentDateId,
            currentDay.toString(),
            currentDayName,
            currentMonth.toString()
        )
    }

    private fun getFullDayName(shortDayName: String): String {
        return when (shortDayName.lowercase(Locale.ROOT)) {
            "sun" -> "Sunday"
            "mon" -> "Monday"
            "tue" -> "Tuesday"
            "wed" -> "Wednesday"
            "thu" -> "Thursday"
            "fri" -> "Friday"
            "sat" -> "Saturday"
            else -> throw IllegalArgumentException("Invalid day name: $shortDayName")
        }
    }
}
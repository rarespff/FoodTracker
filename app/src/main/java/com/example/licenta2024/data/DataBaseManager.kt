package com.example.licenta2024.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore

object DatabaseManager {
    private lateinit var database: AppDatabase
    private val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val userCollection = firestore.collection("users")

    fun initialize(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "nutrition-db"
        ).build()
    }

    suspend fun addUser(user: User) {
        database.userDao().addUser(user)
        addUserToFirestore(user)
    }

    fun getUser(userId: Long): LiveData<User> {
        val userLiveData = MutableLiveData<User>()
        userCollection.document(userId.toString())
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    Log.e("getUserCalled","user: ${documentSnapshot.data}")
                    val user = documentSnapshot.toObject(User::class.java)
                    Log.e("getUserCalled","user: $user")
                    if (user != null) {
                        userLiveData.value = user as User
                    }
                } else {
                    database.userDao().getUser(userId).observeForever { user ->
                        userLiveData.value = user
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("DatabaseManager", "Error getting user by userId: ${exception.message}")
            }
        return userLiveData
    }

    suspend fun updateUser(user: User, updateProfileData: () -> Unit) {
        database.userDao().updateUser(user)
        updateUserInFirestore(user) { updateProfileData() }
    }

    fun getUserByEmail(email: String): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()

        userCollection.whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // If a user with the given email is found, extract the data and set it to LiveData
                    val user = querySnapshot.documents[0].toObject(User::class.java)
                    userLiveData.value = user
                } else {
                    // If no user is found with the given email, set LiveData value to null
                    userLiveData.value = null
                }
            }
            .addOnFailureListener { exception ->
                // If an error occurs, log the error and set LiveData value to null
                Log.e("DatabaseManager", "Error getting user by email: ${exception.message}")
                userLiveData.value = null
            }
        return userLiveData
    }

    private fun addUserToFirestore(user: User) {
        userCollection.document(user.userId.toString())
            .set(user)
            .addOnSuccessListener {
                Log.e("rares", "user added to firestore")
            }
            .addOnFailureListener { e ->
                Log.e("rares", "failed: ${e.message}")
            }
    }

    private fun updateUserInFirestore(user: User, updateProfileData: () -> Unit) {
        userCollection.document(user.userId.toString())
            .set(user)
            .addOnSuccessListener {
                updateProfileData()
                Log.e("rares", "user added to firestore")
            }
            .addOnFailureListener { e ->
                Log.e("rares", "failed: ${e.message}")
            }
    }
}

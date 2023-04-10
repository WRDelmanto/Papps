package com.wrdelmanto.papps.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

// Firebase variables
// Database root
private var db = Firebase.database.reference

// Users table
const val USERS_TABLE = "users"

// Click counter reference
const val CLICK_COUNTER_REF = "click-counter"

/**
 * Get the current users from the database.
 *
 */
fun getCurrentUser() = Firebase.auth.currentUser?.uid.toString()

/**
 * Add the users table to the database.
 *
 */
fun configDatabase() = addUsersTable()

/**
 * Add the users table to the database.
 *
 */
fun addUsersTable() = db.child(USERS_TABLE).setValue("")

/**
 * Add the click counter score to the database.
 *
 * @param score
 * @param appOrGame
 */
fun putFirebase(score: String, appOrGame: String) {
    configDatabase()
    db.child(USERS_TABLE).child(getCurrentUser()).child(appOrGame).setValue(score)
}

/**
 * Add the click counter score to the database.
 *
 * @param appOrGame
 */
fun getFirebase(appOrGame: String, valueType: Any?): Any {
    return db.child("users").child(getCurrentUser()).child(appOrGame).get().toString()
//    return when (valueType) {
//        "String" -> db.child(USERS_TABLE).child(getCurrentUser()).child(appOrGame).get().toString()
//        "Test" -> db.get()
//        else -> error("Only primitive types can be returned from Firebase")
//    }
}

/**
 * Self authenticates to firebase.
 *
 */
fun selfAnonymouslyAuthentication() {
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser == null) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) logD { "signInAnonymously: Success" }
                else logD { "signInAnonymously: Failed" }
            }
    } else logD { "signInAnonymously: Already Authenticated" }
}
package com.trescaballeros.utvend.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class VendingMachine(
    val location: GeoPoint,
    val photoUrl: String,
    val locDesc: String,
    val notes: String,
    val timestamp: Timestamp
)
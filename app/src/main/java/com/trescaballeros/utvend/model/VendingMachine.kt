package com.trescaballeros.utvend.model

import android.graphics.drawable.Drawable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class VendingMachine (
    var id: String = "",
    var location: GeoPoint = GeoPoint(0.0, 0.0),
    var image: String = "",
    var geo_notes: String = "",
    var extra_notes: String = "",
    var timestamp: Timestamp = Timestamp(0,0),
    var drawable: Drawable? = null
)
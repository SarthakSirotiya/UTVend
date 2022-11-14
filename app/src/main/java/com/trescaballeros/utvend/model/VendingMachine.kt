package com.trescaballeros.utvend.model

import android.graphics.drawable.Drawable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.Expose

data class VendingMachine (
    @Expose
    var id: String = "",
    @Expose
    var location: GeoPoint = GeoPoint(0.0, 0.0),
    @Expose
    var image: String = "",
    @Expose
    var geo_notes: String = "",
    @Expose
    var extra_notes: String = "",
    @Expose
    var timestamp: Timestamp = Timestamp(0,0),
    var drawable: Drawable? = null
)
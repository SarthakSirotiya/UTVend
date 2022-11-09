package com.trescaballeros.utvend.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class VendingMachine (
    var location: GeoPoint = GeoPoint(0.0, 0.0),
    var image: String = "",
    var geo_notes: String = "",
    var extra_notes: String = "",
    var timestamp: Timestamp = Timestamp(0,0)
) {

}
package com.trescaballeros.utvend.model

import com.google.firebase.firestore.GeoPoint
import com.google.gson.*
import java.lang.reflect.Type

class GeopointSerializer : JsonSerializer<GeoPoint>, JsonDeserializer<GeoPoint> {
    override fun serialize(
        src: GeoPoint?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val json = JsonObject()
        json.addProperty("latitude", src?.latitude)
        json.addProperty("longitude", src?.longitude)
        return json
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): GeoPoint {
        val obj = json?.asJsonObject
        val lat = obj?.get("latitude")?.asDouble
        val long = obj?.get("longitude")?.asDouble
        return GeoPoint(lat!!, long!!)
    }
}
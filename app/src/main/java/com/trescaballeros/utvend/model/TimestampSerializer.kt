package com.trescaballeros.utvend.model

import com.google.firebase.Timestamp
import com.google.gson.*
import java.lang.reflect.Type

class TimestampSerializer : JsonSerializer<Timestamp>, JsonDeserializer<Timestamp> {
    override fun serialize(
        src: Timestamp?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val json = JsonObject()
        json.addProperty("seconds", src?.seconds)
        json.addProperty("nanoseconds", src?.nanoseconds)
        return json
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Timestamp {
        val obj = json?.asJsonObject
        val seconds = obj?.get("seconds")?.asLong
        val nanoseconds = obj?.get("nanoseconds")?.asInt
        return Timestamp(seconds!!, nanoseconds!!)
    }
}
package com.trescaballeros.utvend.adapter

// XXX START
// DO NOT DELETE -Manuel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.GsonBuilder
import com.trescaballeros.utvend.R
import com.trescaballeros.utvend.ViewActivity
import com.trescaballeros.utvend.model.GeopointSerializer
import com.trescaballeros.utvend.model.JavaVendingMachine
import com.trescaballeros.utvend.model.TimestampSerializer

class VendingMachineAdapter(
    private val context: Context,
    val options: FirestoreRecyclerOptions<JavaVendingMachine>
) : FirestoreRecyclerAdapter<JavaVendingMachine, VendingMachineAdapter.VendingMachineViewHolder>(
    options
) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(GeoPoint::class.java, GeopointSerializer())
        .registerTypeAdapter(Timestamp::class.java, TimestampSerializer())
        .create()
    val storageRef = Firebase.storage.reference

    class VendingMachineViewHolder(private val view: View?) : RecyclerView.ViewHolder(view!!) {
        var myView = view

        // Put views here, example from astire video
        // val textView: TextView = view.findViewById(R.id.item_title)
        var geoTextView: TextView? = view?.findViewById(R.id.geo_notes_id)
        var extraTextView: TextView? = view?.findViewById(R.id.extra_notes_id)
        var timeTextView: TextView? = view?.findViewById(R.id.timestamp_id)
        var imageView: ImageView? = view?.findViewById(R.id.image_id)
    }

    override fun onCreateViewHolder(parentGroup: ViewGroup, i: Int): VendingMachineViewHolder {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        var view: View? = LayoutInflater.from(parentGroup.context)
            .inflate(R.layout.vertical_card, parentGroup, false)

        return VendingMachineViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: VendingMachineViewHolder,
        position: Int,
        model: JavaVendingMachine
    ) {
        // Only trying with one textview first
        holder.geoTextView?.text = "Where: " + model.geo_notes
        holder.extraTextView?.text = "Notes: " + model.extra_notes
        holder.timeTextView?.text = "Image Taken: "+model.timestamp.toDate().toString()

        val pathRef = storageRef.child(model.image)

        pathRef.downloadUrl.addOnSuccessListener { uri ->
            holder.imageView?.load(uri)
        }

        holder.myView?.setOnClickListener {
            val intent = Intent(context, ViewActivity::class.java)
            intent.putExtra("vm", gson.toJson(model))
            context.startActivity(intent)
        }
    }

    override fun onDataChanged() {
        super.onDataChanged()
    }
}
// XXX END
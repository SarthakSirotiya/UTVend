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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.trescaballeros.utvend.R
import com.trescaballeros.utvend.ViewActivity
import com.trescaballeros.utvend.model.JavaVendingMachine

class VendingMachineAdapter(private val context: Context, val options: FirestoreRecyclerOptions<JavaVendingMachine>)
    : FirestoreRecyclerAdapter<JavaVendingMachine, VendingMachineAdapter.VendingMachineViewHolder>(options) {

    private val gson = Gson()

    class VendingMachineViewHolder(private val view: View?): RecyclerView.ViewHolder(view!!) {
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
        var view: View? = null
        view = LayoutInflater.from(parentGroup.context).inflate(R.layout.vertical_card, parentGroup, false)

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

        val storage = Firebase.storage
        val storageRef = storage.reference
        val pathRef = storageRef.child(model.image)
        val imageBytes: Long = 1024*1024*12
        val byteArray = arrayOf<Byte>()

        pathRef.downloadUrl.addOnSuccessListener { uri ->
            holder.imageView?.load(uri)
        }

        holder.myView?.setOnClickListener {
            Toast.makeText(context, model.timestamp.toString(), Toast.LENGTH_SHORT).show()
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
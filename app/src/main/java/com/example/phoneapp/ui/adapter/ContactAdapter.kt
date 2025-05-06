package com.example.phoneapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.R
import com.example.phoneapp.models.Contact

class ContactAdapter(
    private val contacts: List<Contact>,
    private val itemClickListener: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.textViewName)
        private val phoneView: TextView = itemView.findViewById(R.id.textViewPhone)
        init {
            itemView.setOnClickListener {
                itemClickListener(contacts[adapterPosition])
            }
        }
        fun bind(contact: Contact) {
            nameView.text = contact.name
            phoneView.text = contact.phone
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}

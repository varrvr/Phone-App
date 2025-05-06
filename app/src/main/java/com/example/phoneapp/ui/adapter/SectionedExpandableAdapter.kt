package com.example.phoneapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.R
import com.example.phoneapp.models.Contact
import com.example.phoneapp.models.SectionModel
import com.google.android.material.textview.MaterialTextView

class SectionedExpandableAdapter(
    private val sections: List<SectionModel>,
    private val onContactClick: (Contact) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SECTION = 0
        const val TYPE_ENTRY   = 1
    }

    override fun getItemCount(): Int {
        var count = 0
        for (sec in sections) {
            count += 1
            if (sec.expanded) count += sec.contacts.size
        }
        return count
    }

    override fun getItemViewType(position: Int): Int {
        var pos = position
        for (sec in sections) {
            if (pos == 0) return TYPE_SECTION
            pos--
            if (sec.expanded) {
                if (pos < sec.contacts.size) return TYPE_ENTRY
                pos -= sec.contacts.size
            }
        }
        throw IndexOutOfBoundsException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_SECTION -> {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_section_header, parent, false)
            SectionViewHolder(v)
        }
        else -> {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
            EntryViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var pos = position
        for (sec in sections) {
            if (pos == 0 && holder is SectionViewHolder) {
                holder.bind(sec)
                return
            }
            pos--
            if (sec.expanded) {
                if (pos < sec.contacts.size && holder is EntryViewHolder) {
                    holder.bind(sec.contacts[pos], onContactClick)
                    return
                }
                pos -= sec.contacts.size
            }
        }
    }

    inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: MaterialTextView = itemView.findViewById(R.id.textViewSection)

        fun bind(section: SectionModel) {
            title.text = section.letter.toString()
            val iconRes = if (section.expanded)
                R.drawable.ic_arrow_down
            else
                R.drawable.ic_arrow_up
            title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, iconRes, 0)

            title.setOnClickListener {
                section.expanded = !section.expanded
                notifyDataSetChanged()
            }
        }
    }

    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView  = itemView.findViewById(R.id.textViewName)
        private val phoneView: TextView = itemView.findViewById(R.id.textViewPhone)

        fun bind(contact: Contact, onClick: (Contact) -> Unit) {
            nameView.text  = contact.name
            phoneView.text = contact.phone
            itemView.setOnClickListener { onClick(contact) }
        }
    }
}
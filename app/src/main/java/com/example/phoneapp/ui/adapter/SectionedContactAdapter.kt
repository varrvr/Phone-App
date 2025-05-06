package com.example.phoneapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.R
import com.example.phoneapp.models.*

class SectionedContactAdapter(
    private val items: List<ContactItem>,
    private val onClick: (Contact) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SECTION = 0
        private const val TYPE_ENTRY   = 1
    }

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.textViewSection)
        fun bind(letter: Char) {
            title.text = letter.toString()
        }
    }

    class EntryViewHolder(itemView: View, val onClick: (Int) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        private val nameView: TextView = itemView.findViewById(R.id.textViewName)
        private val phoneView: TextView = itemView.findViewById(R.id.textViewPhone)

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }

        fun bind(contact: Contact) {
            nameView.text = contact.name
            phoneView.text = contact.phone
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ContactItem.Section -> TYPE_SECTION
            is ContactItem.Entry   -> TYPE_ENTRY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_SECTION) {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_section_header, parent, false)
            SectionViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact, parent, false)
            EntryViewHolder(v) { pos ->
                val entry = items[pos] as ContactItem.Entry
                onClick(entry.contact)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ContactItem.Section -> (holder as SectionViewHolder).bind(item.letter)
            is ContactItem.Entry   -> (holder as EntryViewHolder).bind(item.contact)
        }
    }

    override fun getItemCount(): Int = items.size
}

package com.example.phoneapp.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.phoneapp.models.Contact
import android.Manifest
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.phoneapp.R
import com.example.phoneapp.ui.decoration.SectionSpacingDecoration
import com.example.phoneapp.ui.adapter.SectionedExpandableAdapter
import com.example.phoneapp.models.SectionModel
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {

    private val REQUEST_READ_CONTACTS = 100

    private lateinit var sectionAdapter: SectionedExpandableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS
            )
        } else {
            loadContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_CONTACTS &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadContacts()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }


    fun toggleSection(headerView: MaterialTextView, section: SectionModel) {
        val from = if (section.expanded) 180f else 0f
        val to   = if (section.expanded) 0f else 180f
        ObjectAnimator.ofFloat(headerView, "rotation", from, to)
            .setDuration(250)
            .start()

        section.expanded = !section.expanded
        sectionAdapter.notifyDataSetChanged()
    }

    private fun loadContacts() {
        val contacts = mutableListOf<Contact>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        contentResolver.query(uri, projection, null, null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )?.use { cursor ->
            val nameIdx   = cursor.getColumnIndex(projection[0])
            val numberIdx = cursor.getColumnIndex(projection[1])
            while (cursor.moveToNext()) {
                contacts.add(
                    Contact(
                        name  = cursor.getString(nameIdx),
                        phone = cursor.getString(numberIdx)
                    )
                )
            }
        }

        val sections = contacts
            .groupBy { it.name.firstOrNull()?.uppercaseChar() ?: '#' }
            .toSortedMap()
            .map { (letter, list) ->
                SectionModel(letter, list.sortedBy { it.name }, expanded = true)
            }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        sectionAdapter = SectionedExpandableAdapter(sections) { contact ->
            startActivity(
                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
            )
        }
        recyclerView.adapter = sectionAdapter

        val headerSpacing = resources.getDimensionPixelSize(R.dimen.section_header_spacing)
        val itemSpacing   = resources.getDimensionPixelSize(R.dimen.item_spacing)

        recyclerView.apply {
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL)
            )
            addItemDecoration(
                SectionSpacingDecoration(headerSpacing, itemSpacing)
            )
        }
    }
}
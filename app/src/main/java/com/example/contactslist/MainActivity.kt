package com.example.contactslist

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.contactslist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = ContactsAdapter()
    private lateinit var contactsList: ArrayList<ContactModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //
        requestPermission()
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 111)
        } else {
            readContact()
        }
    }
    private fun readContact() {
        contactsList = ArrayList<ContactModel>()
        val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        if (cursor?.count!! > 0) {
            var index = 0
            while (cursor.moveToNext()) {
                index++
                val id = index.toString()
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val mobileNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add(ContactModel(id, name, mobileNumber))
            }
        }
        cursor.close()
        binding.rcView.adapter = adapter
        adapter.submitList(contactsList)
        setupSearchViewListener()
    }
    private fun setupSearchViewListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == "") {
                    adapter.submitList(contactsList)
                } else {
                    val searchList = ArrayList<ContactModel>()
                    contactsList.forEach {
                        if (it.name.contains(newText!!)) {
                            searchList.add(it)
                        }
                    }
                    adapter.submitList(searchList)
                }
                return false
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Alert")
                .setMessage("You need to accept the permission to use this app.")
                .setPositiveButton("Ok") { dialog, someInt ->
                    dialog.cancel()
                    finish()
                }
                .create()
                .show()
        } else {
            readContact()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
package com.example.contactslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contactslist.databinding.ItemLayoutBinding

class ContactsAdapter : ListAdapter<ContactModel, ContactsAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemLayoutBinding.bind(view)
        fun bind(item: ContactModel) {
            binding.tvId.text = item.id
            binding.tvName.text = item.name
            binding.tvPhone.text = item.mobileNumber
        }
    }
    class Comparator : DiffUtil.ItemCallback<ContactModel>() {
        override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}
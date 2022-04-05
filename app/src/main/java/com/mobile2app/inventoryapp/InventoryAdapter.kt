package com.mobile2app.inventoryapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

class InventoryAdapter(private val dataset: List<InventoryItem>) :
        RecyclerView.Adapter<InventoryAdapter.ViewHolder>()
{

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.textview_itemCard_title)
        val category: TextView = view.findViewById(R.id.textview_itemCard_category)
        val description: TextView = view.findViewById(R.id.textview_itemCard_description)
        val count: TextView = view.findViewById(R.id.textview_itemCard_count)
        val countIncrease: ImageButton = view.findViewById(R.id.imageButton_itemCard_countIncrease)
        val countDecrease: ImageButton = view.findViewById(R.id.imageButton_itemCard_countDecrease)
        val delete: MaterialButton = view.findViewById(R.id.button_itemCard_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = dataset[position].item
        holder.category.text = dataset[position].category
        holder.description.text = dataset[position].description
        holder.count.text = dataset[position].quantity.toString()

        holder.countIncrease.setOnClickListener {
            // This... Is arguably an aggressive approach to handling click events, as it bypasses
            // the ViewModel entirely. However, It's more elegant than blasting updates through
            // GlobalScope, and I'm clinging to the carriage at this point, so I'll take it.
            val context = (holder.itemView.context.applicationContext as InventoryApplication)
            context.applicationScope.launch {
                dataset[position].quantity++
                context.inventoryRepository.updateItem(dataset[position])
            }
        }

        holder.countDecrease.setOnClickListener {
            val context = (holder.itemView.context.applicationContext as InventoryApplication)
            context.applicationScope.launch {
                dataset[position].quantity--
                context.inventoryRepository.updateItem(dataset[position])
            }
        }

        holder.delete.setOnClickListener {
            val context = (holder.itemView.context.applicationContext as InventoryApplication)
            context.applicationScope.launch {
                context.inventoryRepository.deleteItem(dataset[position])
            }
        }
    }

    override fun getItemCount(): Int = dataset.size

}
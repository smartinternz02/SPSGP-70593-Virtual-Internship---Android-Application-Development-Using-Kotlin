package com.example.groceryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroceryRVAdapter(
    var list: List<GroceryItems>,
    val groceryItemClickInterface: GroceryItemClickInterface
) : RecyclerView.Adapter<GroceryRVAdapter.GroceryViewHolder>() {

    inner class GroceryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameTv = itemView.findViewById<TextView>(R.id.idTVItemName)
        val quantityTv = itemView.findViewById<TextView>(R.id.idTVQuantity)
        val rateTv = itemView.findViewById<TextView>(R.id.idTVRate)
        val amountTv = itemView.findViewById<TextView>(R.id.idTVTotalAmt)
        val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)

    }

    interface GroceryItemClickInterface {
        fun onItemClick(groceryItems: GroceryItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grocery_rv_item, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        holder.nameTv.text = list.get(position).itemName
        holder.quantityTv.text = list.get(position).itemQuantity.toString()
        holder.rateTv.text = "RS. " + list.get(position).itemPrice.toString()

        val itemTotal : Int = list.get(position).itemPrice * list.get(position).itemQuantity
        holder.amountTv.text = "RS. " + itemTotal.toString()

        holder.deleteIV.setOnClickListener{
            groceryItemClickInterface.onItemClick(list.get(position))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
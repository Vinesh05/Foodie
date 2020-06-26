package com.vinesh.foodie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.vinesh.foodie.Activity.RestaurantMenu
import com.vinesh.foodie.Activity.orderList
import com.vinesh.foodie.R
import com.vinesh.foodie.model.FoodItem

class FoodItemAdapter(val context: Context,val itemList: ArrayList<FoodItem>): RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {

    class FoodItemViewHolder (view: View): RecyclerView.ViewHolder(view){
        val foodItemName: TextView = view.findViewById(R.id.txtFoodItemName)
        val foodItemCost: TextView = view.findViewById(R.id.txtFoodItemCost)
        val imgAdd: ImageView = view.findViewById(R.id.imgAdd)
        val imgRemove: ImageView = view.findViewById(R.id.imgRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item_recycler_single_row,parent,false)
        return FoodItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val foodItem = itemList[position]
        holder.foodItemName.text = foodItem.itemName
        holder.foodItemCost.text = foodItem.itemCostForOne
        holder.imgRemove.visibility = View.GONE

        holder.imgAdd.setOnClickListener {

            holder.imgAdd.visibility = View.GONE
            holder.imgRemove.visibility = View.VISIBLE
            RestaurantMenu().onAddItemClick(foodItem)
        }

        holder.imgRemove.setOnClickListener {

            holder.imgRemove.visibility = View.GONE
            holder.imgAdd.visibility = View.VISIBLE
            RestaurantMenu().onRemoveItemClick(foodItem)

        }

    }

}
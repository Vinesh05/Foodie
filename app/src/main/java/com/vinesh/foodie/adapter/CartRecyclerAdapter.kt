package com.vinesh.foodie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinesh.foodie.R
import com.vinesh.foodie.model.FoodItem

class CartRecyclerAdapter(val context: Context, val orderList: ArrayList<FoodItem>): RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){

        val itemName: TextView = view.findViewById(R.id.cartRecyclerFoodItem)
        val itemprice: TextView = view.findViewById(R.id.cartRecyclerPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_recycler_single_row,parent,false)

        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val foodItem = orderList[position]
        holder.itemName.text = foodItem.itemName
        holder.itemprice.text = "Rs.${foodItem.itemCostForOne}"
    }

}
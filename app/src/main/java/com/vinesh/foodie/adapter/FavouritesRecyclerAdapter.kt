package com.vinesh.foodie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinesh.foodie.Activity.RestaurantMenu
import com.vinesh.foodie.R
import com.vinesh.foodie.databases.FavEntitiy

class FavouritesRecyclerAdapter(val context:Context,val favList: List<FavEntitiy>): RecyclerView.Adapter<FavouritesRecyclerAdapter.favouritesviewHolder>() {

    class favouritesviewHolder(view:View): RecyclerView.ViewHolder(view){
        val restaurantName: TextView = view.findViewById(R.id.txtResName)
        val restaurantPrice: TextView = view.findViewById(R.id.txtResPrice)
        val restaurantRating: TextView = view.findViewById(R.id.txtResRatings)
        val restaurantImage: ImageView = view.findViewById(R.id.imgRestaurant)
        val llContent: LinearLayout = view.findViewById(R.id.llResContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favouritesviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurants_recycler_single_row,parent,false)

        return FavouritesRecyclerAdapter.favouritesviewHolder(view)
    }

    override fun getItemCount(): Int {
        return favList.size
    }

    override fun onBindViewHolder(holder: favouritesviewHolder, position: Int) {

        val restaurant = favList[position]
        holder.restaurantName.text = restaurant.resName
        holder.restaurantPrice.text = restaurant.resCost
        holder.restaurantRating.text = restaurant.resRating
        Picasso.get().load(restaurant.resImage).error(R.drawable.error_image).into(holder.restaurantImage)

        holder.llContent.setOnClickListener {
            val resId = restaurant.res_id
            val resName = restaurant.resName
            val intent = Intent(context, RestaurantMenu::class.java)
            intent.putExtra("id",resId)
            intent.putExtra("name",resName)
            context.startActivity(intent)
        }

    }

}
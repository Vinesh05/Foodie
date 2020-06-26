package com.vinesh.foodie.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinesh.foodie.Activity.RestaurantMenu
import com.vinesh.foodie.R
import com.vinesh.foodie.databases.FavEntitiy
import com.vinesh.foodie.fragments.HomeFragment
import com.vinesh.foodie.model.Restaurant

class RestaurantsRecyclerAdapter(val context: Context, private val itemList: ArrayList<Restaurant>): RecyclerView.Adapter<RestaurantsRecyclerAdapter.RestaurantsViewHolder> () {

    class RestaurantsViewHolder(view:View): RecyclerView.ViewHolder(view){
        val restaurantName: TextView = view.findViewById(R.id.txtResName)
        val restaurantPrice: TextView = view.findViewById(R.id.txtResPrice)
        val restaurantRating: TextView = view.findViewById(R.id.txtResRatings)
        val restaurantImage: ImageView = view.findViewById(R.id.imgRestaurant)
        val resFavIcon: ImageView = view.findViewById(R.id.imgResFavIcon)
        val llContent: LinearLayout = view.findViewById(R.id.llResContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurants_recycler_single_row,parent,false)
        return RestaurantsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantPrice.text = restaurant.restaurantCostForOne
        holder.restaurantRating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.error_image).into(holder.restaurantImage)

        holder.llContent.setOnClickListener {
            val resId = restaurant.restaurantId
            val resName = restaurant.restaurantName
            val intent = Intent(context, RestaurantMenu::class.java)
            intent.putExtra("id",resId)
            intent.putExtra("name",resName)
            context.startActivity(intent)
        }

        val favEntitiy = FavEntitiy(
            restaurant.restaurantId,
            restaurant.restaurantName,
            restaurant.restaurantRating,
            restaurant.restaurantCostForOne,
            restaurant.restaurantImage
        )

        val checkFav = HomeFragment.DbAsyncTask(context, favEntitiy, 1).execute()
        val isFav = checkFav.get()

        if(isFav){
            holder.resFavIcon.setImageResource(R.drawable.favourite_menu)
        }
        else{
            holder.resFavIcon.setImageResource(R.drawable.ic_no_fav)
        }

        holder.resFavIcon.setOnClickListener {

            if(HomeFragment.DbAsyncTask(context, favEntitiy, 1).execute().get()){

                val addFav = HomeFragment.DbAsyncTask(context,favEntitiy,3).execute()
                val result = addFav.get()

                if(result){
                    Toast.makeText(context,"Restaurant Added to Favourites",Toast.LENGTH_SHORT).show()
                    holder.resFavIcon.setImageResource(R.drawable.ic_no_fav)
                }
                else{
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }

            }
            else{
                val addFav = HomeFragment.DbAsyncTask(context,favEntitiy,2).execute()
                val result = addFav.get()

                if(result){
                    Toast.makeText(context,"Restaurant Removed to Favourites",Toast.LENGTH_SHORT).show()
                    holder.resFavIcon.setImageResource(R.drawable.favourite_menu)
                }
                else{
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
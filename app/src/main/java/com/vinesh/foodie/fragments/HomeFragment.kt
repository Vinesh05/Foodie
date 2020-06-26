package com.vinesh.foodie.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color.red
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vinesh.foodie.Activity.CartActivity
import com.vinesh.foodie.Activity.MainActivity
import com.vinesh.foodie.R
import com.vinesh.foodie.adapter.RestaurantsRecyclerAdapter
import com.vinesh.foodie.databases.FavDatabase
import com.vinesh.foodie.databases.FavEntitiy
import com.vinesh.foodie.model.Restaurant
import com.vinesh.foodie.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class HomeFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: RestaurantsRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val restaurantList= arrayListOf<Restaurant>()

    var ratingComparator = Comparator<Restaurant>{Restaurant1,Restaurant2 ->
        if(Restaurant1.restaurantRating.compareTo(Restaurant2.restaurantRating,true)==0){

            Restaurant1.restaurantName.compareTo(Restaurant2.restaurantName,true)

        }
        else{
            Restaurant1.restaurantRating.compareTo(Restaurant2.restaurantRating,true)
        }
    }

    var costComparator = Comparator<Restaurant>{Restaurant1,Restaurant2 ->
        if(Restaurant1.restaurantRating.compareTo(Restaurant2.restaurantRating,true)==0){

            Restaurant1.restaurantName.compareTo(Restaurant2.restaurantName,true)

        }
        else{
            Restaurant1.restaurantCostForOne.compareTo(Restaurant2.restaurantCostForOne,true)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerRes)
        layoutManager = LinearLayoutManager(activity)
        progressLayout = view.findViewById(R.id.homeProgressLayout)
        progressBar = view.findViewById(R.id.homeProgressBar)

        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        setHasOptionsMenu(true)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
        if(ConnectionManager().checkConnectivity(activity as Context)){

                val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                    try{
                        progressLayout.visibility = View.GONE
                        progressBar.visibility = View.GONE

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if(success){
                            val RestaurantArray = data.getJSONArray("data")
                            for(i in 0 until RestaurantArray.length()){
                                val restaurantJsonObject = RestaurantArray.getJSONObject(i)
                                val restaurantObject = Restaurant(
                                    restaurantJsonObject.getString("id"),
                                    restaurantJsonObject.getString("name"),
                                    restaurantJsonObject.getString("rating"),
                                    restaurantJsonObject.getString("cost_for_one"),
                                    restaurantJsonObject.getString("image_url")
                                )
                                restaurantList.add(restaurantObject)

                            }
//                            if (activity!=null){
                                recyclerAdapter = RestaurantsRecyclerAdapter(activity as Context,restaurantList)
//                            }
//                            else{
//                                recyclerAdapter = RestaurantsRecyclerAdapter(CartActivity().activityContext as Context,restaurantList)
//                            }

                            recyclerView.layoutManager = layoutManager
                            recyclerView.adapter = recyclerAdapter
                        }
                        else{
                            if(activity!=null){
                                Toast.makeText(activity?.applicationContext,"Some unexpected Error Occurred",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch (e: JSONException){
                        Toast.makeText(activity as Context,"Some Error Occurred Please try again",Toast.LENGTH_SHORT).show()
                    }


            },Response.ErrorListener {

                Toast.makeText(activity as Context,"Some Error Occurred Pleast try again Later",Toast.LENGTH_SHORT).show()

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "058cab00f48bff"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)
        }
        else{
            val alertDialog = AlertDialog.Builder(activity as Context,R.style.MyDialogTheme)
            alertDialog.setTitle("Error")
            alertDialog.setMessage("Internet Connection Not Found")
            alertDialog.setPositiveButton("Open Settings"){ textColor, listener ->

                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()

            }
            alertDialog.setNegativeButton("Exit"){text,listener ->

                ActivityCompat.finishAffinity(activity as Activity)

            }
            alertDialog.create()
            alertDialog.show()
        }

        return view
    }

    class DbAsyncTask(val context: Context,val favEntity: FavEntitiy,val mode: Int): AsyncTask<Void,Void,Boolean>(){

        private val db= Room.databaseBuilder(context,FavDatabase::class.java,"favourites_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1->{
                    val favEntitiy: FavEntitiy? = db.favDao().getFavouriteById(favEntity.res_id)
                    db.close()

                    return favEntitiy != null
                }
                2->{
                    db.favDao().insertFavourite(favEntity)
                    db.close()

                    return true
                }
                3->{
                    db.favDao().deleteFavourite(favEntity)
                    db.close()

                    return true
                }
            }

            return false
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sort,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.sortCostLowToHigh->{
                Collections.sort(restaurantList,costComparator)
            }
            R.id.sortCostHighToLow->{
                Collections.sort(restaurantList,costComparator)
                restaurantList.reverse()
            }

            R.id.sortRatingLowToHigh->{
                Collections.sort(restaurantList,ratingComparator)
                restaurantList.reverse()
            }
            R.id.sortRatingHighToLow->{
                Collections.sort(restaurantList,ratingComparator)
            }
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        recyclerView.adapter = null
        super.onDestroy()
    }

}
package com.vinesh.foodie.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.vinesh.foodie.R
import com.vinesh.foodie.adapter.FoodItemAdapter
import com.vinesh.foodie.model.FoodItem
import com.vinesh.foodie.util.ConnectionManager
import org.json.JSONException

lateinit var resToolbar:Toolbar
lateinit var progressLayout: RelativeLayout
lateinit var progressBar: ProgressBar
lateinit var btnResMenu: Button

var ResId: String? = "0"
var ResName: String? = ""
val orderList = ArrayList<FoodItem>()

class RestaurantMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        resToolbar = findViewById(R.id.ResMenuToolbar)
        btnResMenu = findViewById(R.id.btnResMenu)
        progressLayout = findViewById(R.id.ResMenuProgressLayout)
        progressBar = findViewById(R.id.ResMenuProgressBar)
        btnResMenu.visibility = View.GONE
        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        val resMenuRecyclerView: RecyclerView = findViewById(R.id.ResMenuRecyclerView)
        val resMenuLayoutManager = LinearLayoutManager(this@RestaurantMenu)

        val foodItemList = ArrayList<FoodItem>()

        if(intent!=null){
            ResId = intent.getStringExtra("id")
            ResName = intent.getStringExtra("name")
        }
        else if(ResId=="0" || ResName == ""){
            finish()
            Toast.makeText(this@RestaurantMenu,"Some Error Occurred",Toast.LENGTH_SHORT).show()
        }
        else{
            finish()
            Toast.makeText(this@RestaurantMenu,"Some Error Occurred",Toast.LENGTH_SHORT).show()
        }

        setUpToolbar()

        val queue = Volley.newRequestQueue(this@RestaurantMenu)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$ResId"

        if(ConnectionManager().checkConnectivity(this@RestaurantMenu as Context)){

            val jsonObjectRequest = object: JsonObjectRequest(Method.GET,url,null, Listener{

                try {
                    progressLayout.visibility = View.GONE
                    progressBar.visibility = View.GONE

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if(success){
                        val foodItemArray = data.getJSONArray("data")
                        for(i in 0 until foodItemArray.length()){
                            val foodItemObject = foodItemArray.getJSONObject(i)
                            val foodItem = FoodItem(
                                foodItemObject.getString("id"),
                                foodItemObject.getString("name"),
                                foodItemObject.getString("cost_for_one")
                            )
                            foodItemList.add(foodItem)
                        }
                        val resMenuAdapter = FoodItemAdapter(this@RestaurantMenu as Context,foodItemList)
                        resMenuRecyclerView.layoutManager = resMenuLayoutManager
                        resMenuRecyclerView.adapter = resMenuAdapter
                    }
                    else{
                        Toast.makeText(this@RestaurantMenu,"Some Unexpected Error Occurred",Toast.LENGTH_SHORT).show()
                    }
                }catch(e: JSONException){
                    Toast.makeText(this@RestaurantMenu,"Some Unexpected Error Occurred",Toast.LENGTH_SHORT).show()
                }

            },Response.ErrorListener {

                Toast.makeText(this@RestaurantMenu,"Some Unexpected Error Occurred",Toast.LENGTH_SHORT).show()

            }){
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Content-Type"] = "application/json"
                    headers["token"] = "058cab00f48bff"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        }
        else{
            val dialog = AlertDialog.Builder(this@RestaurantMenu,R.style.MyDialogTheme)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection")
            dialog.setPositiveButton("Open Settings"){text,listener->
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
                finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(this@RestaurantMenu)
            }
            dialog.create()
            dialog.show()
        }

        btnResMenu.setOnClickListener {


            val gson = Gson()

            val foodItems = gson.toJson(orderList)

                val data = Bundle()
                data.putString("res_id", ResId)
                data.putString("res_name", ResName)
                data.putString("food_items",foodItems)
                val cartIntent = Intent(this@RestaurantMenu,CartActivity::class.java)
                cartIntent.putExtra("data",data)
                startActivity(cartIntent)

        }

    }

    fun onAddItemClick(foodItem: FoodItem){
        orderList.add(foodItem)
        if(orderList.size>0){
            btnResMenu.visibility = View.VISIBLE
        }
    }
    fun onRemoveItemClick(foodItem: FoodItem){
        orderList.remove(foodItem)
        if(orderList.isEmpty()){
            btnResMenu.visibility = View.GONE
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(resToolbar)
        supportActionBar?.title = ResName
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId == android.R.id.home){
            super.onBackPressed()
            return true
        }
        return onOptionsItemSelected(item)
    }

}
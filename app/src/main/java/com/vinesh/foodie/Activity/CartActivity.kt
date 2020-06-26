package com.vinesh.foodie.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.vinesh.foodie.R
import com.vinesh.foodie.adapter.CartRecyclerAdapter
import com.vinesh.foodie.fragments.OrderPlaced
import com.vinesh.foodie.model.FoodItem
import com.vinesh.foodie.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    lateinit var cartToolbar: Toolbar
    lateinit var cartFrame: FrameLayout
    lateinit var cartResName: TextView
    lateinit var recyclerCart: RecyclerView
    lateinit var btnCartPlaceOrder: Button
    lateinit var cartAdapter: CartRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        cartToolbar = findViewById(R.id.cartToolbar)
        cartFrame = findViewById(R.id.cartFrame)
        cartResName = findViewById(R.id.cartResName)
        recyclerCart = findViewById(R.id.recyclerCart)
        btnCartPlaceOrder = findViewById(R.id.btnCartPlaceOrder)

        setUpToolbar()
        val orderList = ArrayList<FoodItem>()

        val userId = sharedPreferences.getString("user_id","")
        val data = intent.getBundleExtra("data")
        val resId = data?.getString("res_id")
        val resName = data?.getString("res_name")
        cartResName.text = resName
        val foodItems = data?.getString("food_items")

        orderList.addAll(
            Gson().fromJson(foodItems,Array<FoodItem>::class.java).asList()
        )

        val cartLayoutManager = LinearLayoutManager(this@CartActivity)
        cartAdapter = CartRecyclerAdapter(this@CartActivity,orderList)
        recyclerCart.layoutManager = cartLayoutManager
        recyclerCart.adapter = cartAdapter

        val foodArray = JSONArray()
        var total = 0
        for(i in 0 until orderList.size){

            val foodIdObject = JSONObject()
            foodIdObject.put("food_item_id",orderList[i].itemId)
            foodArray.put(i,foodIdObject)
            total += orderList[i].itemCostForOne.toInt()

        }

        btnCartPlaceOrder.text = "Place Order (Rs.${total})"

        val cartQueue = Volley.newRequestQueue(this@CartActivity)
        val cartUrl = "http://13.235.250.119/v2/place_order/fetch_result/"

        btnCartPlaceOrder.setOnClickListener {

            if(ConnectionManager().checkConnectivity(this@CartActivity)){

                val cartJsonObject = JSONObject()
                cartJsonObject.put("user_id",userId)
                cartJsonObject.put("restaurant_id",resId)
                cartJsonObject.put("total_cost",total)
                cartJsonObject.put("food",foodArray)

                val cartJsonObjectRequest = object: JsonObjectRequest(Method.POST,cartUrl,cartJsonObject,Response.Listener {

                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")

                    if(success){

                        supportFragmentManager.beginTransaction()
                            .replace(R.id.cartFrame,OrderPlaced())
                            .commit()

                    }
                    else{
                        Toast.makeText(this@CartActivity,data.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                    }


                },Response.ErrorListener {

                    Toast.makeText(this@CartActivity,"Some unexpected Error Occurred",Toast.LENGTH_SHORT).show()

                }){

                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String,String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "058cab00f48bff"
                        return headers

                    }

                }

                cartQueue.add(cartJsonObjectRequest)

            }
            else{
                val dialog = AlertDialog.Builder(this@CartActivity,R.style.MyDialogTheme)
                dialog.setTitle("Error")
                dialog.setMessage("No Internet Connection")
                dialog.setPositiveButton("Open Settings"){text,listener->
                    val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(intent)
                    finish()
                }
                dialog.setNegativeButton("Exit"){text,listener->
                    ActivityCompat.finishAffinity(this@CartActivity)
                }
                dialog.create()
                dialog.show()
            }

        }

    }

    private fun setUpToolbar(){
        setSupportActionBar(cartToolbar)
        supportActionBar?.title = "Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.cartFrame)

        when(frag){

            is OrderPlaced -> {
                val intent = Intent(this@CartActivity,MainActivity::class.java)
                startActivity(intent)
            }

            else -> super.onBackPressed()

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return super.onOptionsItemSelected(item)
    }

}
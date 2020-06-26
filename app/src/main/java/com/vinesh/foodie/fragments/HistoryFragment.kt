package com.vinesh.foodie.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vinesh.foodie.Activity.progressBar
import com.vinesh.foodie.Activity.progressLayout
import com.vinesh.foodie.R
import com.vinesh.foodie.adapter.HistoryOrderAdapter
import com.vinesh.foodie.model.FoodItem
import com.vinesh.foodie.model.Order
import com.vinesh.foodie.util.ConnectionManager
import org.json.JSONException

class HistoryFragment : Fragment() {

    lateinit var orderRecycler: RecyclerView
    lateinit var orderlayoutManager: LinearLayoutManager
    lateinit var histNoOrders: TextView
    lateinit var histProgressLayout: RelativeLayout
    lateinit var histProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val sharedPreferences = activity?.getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        val userId = sharedPreferences?.getString("user_id",null)

        orderRecycler = view.findViewById(R.id.recyclerHist)
        histNoOrders = view.findViewById(R.id.histNoOrders)
        histProgressLayout = view.findViewById(R.id.histProgressLayout)
        histProgressBar = view.findViewById(R.id.histProgressBar)
        histNoOrders.visibility = View.GONE
        histProgressLayout.visibility = View.VISIBLE
        histProgressBar.visibility = View.VISIBLE

        orderlayoutManager = LinearLayoutManager(activity as Context)

        if(userId!=null){
            val histQueue = Volley.newRequestQueue(activity as Context)
            val histUrl = "http://13.235.250.119/v2/orders/fetch_result/$userId"

            if(ConnectionManager().checkConnectivity(activity as Context)){
                val histJsonObjectRequest = object: JsonObjectRequest(Method.GET,histUrl,null,Response.Listener {

                    try{
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if(success){

                            val dataArray = data.getJSONArray("data")
                            val orderList = ArrayList<Order>()

                            for(i in 0 until dataArray.length()){

                                val dataObject = dataArray.getJSONObject(i)
                                val foodItemArray = dataObject.getJSONArray("food_items")

                                val orderObject = Order(
                                    dataObject.getString("order_id"),
                                    dataObject.getString("restaurant_name"),
                                    dataObject.getString("order_placed_at"),
                                    foodItemArray,
                                    dataObject.getString("total_cost")
                                )
                                orderList.add(orderObject)

                            }
                            orderRecycler.adapter = HistoryOrderAdapter(activity as Context,orderList)
                            orderRecycler.layoutManager = orderlayoutManager

                            if(orderList.isEmpty()){
                                histNoOrders.visibility = View.VISIBLE
                            }
                            histProgressLayout.visibility = View.GONE
                            histProgressBar.visibility = View.GONE

                        }
                        else{
                            if(activity!=null){
                                Toast.makeText(activity as Context,data.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch(e: JSONException){
                        if(activity!=null){
                            Toast.makeText(activity as Context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                        }
                    }

                },Response.ErrorListener {
                    if(activity!=null){
                        Toast.makeText(activity as Context,"Some unexpected Error Occurred",Toast.LENGTH_SHORT).show()
                    }
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "058cab00f48bff"
                        return headers
                    }
                }
                histQueue.add(histJsonObjectRequest)
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
        }
        else{
            if(activity!=null){
                Toast.makeText(activity as Context,"Please restart or reinstall the app",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}
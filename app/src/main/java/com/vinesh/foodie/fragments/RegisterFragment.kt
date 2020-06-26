package com.vinesh.foodie.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vinesh.foodie.Activity.MainActivity
import com.vinesh.foodie.R
import com.vinesh.foodie.util.ConnectionManager
import org.json.JSONObject

class RegisterFragment : Fragment() {

    lateinit var etRegName: EditText
    lateinit var etRegEmailAddress: EditText
    lateinit var etRegMobileNumber: EditText
    lateinit var etRegAdress: EditText
    lateinit var etRegPassword: EditText
    lateinit var etRegConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var regProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_register, container, false)

        val sharedPreferences = activity?.getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        etRegName = view.findViewById(R.id.etRegName)
        etRegEmailAddress = view.findViewById(R.id.etRegEmailAddress)
        etRegMobileNumber = view.findViewById(R.id.etRegMobileNumber)
        etRegAdress = view.findViewById(R.id.etRegAddress)
        etRegPassword = view.findViewById(R.id.etRegConfirmPassword)
        etRegConfirmPassword = view.findViewById(R.id.etRegConfirmPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        regProgressBar = view.findViewById(R.id.regProgressBar)
        regProgressBar.visibility = View.GONE

        val regQueue = Volley.newRequestQueue(activity as Context)
        val regurl = "http://13.235.250.119/v2/register/fetch_result"

        btnRegister.setOnClickListener {

            regProgressBar.visibility = View.VISIBLE

            if(ConnectionManager().checkConnectivity(activity as Context)){
                val name = etRegName.text.toString()
                val mobileNumber = etRegMobileNumber.text.toString()
                val password = etRegPassword.text.trim().toString()
                val address = etRegAdress.text.toString()
                val emailAddress = etRegEmailAddress.text.trim().toString()

                val regJsonParams = JSONObject()
                regJsonParams.put("name",name)
                regJsonParams.put("mobile_number",mobileNumber)
                regJsonParams.put("password",password)
                regJsonParams.put("address",address)
                regJsonParams.put("email",emailAddress)

                val regJsonObjectRequest = object: JsonObjectRequest(Method.POST,regurl,regJsonParams,Response.Listener {

                    val dataObject = it.getJSONObject("data")
                    val success = dataObject.getBoolean("success")
                    if(success){

                        val data = dataObject.getJSONObject("data")

                        sharedPreferences?.edit()?.putString("user_id",data.getString("user_id"))?.apply()
                        sharedPreferences?.edit()?.putString("user_name",data.getString("name"))?.apply()
                        sharedPreferences?.edit()?.putString("user_email",data.getString("email"))?.apply()
                        sharedPreferences?.edit()?.putString("user_number",data.getString("mobile_number"))?.apply()
                        sharedPreferences?.edit()?.putString("user_address",data.getString("address"))?.apply()
                        sharedPreferences?.edit()?.putBoolean("Logged_in",true)?.apply()

                        regProgressBar.visibility = View.GONE

                        val intent = Intent(activity as Context,MainActivity::class.java)
                        startActivity(intent)

                    }
                    else{
                        regProgressBar.visibility = View.GONE

                        Toast.makeText(activity as Context,dataObject.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                    }

                },Response.ErrorListener{
                    regProgressBar.visibility = View.GONE

                    Toast.makeText(activity as Context,"Some unexpected Error Occurred",Toast.LENGTH_SHORT).show()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "058cab00f48bff"
                        return headers
                    }
                }
                regQueue.add(regJsonObjectRequest)

            }
            else{
                regProgressBar.visibility = View.GONE

                Toast.makeText(activity as Context,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}
package com.vinesh.foodie.fragments

import android.widget.Toast
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vinesh.foodie.R
import com.vinesh.foodie.util.ConnectionManager
import org.json.JSONObject

class ForgotPassword : Fragment() {

    lateinit var etForgotMobileNumber: EditText
    lateinit var etForgotEmailAddress: EditText
    lateinit var btnForgotProceed: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_forgot_password, container, false)

        etForgotMobileNumber = view.findViewById(R.id.etForgotMobileNumber)
        etForgotEmailAddress = view.findViewById(R.id.etForgotEmailAddress)
        btnForgotProceed = view.findViewById(R.id.btnForgotProceed)

        val ForgotQueue = Volley.newRequestQueue(activity as Context)
        val Forgoturl = "http://13.235.250.119/v2/forgot_password/fetch_result"

        btnForgotProceed.setOnClickListener {
            val ForgotMobileNumber = etForgotMobileNumber.text
            val ForgotEmailAddress = etForgotEmailAddress.text

            val ForgotJsonParams = JSONObject()
            ForgotJsonParams.put("mobile_number",ForgotMobileNumber)
            ForgotJsonParams.put("email",ForgotEmailAddress)

            if(ConnectionManager().checkConnectivity(activity as Context)){
                val forgotJsonObjectRequest = object : JsonObjectRequest(Method.POST, Forgoturl, ForgotJsonParams,Response.Listener {

                    println("Response is $it")
                    val forgotDataObject = it.getJSONObject("data")
                    val success = forgotDataObject.getBoolean("success")
                    if(success){

                        val otpFragment = Otp()
                        otpFragment.arguments = Bundle().apply {
                            putString("mobile_number",ForgotMobileNumber.toString())
                        }

                        fragmentManager?.beginTransaction()
                            ?.replace(R.id.loginFrame,otpFragment)
                            ?.commit()
                    }
                    else{
                        Toast.makeText(activity as Context, forgotDataObject.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                    }

                },Response.ErrorListener {
                    Toast.makeText(activity as Context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String>()
                        headers["Content-Type"] = "applicaiton/json"
                        headers["token"] = "058cab00f48bff"
                        return headers
                    }
                }
                ForgotQueue.add(forgotJsonObjectRequest)
            }
            else{
                Toast.makeText(activity as Context,"No internet Connection",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}
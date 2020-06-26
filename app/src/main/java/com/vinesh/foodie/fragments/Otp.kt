package com.vinesh.foodie.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.vinesh.foodie.Activity.LoginActivity
import com.vinesh.foodie.R
import com.vinesh.foodie.util.ConnectionManager
import okhttp3.Response
import org.json.JSONObject

class Otp : Fragment() {

    lateinit var etForgotOTP: EditText
    lateinit var etForgotNewPassword: EditText
    lateinit var etForgotConfirmPassword: EditText
    lateinit var btnForgotOtpSubmit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_otp, container, false)

        etForgotOTP = view.findViewById(R.id.etForgotOTP)
        etForgotNewPassword = view.findViewById(R.id.etForgotNewPassword)
        etForgotConfirmPassword = view.findViewById(R.id.etForgotConfirmPassword)
        btnForgotOtpSubmit = view.findViewById(R.id.btnOtpSubmit)

        val bundle = this.arguments
        if(bundle!=null){

            val otpQueue = Volley.newRequestQueue(activity as Context)
            val otpurl = "http://13.235.250.119/v2/reset_password/fetch_result"


            btnForgotOtpSubmit.setOnClickListener {

                val otp = etForgotOTP.text
                val newPassword = etForgotNewPassword.text.trim().toString()
                val confirmPassword = etForgotConfirmPassword.text.trim().toString()

                val otpJsonParams = JSONObject()
                otpJsonParams.put("mobile_number",bundle.getString("mobile_number"))
                otpJsonParams.put("password",newPassword)
                otpJsonParams.put("otp",otp)

                if(newPassword == confirmPassword){

                    if(ConnectionManager().checkConnectivity(activity as Context)){
                        val otpJsonObjectRequest = object: JsonObjectRequest(Method.POST,otpurl,otpJsonParams,com.android.volley.Response.Listener {

                            println(otpJsonParams)
                            println("Response is $it")
                            val dataObject = it.getJSONObject("data")
                            val success = dataObject.getBoolean("success")
                            if(success){

                                Toast.makeText(activity as Context,dataObject.getString("successMessage"),Toast.LENGTH_SHORT).show()

                                val intent = Intent(activity as Context,LoginActivity::class.java)
                                startActivity(intent)

                            }
                            else{
                                Toast.makeText(activity as Context,dataObject.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                            }

                        },com.android.volley.Response.ErrorListener {

                        }){
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String,String>()
                                headers["Content-Type"] = "application/json"
                                headers["token"] = "058cab00f48bff"
                                return headers
                            }
                        }
                        otpQueue.add(otpJsonObjectRequest)
                    }
                }
                else{
                    Toast.makeText(activity as Context,"Confirm Password doesn't match",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(activity as Context,"Some unexpected Error Occurred",Toast.LENGTH_SHORT).show()
            activity?.finish()
        }

        return view
    }

}
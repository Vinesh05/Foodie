package com.vinesh.foodie.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vinesh.foodie.R
import java.lang.ref.WeakReference

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPreferences = activity?.getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)
        val txtProfileName:TextView = view.findViewById(R.id.txtProfileName)
        val txtProfileMobileNumber:TextView = view.findViewById(R.id.txtProfileMobileNumber)
        val txtProfileEmailAddress:TextView = view.findViewById(R.id.txtProfileEmailAddress)
        val txtProfileAddress: TextView = view.findViewById(R.id.txtProfileAddress)

        txtProfileName.text = sharedPreferences?.getString("user_name","Name")
        txtProfileMobileNumber.text = sharedPreferences?.getString("user_number","Mobile Number")
        txtProfileEmailAddress.text= sharedPreferences?.getString("user_email","Email")
        txtProfileAddress.text= sharedPreferences?.getString("user_address","Address")

        return view
    }

}
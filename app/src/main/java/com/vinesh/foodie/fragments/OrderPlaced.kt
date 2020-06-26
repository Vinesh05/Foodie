package com.vinesh.foodie.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.vinesh.foodie.Activity.MainActivity
import com.vinesh.foodie.R

class OrderPlaced : Fragment() {

    lateinit var btnCartOk: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_order_placed, container, false)

        btnCartOk = view.findViewById(R.id.btnCartOk)

        btnCartOk.setOnClickListener {

            val intent = Intent(activity as Context,MainActivity::class.java)
            startActivity(intent)

        }

        return view
    }

}
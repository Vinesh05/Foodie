package com.vinesh.foodie.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.vinesh.foodie.R

class FaqFragment : Fragment() {

    lateinit var txtFaqTrainingLink: TextView
    lateinit var txtFaqGithubRepo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_faq, container, false)

        txtFaqTrainingLink = view.findViewById(R.id.txtFaqA3)
        txtFaqGithubRepo = view.findViewById(R.id.txtFaqA4)

        txtFaqTrainingLink.movementMethod = LinkMovementMethod.getInstance()
        txtFaqTrainingLink.setLinkTextColor(Color.BLUE)

        txtFaqGithubRepo.movementMethod = LinkMovementMethod.getInstance()
        txtFaqGithubRepo.setLinkTextColor(Color.BLUE)


        return view
    }

}
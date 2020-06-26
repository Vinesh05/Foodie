package com.vinesh.foodie.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.vinesh.foodie.R
import com.vinesh.foodie.adapter.FavouritesRecyclerAdapter
import com.vinesh.foodie.databases.FavDatabase
import com.vinesh.foodie.databases.FavEntitiy

class FavouritesFragment : Fragment() {

    lateinit var txtNoFav: TextView
    lateinit var favProgressLayout: RelativeLayout
    lateinit var favProgressBar: ProgressBar
    lateinit var favRecyclerView: RecyclerView
    lateinit var favRecyclerAdapter: FavouritesRecyclerAdapter
    lateinit var favLayoutManager: RecyclerView.LayoutManager
    var favList = listOf<FavEntitiy>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        txtNoFav = view.findViewById(R.id.txtNoFav)
        txtNoFav.visibility = View.GONE
        favProgressLayout = view.findViewById(R.id.favProgressLayout)
        favProgressLayout.visibility = View.VISIBLE
        favProgressBar = view.findViewById(R.id.favProgressBar)
        favProgressBar.visibility = View.VISIBLE
        favRecyclerView = view.findViewById(R.id.recyclerFav)

        favLayoutManager = LinearLayoutManager(activity as Context)

        favList = RetrieveFavourites(activity as Context).execute().get()

        if(favList.isEmpty()){
            favProgressLayout.visibility = View.GONE
            favProgressBar.visibility = View.GONE
            txtNoFav.visibility = View.VISIBLE
        }
        else{
            if(activity!=null){
                favProgressLayout.visibility = View.GONE
                favProgressBar.visibility = View.GONE
                favRecyclerAdapter = FavouritesRecyclerAdapter(activity as Context,favList)
                favRecyclerView.adapter = favRecyclerAdapter
                favRecyclerView.layoutManager = favLayoutManager
            }
        }

        return view
    }

    class RetrieveFavourites(val context: Context): AsyncTask<Void,Void,List<FavEntitiy>>(){
        override fun doInBackground(vararg params: Void?): List<FavEntitiy> {

            val db = Room.databaseBuilder(context,FavDatabase::class.java,"favourites_db").build()

            return db.favDao().getAllFavourites()

        }

    }

}
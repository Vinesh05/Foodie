package com.vinesh.foodie.Activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import com.vinesh.foodie.fragments.HomeFragment
import com.vinesh.foodie.R
import com.vinesh.foodie.databases.FavDatabase
import com.vinesh.foodie.fragments.*

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var drawerHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.toolbar)
        drawerLayout= findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.NavView)
        val headerView = navigationView.getHeaderView(0)
        drawerHeader = headerView.findViewById(R.id.drawerUserName)
        drawerHeader.setText("${sharedPreferences.getString("user_name","UserName")}")

        var previousItem: MenuItem? = null

        setUpToolbar()

        openHome()

        val actionBarToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()

        navigationView.setNavigationItemSelectedListener {

            if(previousItem!=null){
                previousItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousItem = it

            when(it.itemId){

                R.id.menu_home ->{
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.menu_profile ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame,ProfileFragment())
                        .commit()
                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_fav ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame,FavouritesFragment())
                        .commit()
                    supportActionBar?.title = "Favourite Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_hist ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame,HistoryFragment())
                        .commit()
                    supportActionBar?.title = "Order History"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_faq ->{
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame,FaqFragment())
                        .commit()
                    supportActionBar?.title = "FAQ's"
                    drawerLayout.closeDrawers()
                }
                R.id.menu_logout ->{

                    DeleteDbAsyncTaskClass(applicationContext).execute()

                    sharedPreferences.edit().clear().apply()

                    Toast.makeText(this@MainActivity,"Logged Out",Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@MainActivity,LoginActivity::class.java)
                    startActivity(intent)
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openHome(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())
            .commit()

        supportActionBar?.title = "Restaurants"
        navigationView.setCheckedItem(R.id.menu_home)
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when(frag){

            !is HomeFragment -> openHome()

            else -> ActivityCompat.finishAffinity(this@MainActivity)

        }
    }

    class DeleteDbAsyncTaskClass(val context: Context): AsyncTask<Void,Void,Boolean>(){

        val db = Room.databaseBuilder(context,FavDatabase::class.java,"favourites_db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            db.favDao().deleteAll()
            db.close()

            return true

        }

    }

}
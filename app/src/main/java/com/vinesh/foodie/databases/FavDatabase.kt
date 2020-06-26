package com.vinesh.foodie.databases

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavEntitiy::class],version = 1)
abstract class FavDatabase: RoomDatabase() {

    abstract fun favDao(): FavDao

}
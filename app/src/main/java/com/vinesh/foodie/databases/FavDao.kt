package com.vinesh.foodie.databases

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavDao {

    @Insert
    fun insertFavourite(favEntitiy: FavEntitiy)

    @Delete
    fun deleteFavourite(favEntitiy: FavEntitiy)

    @Query("DELETE FROM Favourites")
    fun deleteAll()

    @Query("SELECT * FROM Favourites")
    fun getAllFavourites(): List<FavEntitiy>

    @Query("SELECT * FROM Favourites WHERE res_id=:resId")
    fun getFavouriteById(resId: String): FavEntitiy

}
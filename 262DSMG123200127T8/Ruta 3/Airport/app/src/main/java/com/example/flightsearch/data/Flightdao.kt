package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {

    @Query(
        """
        SELECT * FROM airport
        WHERE name LIKE '%' || :query || '%' OR iata_code LIKE '%' || :query || '%'
        ORDER BY passengers DESC
        """
    )
    fun searchAirports(query: String): Flow<List<Airport>>

    // Todos los aeropuertos menos el de salida
    @Query("SELECT * FROM airport WHERE iata_code != :code ORDER BY passengers DESC")
    fun getDestinations(code: String): Flow<List<Airport>>

    @Query("SELECT * FROM favorite")
    fun getFavorites(): Flow<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE departure_code = :departure AND destination_code = :destination")
    suspend fun deleteFavorite(departure: String, destination: String)
}
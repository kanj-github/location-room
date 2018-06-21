package com.example.kanj.steallocation.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface LocDao {
    @Query("SELECT * FROM Loc ORDER BY timestamp")
    Maybe<List<Loc>> getSavedLocations();

    @Insert
    void insertLocations(ArrayList<Loc> locs);

    @Delete
    void delete(Loc loc);

    @Query("DELETE FROM Loc")
    void clearDb();
}

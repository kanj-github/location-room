package com.example.kanj.steallocation.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

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

    /*****************************************************
     * Methods for testing room behavior
     *****************************************************/

    @Query("SELECT COUNT(id) FROM Loc")
    Single<Integer> getCountSingle();

    @Query("SELECT COUNT(id) FROM Loc")
    Maybe<Integer> getCountMaybe();

    @Query("SELECT COUNT(id) FROM Loc")
    Flowable<Integer> getCountFlowable();

    @Query("SELECT id FROM Loc")
    Single<List<Integer>> getIdListSingle();

    @Query("SELECT id FROM Loc")
    Maybe<List<Integer>> getIdListMaybe();

    @Query("SELECT id FROM Loc")
    Flowable<List<Integer>> getIdListFlowable();

    @Query("SELECT * FROM Loc WHERE id=:id")
    Single<Loc> getLocationSingle(int id);

    @Query("SELECT * FROM Loc WHERE id=:id")
    Maybe<Loc> getLocationMaybe(int id);

    @Query("SELECT * FROM Loc WHERE id=:id")
    Flowable<Loc> getLocationFlowable(int id);
}

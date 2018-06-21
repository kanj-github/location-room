package com.example.kanj.steallocation.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Loc.class}, version = 1)
public abstract class LocationDb extends RoomDatabase {
    public abstract LocDao locDao();
}

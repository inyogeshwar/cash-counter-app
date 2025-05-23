package com.yogeshwar.cashcounter;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface CashHistoryDao {
    @Insert
    void insert(CashHistory history);

    @Query("SELECT * FROM CashHistory ORDER BY timestamp DESC")
    LiveData<List<CashHistory>> getAll();
}
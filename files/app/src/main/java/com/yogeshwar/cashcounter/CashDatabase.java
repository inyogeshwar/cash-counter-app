package com.yogeshwar.cashcounter;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CashHistory.class}, version = 1)
public abstract class CashDatabase extends RoomDatabase {
    private static volatile CashDatabase INSTANCE;
    public abstract CashHistoryDao cashHistoryDao();

    public static CashDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CashDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CashDatabase.class, "cash_history.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(2);

    public static ExecutorService executor() {
        return databaseWriteExecutor;
    }
}
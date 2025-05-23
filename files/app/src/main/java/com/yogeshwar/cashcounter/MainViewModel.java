package com.yogeshwar.cashcounter;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final CashHistoryDao dao;
    private final LiveData<List<CashHistory>> allHistory;

    public MainViewModel(@NonNull Application application) {
        super(application);
        dao = CashDatabase.getInstance(application).cashHistoryDao();
        allHistory = dao.getAll();
    }

    public LiveData<List<CashHistory>> getAllHistory() {
        return allHistory;
    }

    public void insert(CashHistory history) {
        CashDatabase.executor().execute(() -> dao.insert(history));
    }
}
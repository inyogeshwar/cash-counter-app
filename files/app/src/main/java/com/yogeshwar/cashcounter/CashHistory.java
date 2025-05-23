package com.yogeshwar.cashcounter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CashHistory {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long timestamp;
    public long total;
    public int n2000, n500, n200, n100, n50, n20, n10, n5, n2, n1;

    public CashHistory(long timestamp, long total, int n2000, int n500, int n200, int n100, int n50, int n20, int n10, int n5, int n2, int n1) {
        this.timestamp = timestamp;
        this.total = total;
        this.n2000 = n2000;
        this.n500 = n500;
        this.n200 = n200;
        this.n100 = n100;
        this.n50 = n50;
        this.n20 = n20;
        this.n10 = n10;
        this.n5 = n5;
        this.n2 = n2;
        this.n1 = n1;
    }
}
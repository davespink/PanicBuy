package com.example.panicbuy;


/**
 * Created by User on 3/14/2017.
 */

public class Stock {
    private String my_id;
    private String barcode;
    private String description;
    private String stocklevel;

    private String tobuy;

    private String minstock;

    private String lastupdate;

    public Stock(String my_id, String barcode, String description, String stocklevel, String tobuy, String minstock, String lastupdate) {

        if(barcode.length()==0) {
            long now = (long) System.currentTimeMillis();
            barcode = Long.toString(now);
        }

        this.my_id = my_id;
        this.barcode = barcode;
        this.description = description;
        this.stocklevel = stocklevel;
        this.tobuy = tobuy;
        this.minstock = minstock;
        this.lastupdate = lastupdate;
    }

    public String getMy_id() {
        return my_id;
    }

    public void setMy_id(String my_id) {
        this.my_id = my_id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getStockLevel() {
        return stocklevel;
    }

    public void setStockLevel(String stockLevel) {
        this.stocklevel = stocklevel;
    }
    public String getToBuy() {
        if(this.tobuy==null) {
           this.tobuy = new String("N");
        }
        return this.tobuy;
    }

}

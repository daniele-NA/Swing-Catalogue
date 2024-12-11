package net.bar.model;

import java.io.Serializable;

public class PackOfCigarettes implements Serializable {
    private final String code;
    private final String name;
    private float price;
    private int amount;

    PackOfCigarettes(String code, String name, float price, int amount) {
        check(code);
        check(name);

        this.code=code;
        this.name = name;
        this.price = Math.max(0,price);
        this.amount = Math.max(0, amount);

    }

    private void check(String s){
        if(s==null || s.trim().isEmpty())throw new RuntimeException();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }



    public void setPrice(float price) {
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

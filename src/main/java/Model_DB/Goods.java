package Model_DB;

import java.util.HashMap;

public class Goods {
    int id;
    String name;
    float purchase_price;
    float selling_price;
    int count;
    String type;

    public Goods(int id, String name, float purchase_price, float sale_price, int count, String type) {
        this.id = id;
        this.name = name;
        this.purchase_price = purchase_price;
        this.selling_price = sale_price;
        this.count = count;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getPurchase_price() {
        return purchase_price;
    }

    public float getSelling_price() {
        return selling_price;
    }

    public int getCount() {
        return count;
    }

    public String getType() {
        return type;
    }

    public HashMap<String, Object> getInformation() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("name", name);
        hashMap.put("purchase_price", purchase_price);
        hashMap.put("selling_price", selling_price);
        hashMap.put("count", count);
        hashMap.put("type", type);
        return hashMap;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPurchase_price(float purchase_price) {
        this.purchase_price = purchase_price;
    }

    public void setSelling_price(float selling_price) {
        this.selling_price = selling_price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setType(String type) {
        this.type = type;
    }

}

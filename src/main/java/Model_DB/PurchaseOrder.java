package Model_DB;

import java.util.Date;
import java.util.HashMap;

public class PurchaseOrder {
    private int id;
    private Date date;
    private String goods_id;
    private int count;
    private float total_price;
    private int supplier_id;

    public PurchaseOrder(int id, Date date, String goods_id, int count, float total_price, int supplier_id) {
        this.id = id;
        this.date = date;
        this.goods_id = goods_id;
        this.count = count;
        this.total_price = total_price;
        this.supplier_id = supplier_id;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public int getCount() {
        return count;
    }

    public float getTotal_price() {
        return total_price;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public HashMap<String, Object> getInformation() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", id);
        hashMap.put("date", date);
        hashMap.put("goods_id", goods_id);
        hashMap.put("count", count);
        hashMap.put("total_price", total_price);
        hashMap.put("supplier_id", supplier_id);
        return hashMap;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }
}

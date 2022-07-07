package Model_DB;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PurchaseOrderProperty {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty goods_id;
    private final SimpleIntegerProperty count;
    private final SimpleFloatProperty total_price;
    private final SimpleIntegerProperty supplier_id;

    public PurchaseOrderProperty(int id, String goods_id, int count, float total_price, int supplier_id) {
        this.id = new SimpleIntegerProperty(id);
        this.goods_id = new SimpleStringProperty(goods_id);
        this.count = new SimpleIntegerProperty(count);
        this.total_price = new SimpleFloatProperty(total_price);
        this.supplier_id = new SimpleIntegerProperty(supplier_id);
    }

    public int getId() {
        return this.id.get();
    }

    public String getGoods_id() {
        return this.goods_id.get();
    }

    public int getCount() {
        return this.count.get();
    }

    public float getTotal_price() {
        return this.total_price.get();
    }

    public int getSupplier_id() {
        return this.supplier_id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }


    public void setGoods_id(String goods_id) {
        this.goods_id.set(goods_id);
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public void setTotal_price(float total_price) {
        this.total_price.set(total_price);
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id.set(supplier_id);
    }
}


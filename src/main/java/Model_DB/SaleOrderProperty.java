package Model_DB;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;
import java.util.HashMap;

public class SaleOrderProperty {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty goods_id;
    private final SimpleIntegerProperty count;
    private final SimpleFloatProperty total_price;
    private final SimpleIntegerProperty customer_id;

    public SaleOrderProperty(int id, String goods_id, int count, float total_price, int customer_id) {
        this.id = new SimpleIntegerProperty(id);
        this.goods_id = new SimpleStringProperty(goods_id);
        this.count = new SimpleIntegerProperty(count);
        this.total_price = new SimpleFloatProperty(total_price);
        this.customer_id = new SimpleIntegerProperty(customer_id);
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

    public int getCustomer_id() {
        return this.customer_id.get();
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

    public void setCustomer_id(int customer_id) {
        this.customer_id.set(customer_id);
    }

}

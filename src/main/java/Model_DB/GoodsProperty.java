package Model_DB;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GoodsProperty {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleFloatProperty purchase_price;
    private final SimpleFloatProperty selling_price;
    private final SimpleIntegerProperty count;
    private final SimpleStringProperty type;

    public GoodsProperty(int id, String name, float purchase_price, float selling_price, int count, String type) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.purchase_price = new SimpleFloatProperty(purchase_price);
        this.selling_price = new SimpleFloatProperty(selling_price);
        this.count = new SimpleIntegerProperty(count);
        this.type = new SimpleStringProperty(type);
    }

    public int getId() {
        return this.id.get();
    }

    public String getName() {
        return this.name.get();
    }

    public float getPurchase_price() {
        return this.purchase_price.get();
    }

    public float getSelling_price() {
        return this.selling_price.get();
    }

    public int getCount() {
        return this.count.get();
    }

    public String getType() {
        return this.type.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setPurchase_price(float purchase_price) {
        this.purchase_price.set(purchase_price);
    }

    public void setSelling_price(float selling_price) {
        this.selling_price.set(selling_price);
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public void setType(String type) {
        this.type.set(type);
    }

}

package com.example.objectboxapp.models;

import com.example.objectboxapp.ObjectBox;

import java.util.List;

import io.objectbox.BoxStore;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Driver {

    @Id
    public long id;
    public String name;

    public ToOne<Truck> truck;
    public ToOne<Carrier> carrier;

    public static void removeDriversCascade(List<Driver> drivers) {
        BoxStore boxStore = ObjectBox.getStore();
        boxStore.runInTx(() -> {
            for (Driver driver : drivers) {
                if (!driver.truck.isNull()) {
                    boxStore.boxFor(Truck.class).remove(driver.truck.getTarget());
                }
            }
            boxStore.boxFor(Driver.class).remove(drivers);
        });
    }
}

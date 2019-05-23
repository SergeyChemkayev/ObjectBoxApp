package com.example.objectboxapp.models;

import com.example.objectboxapp.ObjectBox;

import java.util.List;

import io.objectbox.BoxStore;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Carrier {

    @Id
    public long id;
    public String name;

    @Backlink(to = "carrier")
    public ToMany<Driver> drivers;

    public static void removeCarriersCascade(List<Carrier> carriers) {
        BoxStore boxStore = ObjectBox.getStore();
        boxStore.runInTx(() -> {
            for (Carrier carrier : carriers) {
                if (carrier.drivers != null && !carrier.drivers.isEmpty()) {
                    Driver.removeDriversCascade(carrier.drivers);
                }
            }
            boxStore.boxFor(Carrier.class).remove(carriers);
        });
    }
}

package com.example.objectboxapp.models;

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
}

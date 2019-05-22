package com.example.objectboxapp.models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Truck {

    @Id
    public long id;
    public String model;
}

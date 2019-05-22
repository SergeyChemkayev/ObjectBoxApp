package com.example.objectboxapp.models;

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
}

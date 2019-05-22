package com.example.objectboxapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.objectboxapp.models.Carrier;
import com.example.objectboxapp.models.Driver;
import com.example.objectboxapp.models.Truck;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_carriers_count_text_view)
    TextView carriersCountTextView;
    @BindView(R.id.activity_main_drivers_count_text_view)
    TextView driversCountTextView;
    @BindView(R.id.activity_main_trucks_count_text_view)
    TextView trucksCountTextView;

    private Box<Carrier> carriersBox = ObjectBox.getStore().boxFor(Carrier.class);
    private Box<Driver> driversBox = ObjectBox.getStore().boxFor(Driver.class);
    private Box<Truck> trucksBox = ObjectBox.getStore().boxFor(Truck.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_main_add_carrier_text_view)
    public void onAddCarrierClick() {

    }

    @OnClick(R.id.activity_main_add_driver_text_view)
    public void onAddDriverClick() {

    }

    @OnClick(R.id.activity_main_add_truck_text_view)
    public void onAddTruckClick() {

    }

    private void updateCarriersCount() {
        carriersCountTextView.setText(String.valueOf(carriersBox.getAll().size()));
    }

    private void updateDriversCount() {
        driversCountTextView.setText(String.valueOf(driversBox.getAll().size()));
    }

    private void updateTrucksCount() {
        trucksCountTextView.setText(String.valueOf(trucksBox.getAll().size()));
    }
}

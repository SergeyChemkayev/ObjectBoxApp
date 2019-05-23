package com.example.objectboxapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.objectboxapp.models.Carrier;
import com.example.objectboxapp.models.Driver;
import com.example.objectboxapp.models.Driver_;
import com.example.objectboxapp.models.Truck;
import com.example.objectboxapp.models.Truck_;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.android.AndroidScheduler;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_carriers_count_text_view)
    TextView carriersCountTextView;
    @BindView(R.id.activity_main_drivers_count_text_view)
    TextView driversCountTextView;
    @BindView(R.id.activity_main_trucks_count_text_view)
    TextView trucksCountTextView;
    @BindView(R.id.activity_main_truck_model_edit_text)
    EditText truckModelEditText;
    @BindView(R.id.activity_main_driver_name_edit_text)
    EditText driverNameEditText;
    @BindView(R.id.activity_main_carrier_name_edit_text)
    EditText carrierNameEditText;
    @BindView(R.id.activity_main_driver_trucks_spinner)
    Spinner driverTrucksSpinner;
    @BindView(R.id.activity_main_carrier_drivers_spinner)
    Spinner carrierDriversSpinner;

    private Box<Carrier> carriersBox = ObjectBox.getStore().boxFor(Carrier.class);
    private Box<Driver> driversBox = ObjectBox.getStore().boxFor(Driver.class);
    private Box<Truck> trucksBox = ObjectBox.getStore().boxFor(Truck.class);

    private ArrayAdapter<String> driverTrucksSpinnerAdapter;
    private ArrayAdapter<String> carrierDriversSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initSpinners();

        /*ObjectBox.getStore()
                .subscribe()
                .on(AndroidScheduler.mainThread())
                .observer(r -> {
                    updateCarriersInfo();
                    updateDriversInfo();
                    updateTrucksInfo();
                });*/

        ObjectBox.getStore()
                .subscribe(Carrier.class)
                .on(AndroidScheduler.mainThread())
                .observer(r -> updateCarriersInfo());
        ObjectBox.getStore()
                .subscribe(Driver.class)
                .on(AndroidScheduler.mainThread())
                .observer(t -> updateDriversInfo());
        ObjectBox.getStore()
                .subscribe(Truck.class)
                .on(AndroidScheduler.mainThread())
                .observer(t -> updateTrucksInfo());
    }

    public void initSpinners() {
        driverTrucksSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        driverTrucksSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carrierDriversSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        carrierDriversSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverTrucksSpinner.setAdapter(driverTrucksSpinnerAdapter);
        carrierDriversSpinner.setAdapter(carrierDriversSpinnerAdapter);
    }

    @OnClick(R.id.activity_main_add_carrier_text_view)
    public void onAddCarrierClick() {
        Carrier carrier = new Carrier();
        carrier.name = carrierNameEditText.getText().toString();
        Driver driver = driversBox.query()
                .equal(Driver_.name, carrierDriversSpinner.getSelectedItem().toString())
                .build()
                .findFirst();
        carrier.drivers.add(driver);
        carriersBox.put(carrier);
    }

    @OnClick(R.id.activity_main_add_driver_text_view)
    public void onAddDriverClick() {
        Driver driver = new Driver();
        driver.name = driverNameEditText.getText().toString();
        Truck truck = trucksBox.query()
                .equal(Truck_.model, driverTrucksSpinner.getSelectedItem().toString())
                .build()
                .findFirst();
        driver.truck.setTarget(truck);
        driversBox.put(driver);
    }

    @OnClick(R.id.activity_main_add_truck_text_view)
    public void onAddTruckClick() {
        Truck truck = new Truck();
        truck.model = truckModelEditText.getText().toString();
        trucksBox.put(truck);
    }

    @OnClick(R.id.activity_main_add_carrier_with_multiple_drivers_text_view)
    public void onAddCarrierWithMultipleDriversClick() {
        Carrier carrier = new Carrier();
        carrier.name = "carrier with multiple drivers";
        Driver firstDriver = new Driver();
        firstDriver.name = "driver1";
        Driver secondDriver = new Driver();
        secondDriver.name = "driver2";
        Truck truckForFirstDriver = new Truck();
        truckForFirstDriver.model = "truck for driver1";
        Truck truckForSecondDriver = new Truck();
        truckForSecondDriver.model = "truck for driver2";
        firstDriver.truck.setTarget(truckForFirstDriver);
        secondDriver.truck.setTarget(truckForSecondDriver);
        carrier.drivers.add(firstDriver);
        carrier.drivers.add(secondDriver);
        carriersBox.put(carrier);
    }

    @OnClick(R.id.activity_main_remove_all_carriers_text_view)
    public void onRemoveAllCarriersClick() {
        removeAllCarriersCascade();
    }

    @OnClick(R.id.activity_main_remove_all_drivers_text_view)
    public void onRemoveAllDriversClick() {
        removeAllDriversCascade();
    }

    @OnClick(R.id.activity_main_remove_all_trucks_text_view)
    public void onRemoveAllTrucksClick() {
        trucksBox.removeAll();
    }

    private void updateCarriersInfo() {
        carriersCountTextView.setText(String.valueOf(carriersBox.getAll().size()));
    }

    private void updateDriversInfo() {
        List<Driver> drivers = driversBox.getAll();
        driversCountTextView.setText(String.valueOf(drivers.size()));
        carrierDriversSpinnerAdapter.clear();
        for (Driver driver : drivers) {
            carrierDriversSpinnerAdapter.add(driver.name);
        }
        carrierDriversSpinnerAdapter.notifyDataSetChanged();
    }

    private void updateTrucksInfo() {
        List<Truck> trucks = trucksBox.getAll();
        trucksCountTextView.setText(String.valueOf(trucks.size()));
        driverTrucksSpinnerAdapter.clear();
        for (Truck truck : trucks) {
            driverTrucksSpinnerAdapter.add(truck.model);
        }
        carrierDriversSpinnerAdapter.notifyDataSetChanged();
    }

    private void removeAllCarriersCascade() {
        Carrier.removeCarriersCascade(carriersBox.getAll());
    }

    private void removeAllDriversCascade() {
        Driver.removeDriversCascade(driversBox.getAll());
    }
}

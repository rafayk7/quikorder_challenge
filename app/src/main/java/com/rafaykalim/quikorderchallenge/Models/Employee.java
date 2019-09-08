package com.rafaykalim.quikorderchallenge.Models;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Employee implements Parcelable {

    private String name;
    private String title;
    private String[] locations;

    public Employee(String name, String title, String[] locations) {
        this.name = name;
        this.locations = locations;
        this.title = title;
    }

    protected Employee(Parcel in) {
        readFromParcel(in);
    }

    // Order doesn't matter as bundle is used for clarity
    public void readFromParcel(Parcel in)
    {
        Bundle data = in.readBundle();

        name = data.getString("name");
        title = data.getString("title");
        locations = data.getStringArray("locations");
    }

    // Boilerplate code; would change as needed in actual dev but for now this is fine
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        // Use bundle for clarity, so the order for the byte stream doesn't matter
        Bundle data = new Bundle();
        data.putString("name", name);
        data.putString("title", title);
        data.putStringArray("locations", locations);

        parcel.writeBundle(data);
    }

    // Boilerplate code
    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public String getName()
    {
        return name;
    }


    public String getTitle()
    {
        return title;
    }


    public String[] getLocations()
    {
        return locations;
    }

}
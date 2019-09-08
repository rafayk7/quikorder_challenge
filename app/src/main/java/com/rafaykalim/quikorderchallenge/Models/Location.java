package com.rafaykalim.quikorderchallenge.Models;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class Location extends ExpandableGroup<Employee> {
    private String name;
    private int backgroundImgId;
    private ArrayList<Employee> items;

    public Location(String title, ArrayList<Employee> items, int backgroundImgId) {
        super(title, items);
        this.backgroundImgId = backgroundImgId;
        this.name = title;
        this.items = items;
    }

    public int getBackgroundImgId() {
        return backgroundImgId;
    }
    public String getName() {
        return name;
    }
    public ArrayList<Employee> getItems() {
        return items;
    }

    // Boilerplate, but this ensures 0 collisions for now haha. Would fix in actual dev
    @Override
    public int hashCode() {
        return getBackgroundImgId();
    }
}

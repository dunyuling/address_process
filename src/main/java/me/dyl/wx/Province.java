package me.dyl.wx;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pro on 17-5-15.
 */
public class Province {

    private String id;
    private String name;
    private List<City> city = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}

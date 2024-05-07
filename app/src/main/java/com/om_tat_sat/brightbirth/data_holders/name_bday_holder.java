package com.om_tat_sat.brightbirth.data_holders;

public class name_bday_holder {
    String name;
    String date;
    String month;
    String year;

    public name_bday_holder(String name, String date_from_database) {
        this.name = name;
        String[] strings=date_from_database.split("_");
        date=strings[0];
        month=strings[1];
        year=strings[2];
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}

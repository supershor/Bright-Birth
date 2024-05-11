package com.om_tat_sat.brightbirth.data_holders;

public class name_bday_holder {
    String name;
    String zodiac;
    String date;
    String month;
    String year;

    public String getZodiac() {
        return zodiac;
    }


    public name_bday_holder(String name, String date_from_database, String zodiac) {
        this.name = name;
        this.zodiac=zodiac;
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

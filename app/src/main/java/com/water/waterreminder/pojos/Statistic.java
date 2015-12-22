package com.water.waterreminder.pojos;

/**
 * Created by kurayogun on 26/10/15.
 */
public class Statistic {

    private int icon;
    private String statistic_name;
    private String statistic_result;


    public Statistic(int icon, String statistic_name, String statistic_result) {
        this.icon = icon;
        this.statistic_name = statistic_name;
        this.statistic_result = statistic_result;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getStatistic_name() {
        return statistic_name;
    }

    public void setStatistic_name(String statistic_name) {
        this.statistic_name = statistic_name;
    }

    public String getStatistic_result() {
        return statistic_result;
    }

    public void setStatistic_result(String statistic_result) {
        this.statistic_result = statistic_result;
    }
}

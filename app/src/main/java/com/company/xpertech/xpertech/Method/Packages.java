package com.company.xpertech.xpertech.Method;

/**
 * Created by Skylar Gail on 8/12/2018.
 */

public class Packages {

    String title;
    String numOfChannel;
    double monthSubPerMain;
    double monthSubPerExt;
    double digitalBox;

    public Packages(String title){
        this.title = title;
    }
    public Packages(String title, String numOfChannel) {
        this.title = title;
        this.numOfChannel = numOfChannel;
    }

    public double getMonthSubPerMain() {
        return monthSubPerMain;
    }

    public void setMonthSubPerMain(double monthSubPerMain) {
        this.monthSubPerMain = monthSubPerMain;
    }

    public double getMonthSubPerExt() {
        return monthSubPerExt;
    }

    public void setMonthSubPerExt(double monthSubPerExt) {
        this.monthSubPerExt = monthSubPerExt;
    }

    public double getDigitalBox() {
        return digitalBox;
    }

    public void setDigitalBox(double digitalBox) {
        this.digitalBox = digitalBox;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumOfChannel() {
        return numOfChannel;
    }

    public void setNumOfChannel(String numOfChannel) {
        this.numOfChannel = numOfChannel;
    }
}

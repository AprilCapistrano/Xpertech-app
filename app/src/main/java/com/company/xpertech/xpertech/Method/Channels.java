package com.company.xpertech.xpertech.Method;

/**
 * Created by Skylar Gail on 8/14/2018.
 */

public class Channels {

    int channelNumber;
    String title;

    public Channels(String title) {
        this.title = title;
    }

    public Channels(int channelNumber, String title) {
        this.channelNumber = channelNumber;
        this.title = title;

    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

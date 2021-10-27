package com.bermin.landlords.client.model;

import javax.swing.*;

//扑克标签类
public class PokerLabel extends JLabel implements Comparable {
    private int id;
    private String name;
    private int num;
    private boolean isOut;
    private boolean isUp;
    private boolean isSelected;

    public static String pockImagePath = "LandlordsClient/images/poker/";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PokerLabel() {
        this.setSize(105, 150);
    }

    public PokerLabel(int id, String name, int num) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.setSize(105, 150);
    }

    public PokerLabel(int id, String name, int num, boolean isOut, boolean isUp) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.isOut = isOut;
        this.isUp = isUp;

        if (isUp) {
            turnUp();
        } else {
            turnDown();
        }
        this.setSize(105, 150);
    }

    public void turnUp() {
        this.setIcon(new ImageIcon(pockImagePath + id + ".jpg"));
    }

    public void turnDown() {
        this.setIcon(new ImageIcon(pockImagePath + "down.jpg"));
    }

    @Override
    public int compareTo(Object o) {

        PokerLabel pokerLabel = (PokerLabel) o;
        return Integer.compare(this.num, pokerLabel.num);
    }
}

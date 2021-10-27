package com.bermin.landlords.client.util;

import com.bermin.landlords.client.model.PokerLabel;

public class GameUtil {
    public static void move(PokerLabel pokerLabel, int x, int y) {

        pokerLabel.setLocation(x, y);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

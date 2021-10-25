package com.bermin.landlords.client.thread;

import com.alibaba.fastjson.JSON;
import com.bermin.landlords.client.model.Message;
import com.bermin.landlords.client.view.MainFrame;

public class CountThread extends Thread{
    private int index;
    private MainFrame mainFrame;
    private boolean isRun;

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public CountThread(int index, MainFrame mainFrame) {
        this.index = index;
        this.mainFrame = mainFrame;
        this.isRun = true;
    }

    @Override
    public void run() {
        while (index >= 0 && isRun) {
            mainFrame.timeLabel.setText(index + "");
            index--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Message msg = null;
        if (index == 1 || !isRun && !mainFrame.isLord) {
            msg = new Message(1, mainFrame.currentPlayer.getId(), "不抢", null);
        }

        //
        if (!isRun && mainFrame.isLord) {
            msg = new Message(2,mainFrame.currentPlayer.getId(), "抢地主", null);
        }

        mainFrame.sendThread.setMsg(JSON.toJSONString(msg));
    }
}

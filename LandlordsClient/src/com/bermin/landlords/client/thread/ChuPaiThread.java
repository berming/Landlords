package com.bermin.landlords.client.thread;

import com.alibaba.fastjson.JSON;
import com.bermin.landlords.client.model.Message;
import com.bermin.landlords.client.model.Poker;
import com.bermin.landlords.client.model.PokerLabel;
import com.bermin.landlords.client.view.MainFrame;

import java.util.ArrayList;
import java.util.List;

public class ChuPaiThread extends Thread {
    private int time;
    private MainFrame mainFrame;
    private boolean isRun;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    public ChuPaiThread(int time, MainFrame mainFrame) {
        this.time = time;
        this.mainFrame = mainFrame;
        this.isRun = true;
    }

    public ChuPaiThread(int time, MainFrame mainFrame, boolean isRun) {
        this.time = time;
        this.mainFrame = mainFrame;
        this.isRun = isRun;
    }

    @Override
    public void run() {
        while(time>=0 && isRun)
        {
            mainFrame.timeLabel.setText(time+"");
            time--;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        Message message = null;
        //如果是不出 (一种是时间到了 一种是 选择不出)
        if(time==-1 || isRun==false && mainFrame.isOut==false)
        {
            message=new Message(3,mainFrame.currentPlayer.getId(),"不出",null);

            //转换为json 交给 sendThread发送到服务器去
            String msg= JSON.toJSONString(message);
            mainFrame.sendThread.setMsg(msg);
        }

        //出牌
        if(isRun==false && mainFrame.isOut==true)
        {
            message=new Message(4,mainFrame.currentPlayer.getId(),"出牌", changePokerLabelToPoker(mainFrame.selectedPokerLabels));

            //转换为json 交给 sendThread发送到服务器去
            String msg=JSON.toJSONString(message);
            mainFrame.sendThread.setMsg(msg);
            //将当前发送出去的扑克牌 从扑克牌列表中移除
            mainFrame.removeOutPokerFromPokerList();

            //如果扑克列表的数量为0 代表赢了
            if(mainFrame.pokerLabels.size()==0)
            {
                message=new Message(5, mainFrame.currentPlayer.getId(), "游戏结束", null);
                msg=JSON.toJSONString(message);
                try {
                    Thread.sleep(100);
                    mainFrame.sendThread.setMsg(msg);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }


        }
    }

    public List<Poker> changePokerLabelToPoker(List<PokerLabel> selectedPokerLabels)
    {
        List<Poker> list= new ArrayList<>();
        for(int i=0;i<selectedPokerLabels.size();i++)
        {
            PokerLabel pokerLabel = selectedPokerLabels.get(i);
            Poker poker = new Poker(pokerLabel.getId(), pokerLabel.getName(), pokerLabel.getNum());
            list.add(poker);
        }

        return list;
    }

}

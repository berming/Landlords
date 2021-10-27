package com.bermin.landlords.client.thread;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bermin.landlords.client.model.Player;
import com.bermin.landlords.client.model.Poker;
import com.bermin.landlords.client.view.MainFrame;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//创建一个接收消息的线程
public class ReceiveThread extends Thread{
    private Socket socket;
    private MainFrame mainFrame;
    private int playStep = 0;

    public ReceiveThread(Socket socket, MainFrame mainFrame) {
        this.socket = socket;
        this.mainFrame = mainFrame;
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            while (true) {
				// 接收从服务器端传递过来的消息 json字符串
                String jsonString = dataInputStream.readUTF();

                if (playStep == 0) {
                    // 接收玩家及扑克牌信息
                    System.out.println(jsonString);

                    List<Player> players = new ArrayList<>();
                    JSONArray playerJsonArray = JSONArray.parseArray(jsonString);
                    for (int i = 0; i < playerJsonArray.size(); i++) {

                        // 获得当个json对象--> 玩家对象
                        JSONObject playerJson = (JSONObject)playerJsonArray.get(i);

                        // 玩家对象
                        int id = playerJson.getInteger("id");
                        String name = playerJson.getString("name");

                        // 存放扑克列表
                        List<Poker> pokers = new ArrayList<>();
                        JSONArray pokerJsonArray = playerJson.getJSONArray("pokers");
                        for (int j = 0; j < pokerJsonArray.size(); j++) {

                            // 单个扑克对象
                            JSONObject pokerJson = (JSONObject)pokerJsonArray.get(j);
                            int pid = pokerJson.getInteger("id");
                            String pName = pokerJson.getString("name");
                            int num = pokerJson.getInteger("num");
                            Poker poker = new Poker(pid, pName, num);
                            pokers.add(poker);
                        }

                        Player player = new Player(id, name, pokers);
                        players.add(player);
                    }

                    // 获得3个玩家的信息了
                    if (players.size() == 3) {
                        mainFrame.showAllPlayersInfo(players);
                        playStep = 1;
                    }
                } else if (playStep == 1) {
                    // 抢地主或出牌消息
                    JSONObject msgJsonObject = JSONObject.parseObject(jsonString);

                    int typeId = msgJsonObject.getInteger("typeId");
                    int playerId = msgJsonObject.getInteger("playerId");
                    String contentString = msgJsonObject.getString("content");

                    if (typeId == 1) { // 不抢地主
                        mainFrame.showMsg(1);
                        if (playerId + 1 == mainFrame.currentPlayer.getId()) {
                            mainFrame.getLord();
                        }
                    }

                    if (typeId == 2) { // 抢地主
                        //获得地主牌
                        JSONArray pokersJsonArray= msgJsonObject.getJSONArray("pokers");
                        List<Poker> lordPokers = new ArrayList<>();

                        for (int i = 0; i < pokersJsonArray.size(); i++) {
                            JSONObject pokerJsonObject = (JSONObject) pokersJsonArray.get(i);
                            int id = pokerJsonObject.getInteger("id");
                            String name = pokerJsonObject.getString("name");
                            int num = pokerJsonObject.getInteger("num");
                            Poker p = new Poker(id, name, num);
                            System.out.println(p);
                            lordPokers.add(p);
                        }

                        // 如果是自己抢的地主
                        if (mainFrame.currentPlayer.getId() == playerId) {
                            mainFrame.addLordPokers(lordPokers); // 添加地主牌
                            mainFrame.showChuPaiJabel();         // 第一家出牌 显示出牌的按钮
                        }

                        mainFrame.showLordIcon(playerId);     // 显示地主图标
                        mainFrame.msgLabel.setVisible(false); // 之前的消息框隐藏

                        //所有玩家 都可以选择出牌列表 (不代表能出牌)
                        mainFrame.addClickEventToPoker();
                    }

                    if (typeId == 3) { // 不出牌
                        //显示不出牌的消息
                        mainFrame.showMsg(3);

                        //判断自己是不是下一家 如果是 显示出牌按钮  0->1   1-> 2   2-> 0
                        if (playerId + 1 == mainFrame.currentPlayer.getId() || playerId - 2 == mainFrame.currentPlayer.getId()) {
                            mainFrame.showChuPaiJabel();
                        }
                    }

                    if (typeId == 4) { // 出牌
                        //获得出牌列表
                        JSONArray pokersJsonArray= msgJsonObject.getJSONArray("pokers");
                        List<Poker> outPokers = new ArrayList<>();

                        for (int i = 0; i < pokersJsonArray.size(); i++) {
                            JSONObject pokerJsonObject = (JSONObject) pokersJsonArray.get(i);
                            int id = pokerJsonObject.getInteger("id");
                            String name = pokerJsonObject.getString("name");
                            int num = pokerJsonObject.getInteger("num");
                            Poker p = new Poker(id, name, num);
                            System.out.println(p);
                            outPokers.add(p);
                        }

                        //显示出牌列表
                        mainFrame.showOutPokerList(playerId, outPokers);

                        //判断自己是不是下一家 如果是 显示出牌按钮  0->1   1-> 2   2-> 0
                        if (playerId + 1 == mainFrame.currentPlayer.getId() || playerId - 2 == mainFrame.currentPlayer.getId()) {
                            mainFrame.showChuPaiJabel();
                        }

                        mainFrame.prevPlayerId = playerId; // 记录上一个出牌的玩家id
                    }
                    if (typeId == 5) { // 游戏结束
                        if (playerId == mainFrame.currentPlayer.getId()) {
                            JOptionPane.showMessageDialog(mainFrame, "赢了");
                        } else {
                            JOptionPane.showMessageDialog(mainFrame, "输了");
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

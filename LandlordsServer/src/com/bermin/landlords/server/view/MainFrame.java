package com.bermin.landlords.server.view;

import com.alibaba.fastjson.JSONObject;
import com.bermin.landlords.server.model.Message;
import com.bermin.landlords.server.model.Player;
import com.bermin.landlords.server.model.Poker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class MainFrame {

    public int playerIndex;  // 玩家序号
    public int playStep;     // 牌局进展

    public List<Player> players = new ArrayList<>();

    public List<Poker> allPokers = new ArrayList<>();

    public List<Poker> lordPokers = new ArrayList<>();

    public MainFrame() {
        // 创建扑克列表
        createPokers();

        playerIndex = 0;
        playStep = 0;

        try {
            ServerSocket serverSocket = new ServerSocket(8168);
            while (true) {
                Socket socket = serverSocket.accept();
                AcceptThread acceptThread = new AcceptThread(socket);
                acceptThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
			System.out.println("服务器端异常");
        }
    }

    // 接收线程，处理客户端消息
    class AcceptThread extends Thread {
        Socket socket;

        public AcceptThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                while (true) {
                    String msg = dataInputStream.readUTF();

                    if (playStep == 0) {  // 玩家上线消息
                        Player player = new Player(playerIndex++, msg);
                        player.setSocket(socket);
                        players.add(player);
                        System.out.println(msg + "上线了");
                        System.out.println("上线人数：" + players.size());

                        // 玩家到齐，发牌
                        if (playerIndex == 3) {
                            deal();
                            playStep = 1;
                        }
                    } else if (playStep == 1) {  // 抢地主消息
                        System.out.println("接收抢地主消息");
                        JSONObject msgJsonObject;
                        msgJsonObject = JSON.parseObject(msg);
                        int typeId = msgJsonObject.getInteger("typeId");
                        int playerId = msgJsonObject.getInteger("playerId");
                        String content = msgJsonObject.getString( "content");

                        if (typeId == 2) { // 抢地主
                            Message sendMessage;
                            sendMessage = new Message(typeId, playerId, content, lordPokers);
                            msg = JSON.toJSONString(sendMessage);
                            playStep = 2;
                        }

                        sendMessageToClient(msg); // 不抢地主，群发消息

                    } else if (playStep == 2) { // 出牌和不出牌和游戏结束
                        sendMessageToClient(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** 群发消息到客户端
     */
    public void sendMessageToClient(String msg) {

        for (Player player : players) {
            DataOutputStream dataOutputStream;
            try {
                dataOutputStream = new DataOutputStream(player.getSocket().getOutputStream());
                dataOutputStream.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /** 创建所有的扑克列表
     */
    public void createPokers() {
        //创建大小王
        Poker redJoker = new Poker(0,"大王",17);
        Poker blackJoker = new Poker(1,"小王",16);
        allPokers.add(redJoker);
        allPokers.add(blackJoker);

        // 创建其它扑克牌
        String[] names=new String[]{"2","A","K","Q","J","10","9","8","7","6","5","4","3"};
        // String[] colors=new String[]{"黑桃","红桃","梅花","方块"};
        String[] colors=new String[]{"♠️","♥️","♣️","♦️"};
        int id = 2;
        int num = 15;
        for (String name : names) {
            for (String color : colors) {
                Poker poker = new Poker(id++, color+name, num);
                allPokers.add(poker);
            }
            num--;
        }
        // 洗牌
        Collections.shuffle(allPokers);
    }

    /** 发牌
     */
    public void deal() {
        // 将牌分给三个玩家， 最后三张作为地主牌
        for (int i = 0; i < allPokers.size(); i++) {
            if (i >= 51) {
                lordPokers.add(allPokers.get(i));
            } else {
                players.get(i%3).getPokers().add(allPokers.get(i));
            }
        }

        // 玩家信息发送给客户端
        for (int i = 0; i < players.size(); i++) {
            try {
                DataOutputStream dataOutputStream = new DataOutputStream(players.get(i).getSocket().getOutputStream());

                String jsonString = JSON.toJSONString(players);
                dataOutputStream.writeUTF(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

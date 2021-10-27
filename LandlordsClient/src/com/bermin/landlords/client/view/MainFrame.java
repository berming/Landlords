package com.bermin.landlords.client.view;

import com.bermin.landlords.client.model.Player;
import com.bermin.landlords.client.model.Poker;
import com.bermin.landlords.client.model.PokerLabel;
import com.bermin.landlords.client.thread.ChuPaiThread;
import com.bermin.landlords.client.thread.CountThread;
import com.bermin.landlords.client.thread.ReceiveThread;
import com.bermin.landlords.client.thread.SendThread;
import com.bermin.landlords.client.util.GameUtil;
import com.bermin.landlords.client.util.PokerRule;
import com.bermin.landlords.client.util.PokerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.Socket;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame {

    public static String bgImagePath = "LandlordsClient/images/bg/";

    public MyPanel myPanel;
    public String uname;

    public Socket socket;
    public SendThread sendThread;       // 发送消息
    public ReceiveThread receiveThread; // 接收消息
    public CountThread countThread;     // 计数器线程
    public ChuPaiThread chuPaiThread;   // 出牌线程

    public Player currentPlayer;  // 当前玩家
    public int prevPlayerId = -1; // 上个出牌的玩家

    public List<PokerLabel> pokerLabels = new ArrayList<>();  // 扑克标签列表

    public boolean isLord;     // 是否地主
    public boolean isOut;      // 选择是否出牌


    public JLabel lordLabel1;  // 抢地主标签
    public JLabel lordLabel2;  // 不叫地主
    public JLabel timeLabel;   // 定时器标签

    public JLabel msgLabel;      // 消息
    public JLabel lordIconLabel; // 地主图标
    public JLabel chuPaiJLabel;  // 出牌标签
    public JLabel buChuJLabel;   // 不出牌

    public List<PokerLabel> selectedPokerLabels = new ArrayList<>();
    public List<PokerLabel> showOutPokerLabels = new ArrayList<>();

    public MainFrame(String uname, Socket socket) throws HeadlessException {
        this.uname = uname;
        this.socket = socket;

		// 设置窗口的属性
        this.setSize(1200, 700);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 添加myPanel
        myPanel = new MyPanel();
        myPanel.setBounds(0, 0, 1200, 700);
        this.add(myPanel);

        init();

        // 启动发消息的线程
        sendThread = new SendThread(socket, uname);
        sendThread.start();

        // 接收
        receiveThread = new ReceiveThread(socket, this);
        receiveThread.start();
    }

    // 窗口初始化
    public void init () {

        // 消息框
        msgLabel = new JLabel();
        chuPaiJLabel = new JLabel();
        chuPaiJLabel.setBounds(330, 350, 110, 53);
        chuPaiJLabel.setIcon(new ImageIcon(bgImagePath + "chupai.png"));
        chuPaiJLabel.addMouseListener(new MyMouseEvent());
        chuPaiJLabel.setVisible(false);
        this.myPanel.add(chuPaiJLabel);

        buChuJLabel = new JLabel();
        buChuJLabel.setBounds(440, 350, 110, 53);
        buChuJLabel.setIcon(new ImageIcon(bgImagePath + "buchupai.png"));
        buChuJLabel.addMouseListener(new MyMouseEvent());
        buChuJLabel.setVisible(false);
        this.myPanel.add(buChuJLabel);

        timeLabel = new JLabel();
        timeLabel.setBounds(550, 350, 50, 50);
        timeLabel.setFont(new Font("Dialog", 0, 30));
        timeLabel.setForeground(Color.red);
        timeLabel.setVisible(false);
        this.myPanel.add(timeLabel);
    }

    public void showAllPlayersInfo(List<Player> players) {

        // 匹配当前玩家
        for (Player player : players) {
            if (player.getName().equals(uname)) {
                currentPlayer = player;
            }
        }

        List<Poker> pokers = currentPlayer.getPokers();
        for (int i = 0; i < pokers.size(); i++) {
            Poker poker = pokers.get(i);
            PokerLabel pokerLabel = new PokerLabel(poker.getId(),poker.getName(), poker.getNum());
            pokerLabel.turnUp();
            this.myPanel.add(pokerLabel);
            this.pokerLabels.add(pokerLabel);

            this.myPanel.setComponentZOrder(pokerLabel, 0);
            GameUtil.move(pokerLabel, 300 + 30 * i, 450);
        }

        Collections.sort(pokerLabels);
        for (int i = 0; i < pokerLabels.size(); i++) {
            this.myPanel.setComponentZOrder(pokerLabels.get(i), 0);
            GameUtil.move(pokerLabels.get(i), 300 + 30 * i, 450);
        }

        if (currentPlayer.getId() == 0) {
            getLord(); // 抢地主
        }
    }


    //抢地主
    public void getLord() {
        // 显示抢地主的按钮 和 定时器按钮
        lordLabel1 = new JLabel();
        lordLabel1.setBounds(330, 400, 104, 46);
        lordLabel1.setIcon(new ImageIcon(bgImagePath + "jiaodizhu.png"));
        lordLabel1.addMouseListener(new MyMouseEvent());
        this.myPanel.add(lordLabel1);

        lordLabel2 = new JLabel();
        lordLabel2.setBounds(440, 400, 104, 46);
        lordLabel2.setIcon(new ImageIcon(bgImagePath + "bujiao.png"));
        lordLabel2.addMouseListener(new MyMouseEvent());

        this.myPanel.add(lordLabel2);

        //显示定时器的图标
        this.timeLabel.setVisible(true);

        this.setVisible(true);
        // 重绘
        this.repaint();

        // 启动计时器的线程
        countThread = new CountThread(10, this);
        countThread.start();

    }

    //显示出牌的标签
    public void showChuPaiJabel()
    {
        // 显示出牌和不出牌的按钮 和 定时器按钮
        chuPaiJLabel.setVisible(true);
        buChuJLabel.setVisible(true);
        timeLabel.setVisible(true);

        this.repaint();

        chuPaiThread=new ChuPaiThread(30, this);
        chuPaiThread.start();

    }


    public void showMsg(int typeId) {
        msgLabel.setVisible(true);

        msgLabel.setBounds(500, 300, 129, 77);
        if(typeId==1)
            msgLabel.setIcon(new ImageIcon(bgImagePath + "buqiang.png"));
        if(typeId==3)
            msgLabel.setIcon(new ImageIcon(bgImagePath + "buchu.png"));
        this.myPanel.add(msgLabel);
        this.repaint();
    }


    // 添加地主牌
    public void addLordPokers(List<Poker> lordPokers) {
        for (int i = 0; i < lordPokers.size(); i++) {

            Poker poker = lordPokers.get(i);
            PokerLabel pokerLabel = new PokerLabel(poker.getId(),
            poker.getName(), poker.getNum());
            pokerLabel.turnUp(); // 显示正面图
            this.pokerLabels.add(pokerLabel);
        }

        Collections.sort(pokerLabels);

        for(int i=0;i<pokerLabels.size();i++)
        {
            // 添加到面板中
            this.myPanel.add(pokerLabels.get(i));
            // 动态的显示出来
            this.myPanel.setComponentZOrder(pokerLabels.get(i), 0);
            // 一张一张的显示出来
            GameUtil.move(pokerLabels.get(i), 300 + 30 * i, 450);

        }
        currentPlayer.getPokers().addAll(lordPokers);
    }

    //显示地主图标
    public void showLordIcon(int playerId)
    {
        //创建地主图标对象
        lordIconLabel=new JLabel();
        lordIconLabel.setIcon(new ImageIcon(bgImagePath + "dizhu.png"));
        lordIconLabel.setSize(60, 89);

        //根据玩家id显示到具体的位置
        //如果自己是地主
        if(playerId==currentPlayer.getId()) {
            lordIconLabel.setLocation(200, 450);
        }
        else if(playerId+1==currentPlayer.getId() || playerId-2==currentPlayer.getId()) //上家  //  2 0    0 1   1 2
        {
            lordIconLabel.setLocation(200, 100);
        }
        else { // 下家
            lordIconLabel.setLocation(950, 100);
        }

        //添加地主图标到面板上
        this.myPanel.add(lordIconLabel);

        this.repaint();  //重绘
    }

    //给扑克牌添加单击事件
    public void addClickEventToPoker()
    {
        for(int i=0;i<pokerLabels.size();i++)
        {
            pokerLabels.get(i).addMouseListener(new PokerEvent());
        }

    }

    //显示出牌的列表
    public void showOutPokerList(int playerId,List<Poker> outPokers)
    {
        //从窗口上移除之前的出牌的列表
        for(int i=0;i<showOutPokerLabels.size();i++) {
            myPanel.remove(showOutPokerLabels.get(i));
        }

        //清空之前出牌的列表
        showOutPokerLabels.clear();

        //显示当前出牌的列表
        for(int i=0;i<outPokers.size();i++) {

            Poker poker=outPokers.get(i);

            PokerLabel pokerLabel=new PokerLabel(poker.getId(),poker.getName(),poker.getNum());

            pokerLabel.setLocation(400+30*i, 200);

            pokerLabel.turnUp();

            myPanel.add(pokerLabel);

            showOutPokerLabels.add(pokerLabel);

            myPanel.setComponentZOrder(pokerLabel, 0);

        }

        this.repaint() ; //窗口重绘
    }

    //出牌  将出的牌从当前玩家的扑克列表中移除
    public void removeOutPokerFromPokerList()
    {
        //1.从当前玩家的扑克列表中移除
        pokerLabels.removeAll(selectedPokerLabels);

        //2.从面板中移除
        for(int i=0;i<selectedPokerLabels.size();i++)
        {
            myPanel.remove(selectedPokerLabels.get(i));
        }

        //3.剩下的扑克列表重新定位
        for(int i=0;i<pokerLabels.size();i++)
        {
            myPanel.setComponentZOrder(pokerLabels.get(i), 0);
            GameUtil.move(pokerLabels.get(i), 300+30*i, 450);
        }

        //4.清空选择的扑克牌列表
        selectedPokerLabels.clear();
        this.repaint();
    }

    // 创建鼠标事件监听器类
    class MyMouseEvent implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            // 点击抢地主
            if (e.getSource().equals(lordLabel1)) {
                countThread.setRun(false);
                isLord = true;
                lordLabel1.setVisible(false);
                lordLabel2.setVisible(false);
                timeLabel.setVisible(false);
            }

            // 点击不抢地主
            if (e.getSource().equals(lordLabel2)) {
                countThread.setRun(false);
                isLord = false;
                lordLabel1.setVisible(false);
                lordLabel2.setVisible(false);
                timeLabel.setVisible(false);
            }

            // 点击出牌
            if (e.getSource().equals(chuPaiJLabel)) {
                PokerType pokerType = PokerRule.checkPokerType(selectedPokerLabels);

                // 判断牌型
                if (!pokerType.equals(PokerType.TYPE_ERROR)) {
                    System.out.println(prevPlayerId + ", " + currentPlayer.getId());

                    if (prevPlayerId == -1 || prevPlayerId == currentPlayer.getId()
                            || PokerRule.isBigger(showOutPokerLabels, selectedPokerLabels)) {
                        isOut = true;
                        chuPaiThread.setRun(false);
                        chuPaiJLabel.setVisible(false);
                        buChuJLabel.setVisible(false);
                        timeLabel.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "请按规则出牌");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "不符合牌型");
                }

            }

            // 点击不出牌
            if (e.getSource().equals(buChuJLabel)) {
                isOut = false;
                chuPaiThread.setRun(false);
                chuPaiJLabel.setVisible(false);
                buChuJLabel.setVisible(false);
                timeLabel.setVisible(false);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    class PokerEvent implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            PokerLabel pokerLabel = (PokerLabel)e.getSource();

            //如果之前选择过了 则取消选择(设置选中属性为false 位置回到原位，从选择的扑克牌列表中移除)
            if (pokerLabel.isSelected()) {
                pokerLabel.setSelected(false);
                pokerLabel.setLocation(pokerLabel.getX(), pokerLabel.getY()+30);
                selectedPokerLabels.remove(pokerLabel);
            } else {
                pokerLabel.setSelected(true);
                pokerLabel.setLocation(pokerLabel.getX(), pokerLabel.getY() - 30);
                selectedPokerLabels.add(pokerLabel);
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}

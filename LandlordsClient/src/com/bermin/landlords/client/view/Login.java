package com.bermin.landlords.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

public class Login extends  JFrame{
    private JLabel unameJLabel;
    private JTextField unameJText;
    private JButton  loginJButton;
    private JButton cancelJButton;

    public Login() {
		 //创建组件对象
        this.unameJLabel = new JLabel("用户名：");
        this.unameJText = new JTextField();
        this.loginJButton = new JButton("登录");
        this.cancelJButton = new JButton("取消");

		 //设置窗口属性 
        this.setSize(400, 300);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new GridLayout(2,2));
		 //添加组件到窗口中
        this.add(unameJLabel);
        this.add(unameJText);
        this.add(loginJButton);
        this.add(cancelJButton);

		 //创建监听器对象 绑定到按钮上
        MyEvent myEvent = new MyEvent();
        this.loginJButton.addActionListener(myEvent);
    }

    // 事件监听器类
    class MyEvent implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // 点击登录
            String uname = unameJText.getText();

            try {
                // 创建Socket连接
                Socket socket = new Socket("127.0.0.1", 8168);
                new MainFrame(uname, socket);

                //dispose();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}

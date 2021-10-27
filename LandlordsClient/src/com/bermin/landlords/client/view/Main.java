package com.bermin.landlords.client.view;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        autoLogin();
        new Login();
    }

    public static void autoLogin() {

        // 自动登录3个用户
        for (int i = 0; i < 3; i++) {
            try {
                String uname = "Guest" + i + 1;
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

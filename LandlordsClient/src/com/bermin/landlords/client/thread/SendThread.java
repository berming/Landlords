package com.bermin.landlords.client.thread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SendThread extends Thread {
    private String msg;
    private Socket socket;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public SendThread(Socket socket, String msg) {
        this.msg = msg;
        this.socket = socket;
    }

    public SendThread(Socket socket) {
        this.socket = socket;
    }

    public SendThread() {
    }

    public void run() {
        DataOutputStream dataOutputStream;

        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (true) {
                if (msg != null) {
				    System.out.println("消息在发送中:"+msg);
                    dataOutputStream.writeUTF(msg);
                    msg = null;
                }

			    Thread.sleep(50);  // 暂停 等待新消息进来
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}

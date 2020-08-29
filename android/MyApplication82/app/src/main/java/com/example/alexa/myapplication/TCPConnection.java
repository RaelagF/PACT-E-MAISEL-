package com.example.alexa.myapplication;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TCPConnection {
    private String host = "35.189.106.175";
    private int port = 50000;
    private Socket s = null;
    private InputStream is = null;
    private InputStreamReader isr = null;
    private BufferedReader br = null;
    private OutputStream os = null;
    private String response = null;

    public TCPConnection()
    {
        try {
            s = new Socket(host, port);
            this.send("client\n");
            this.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg)
    {
        try {
            os = s.getOutputStream();
            os.write(msg.getBytes("utf-8"));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive()
    {
        try {
            is = s.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            response = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public void closeConnection()
    {
        try {
            br.close();
            os.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.blkcaphax.remotepc.socket;

/**
 * @author narendra
 * @date 03/03/2020
 */

import android.util.Log;

import com.blkcaphax.remotepc.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class RpClient{
    private String ip;
    private int port;
    private Socket socket;
    static BufferedReader bufferedReader = null;
    static BufferedWriter bufferedWriter = null;
    private boolean isConnecting;
    private boolean isConnected;

    public RpClient(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.isConnecting = false;
        this.isConnected = false;
    }

    /**
     * Send a String data to host network
     * @param str
     */
    public void send(String str){
        if(RpClient.bufferedWriter != null){
            new Communicate(str).start();
        }else{
            Log.d("", "send: writer null");
            this.connect();
        }
    }

    /**
     * Close connection
     * @param
     * @return void
     */
    public void close(){
        try {
            RpClient.bufferedReader.close();
            RpClient.bufferedWriter.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to the host network
     * @return void
     */
    public void connect(){
        final RpClient rpClient = this;
        if(this.isConnecting == false) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (rpClient) {
                        rpClient.isConnecting = true;
                        try {
                            Log.d("check", "check: connecting");
                            SocketAddress socketAddress = new InetSocketAddress(rpClient.ip,rpClient.port);
                            rpClient.socket = new Socket();
                            rpClient.socket.connect(socketAddress,1000);
                            rpClient.socket.setSoTimeout(5000);
                            rpClient.isConnected = true;
                            Log.d("check", "check: connected");
                            if (rpClient.socket != null) {
                                RpClient.bufferedWriter = new BufferedWriter(new OutputStreamWriter(rpClient.socket.getOutputStream()));
                                RpClient.bufferedReader = new BufferedReader(new InputStreamReader(rpClient.socket.getInputStream()));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("check", "check: timeout");
                        }
                    }
                    rpClient.isConnecting = false;
                }
            }).start();
        }else{
            Log.d("check", "connect: already connecting");
        }
    }

    /**
     * Communicate to host server
     */
    class Communicate extends Thread{
        private String str;
        Communicate(String str){
            this.str = str;
        }
        @Override
        public void run() {
            synchronized (this) {
                this.send(this.str);
            }
            super.run();
        }

        private void send(String str){
            try {
                str+="\n";
                RpClient.bufferedWriter.write(str);
                RpClient.bufferedWriter.flush();
                String str2 = RpClient.bufferedReader.readLine();
                Log.d("sending", "send: " +str2);
                MainActivity.mainActivity.updateUi(str2);
            } catch (IOException e) {
                Log.d("connection closed", "send: ");
                RpClient.this.isConnected = false;
                //e.printStackTrace();
            }
        }
    }

}
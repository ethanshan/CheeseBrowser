package net.codingpark.cheesebrowser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Stack;

/**
 * Created by ethanshan on 9/2/14.
 * Listening the PORT, interact with the client.
 * Used to obtain startup/shutdown schedule time, and
 * receive shutdown/reboot command
 */
public class ControlClient extends Service {

    private static final String TAG     = "ControlClient";

    private static int SERVER_PORT      = 8888;

    /**
     * A stack that contains all connected control clients.
     */
    private Stack<Socket> clients = new Stack<Socket>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting control client.");
        syncConfig();
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }


    private void syncConfig() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Socket socket = null;
                BufferedReader br = null;
                PrintWriter pw = null;
                try {
                    // Create socket to connect server
                    socket = new Socket("192.168.1.160", SERVER_PORT);
                    System.out.println("Socket=" + socket);
                    br = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                    //pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    //socket.getOutputStream())));
                    String msg = br.readLine();
                    Log.d(TAG, "Receive message: \n" + msg);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        System.out.println("close......");
                        if (br !=  null)
                            br.close();
                        //pw.close();
                        if (socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
}

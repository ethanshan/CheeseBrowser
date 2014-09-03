package net.codingpark.cheesebrowser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
public class ControlServer extends Service {

    private static final String TAG     = "ControlServer";

    private static int SERVER_PORT      = 5678;

    /**
     * A stack that contains all connected control clients.
     */
    private Stack<Socket> clients = new Stack<Socket>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting control server.");
        try {
            setupServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Sets up the TCP/IP server socket.
     */
    private void setupServer() throws IOException {
        Log.d(TAG, "Setting up server...");
        final ServerSocket socket = new ServerSocket(SERVER_PORT);
        Log.i(TAG, "Listening on " + socket.getInetAddress().getHostAddress());
        Thread connectionThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Socket client = socket.accept();
                        Log.d(TAG, "Client connected");
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(client.getInputStream()));
                        DataOutputStream out = new DataOutputStream(client.getOutputStream());
                        String str = in.readLine();
                        do {
                            Log.d(TAG, "Received: " + str);
                            out.write("OK OK OK".getBytes(Charset.forName("US-ASCII")));
                            out.flush();
                        } while( (str = in.readLine()) != null);
                        in.close();
                        out.close();
                    } catch ( Exception ex ) {
                        Log.e(TAG, "Error accepting connection: " + ex.getMessage());

                    }
                }
            }
        });
        connectionThread.start();

        Log.d(TAG, "Finished setting up server...");
    }
}

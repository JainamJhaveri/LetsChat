package com.example.digvijay.letschat;

/**
 * Created by Digvijay on 03-04-2016.
 */
public class SocketHandler {
    private static com.github.nkzawa.socketio.client.Socket mSocket;

    public static synchronized void setSocket(com.github.nkzawa.socketio.client.Socket socket){
        SocketHandler.mSocket = socket;
    }

    public static synchronized com.github.nkzawa.socketio.client.Socket getSocket(){
        return mSocket;
    }
}

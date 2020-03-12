package com.org.robocrowd.detection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {

    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final BufferedReader in;
    private final BufferedWriter out;
    
    public String coms = "Null";

        public TCPConnection(TCPConnectionListener eventListener, String ip, int port) throws IOException {
            this(eventListener, new Socket(ip,port));
        }
        public TCPConnection(final TCPConnectionListener eventListener, Socket socket)throws IOException {


            this.socket =socket;
            this.eventListener=eventListener;

            in =new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));

            out =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

            rxThread=new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        eventListener.onConnectionReady(TCPConnection.this);
                        while (!rxThread.isInterrupted()){
                           // eventListener.onReceiveString(TCPConnection.this, in.readLine());
                            
                            coms = in.readLine();
                        }

                    } catch (IOException e) {

                                eventListener.onExeption(TCPConnection.this,e);

                    }finally {
                                eventListener.onDisconnect(TCPConnection.this);

                    }


                }
            });
            rxThread.start();

        }

        public synchronized  void sendMessage(String value){
            try {
                out.write(value+"\r\n");
                out.flush();
            } catch (IOException e) {
                eventListener.onExeption(TCPConnection.this, e);
                setDissconnect();
            }
        }
        
        public synchronized String getMessage(){
            String value = "Null";
        	try {
                 value = in.readLine();
               
            } catch (IOException e) {
                eventListener.onExeption(TCPConnection.this, e);
                setDissconnect();
            }
        	return value;
        }

        public synchronized  void setDissconnect(){
            rxThread.interrupt();
            try {
                socket.close();
            } catch (IOException e) {
                eventListener.onExeption(TCPConnection.this, e);
            }

        }

    @Override
    public String toString() {
        return "TCPConnection: "+socket.getInetAddress()+" port: "+socket.getPort();
    }
}
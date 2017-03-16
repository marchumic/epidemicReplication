package Client;

import CoreLayer.CoreLayerProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Created by Marchu on 4/1/17.
 */
public class Client {


    public static void enviaString(int myPort, String line, DatagramSocket socket, int port) throws Exception{

        InetAddress hostAddress = InetAddress.getByName("localhost");

        String msg = String.valueOf(myPort) + "-" + line;
        System.out.println(msg);

        DatagramPacket datagram = new DatagramPacket(msg.getBytes(), msg.length(), hostAddress, (port+9000));
        socket.send(datagram);

    }

    public static void esperaAck(DatagramSocket socket) throws Exception{

        byte buf[] = new byte[1000];

        //Rebem el ACK del selver conforme ja han tractat el misssatge
        DatagramPacket datagram = new DatagramPacket(buf, 1000);
        socket.receive(datagram);

        //System.out.println("Missatge Rebut: " + new String(datagram.getData(), 0, datagram.getLength()));

    }

    public static void main(String args[]) throws Exception {

        DatagramSocket socket = new DatagramSocket(9500);

        try(BufferedReader br = new BufferedReader(new FileReader("client.txt"))) {

            String line = "";

            while (line != null) {
                line = br.readLine();
                if(line != null) {
                    //System.out.println(line);
                    int randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1); //+1 per fer'ho inclusiu
                    enviaString(9500, line, socket, randomNum); //Enviem els string a una adreça random
                    for(int i = 0; i < 3; i++){
                        esperaAck(socket);
                    }
                }
            }
        }
    }

}

package Utils;

import sun.jvm.hotspot.debugger.posix.DSO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Marchu on 4/1/17.
 */
public class Mensaje extends Thread{

    public Mensaje(){

    }

    public DatagramPacket readMessage(DatagramSocket dSocket) throws Exception{
        byte buf[] = new byte[1000];
        DatagramPacket datagram = new DatagramPacket(buf, 1000);
        dSocket.receive(datagram);

        //System.out.println("He rebut: " + new String(datagram.getData(), 0, datagram.getLength()));

        return datagram;
    }

    public int tractaMissatge(DatagramPacket datagram, FileWriter writer, BufferedWriter bw, PrintWriter out, int arrayValors[], int versio) throws Exception{

        String msg;
        String parts[];
        Fitxer file = new Fitxer();

        //Fem split del missatge
        msg = new String(datagram.getData(), 0, datagram.getLength());
        parts = msg.split("-");

        switch (parts[0]){
            case "read":
                //System.out.println("Valor llegit: " + arrayValors[posicioArray]);
                break;
            case "update":
                //Faig el update de la variable que m'arriba per fer l'update
                for(int i = 1; i < parts.length; i++){
                    arrayValors[i-1] = Integer.parseInt(parts[i]);
                }
                file.actualitzaFitxer(writer, bw, out, arrayValors, versio);
                versio++;
                break;
            default:
                System.out.println("Missatge mal format: " + msg);
                break;
        }

        return versio;
    }
}

package SecondLayer;

import Utils.Fitxer;
import Utils.Mensaje;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Marchu on 5/1/17.
 */
public class SecondLayerProcess extends Thread{

    private int id;
    private int myPort;
    private int versio;
    private FileWriter writer;
    private BufferedWriter bw;
    private PrintWriter out;
    private DatagramSocket dSocketSecondLayer;
    private int arrayValors[];
    private Mensaje msg;

    public SecondLayerProcess(int myPort, int id) throws Exception{
        this.id = id;
        this.myPort = myPort;
        this.versio = 0;

        arrayValors = new int[25];
        msg = new Mensaje();
        dSocketSecondLayer = new DatagramSocket(myPort);

        writer = new FileWriter("SecondLayerC" + id + ".txt", false); //Obro i tanco el fitxer per borrarlo al inici
        writer.close();

        this.start();
    }

    public void run(){

        DatagramPacket datagram;

        while(true){
            try {

                writer = new FileWriter("SecondLayerC" + id + ".txt", true);
                bw = new BufferedWriter(writer);
                out = new PrintWriter(bw);

                datagram = msg.readMessage(dSocketSecondLayer);
                System.out.println("Soc Layer 2, he rebut un missatge");
                versio = msg.tractaMissatge(datagram,writer, bw, out, arrayValors, versio);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

package FirstLayer;

import Utils.Fitxer;
import Utils.Mensaje;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Marchu on 5/1/17.
 */
public class FirstLayerProcess extends Thread{

    private int id;
    private int myPort;
    private int versio;
    private FileWriter writer;
    private BufferedWriter bw;
    private PrintWriter out;
    private DatagramSocket dSocketFirstLayer;
    private int arrayValors[];
    private Mensaje msg;
    private int secondLayerPort;

    public FirstLayerProcess(int myPort, int id, int secondLayerPort) throws Exception{
        this.id = id;
        this.myPort = myPort;
        this.versio = 0;
        this.secondLayerPort = secondLayerPort;

        arrayValors = new int[25];
        msg = new Mensaje();
        dSocketFirstLayer = new DatagramSocket(myPort);

        writer = new FileWriter("FirstLayerB" + id + ".txt", false); //Obro i tanco el fitxer per borrarlo al inici
        writer.close();

        this.start();
    }

    public  void run(){

        DatagramPacket datagram;
        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, 10000);

        while(true){
            try {

                writer = new FileWriter("FirstLayerB" + id + ".txt", true);
                bw = new BufferedWriter(writer);
                out = new PrintWriter(bw);

                datagram = msg.readMessage(dSocketFirstLayer);
                versio = msg.tractaMissatge(datagram,writer, bw, out, arrayValors, versio);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class SayHello extends TimerTask implements Runnable{

        public void sendSeconLayer() {

            try {
                String mensaje = "update-";

                for (int i = 0; i < 25; i++) {
                    if (i == 25 - 1) {
                        mensaje = mensaje + arrayValors[i];
                    } else {
                        mensaje = mensaje + arrayValors[i] + "-";
                    }
                }

                System.out.println("Message: " + mensaje);

                DatagramPacket datagram = new DatagramPacket(mensaje.getBytes(), mensaje.length(), InetAddress.getByName("localhost"), secondLayerPort);
                dSocketFirstLayer.send(datagram);
            }catch(Exception e){
                //Exception Ocurred
            }

        }

        public void run(){
                sendSeconLayer();
        }
    }


}

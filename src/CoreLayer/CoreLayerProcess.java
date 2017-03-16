package CoreLayer;

import Utils.Fitxer;
import Utils.Mensaje;
import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Marchu on 3/1/17.
 */
public class CoreLayerProcess extends  Thread{

    private int id;
    private int myPort;
    private int valorFirstLayer;
    private int portFirstLayer;
    private DatagramSocket dSocketCore;
    private Mensaje msg;
    private Fitxer file;
    private FileWriter writer;
    private BufferedWriter bw;
    private PrintWriter out;
    private int versio;

    private int arrayValors[];

    public CoreLayerProcess(int myPort, int id, int portFirstLayer) throws  Exception{
        this.id = id;
        this.versio = 0;
        this.myPort = myPort;
        this.valorFirstLayer = 0;
        this.portFirstLayer = portFirstLayer;
        arrayValors = new int[25];
        dSocketCore = new DatagramSocket(myPort);
        msg = new Mensaje();
        file = new Fitxer();

        writer = new FileWriter("CoreA" + id + ".txt", false); //Obro i tanco el fitxer per borrarlo al inici
        writer.close();

        this.start();
    }

    //Fem brodcast del nous valors als altres nodes
    public void actualitzaValorsNodes(int posicioArray, int valor, int portClient) throws Exception{

        InetAddress hostAddress = InetAddress.getByName("localhost");

        String msg = String.valueOf(portClient) + "-update-" + posicioArray + "-" + valor;

        for(int i= 9000; i < 9003; i++) {
            if(this.myPort != i) {
                DatagramPacket datagram = new DatagramPacket(msg.getBytes(), msg.length(), hostAddress, i);
                dSocketCore.send(datagram);
            }
        }
    }

    public void checkUpdateFirstLayer() throws Exception{

        InetAddress hostAddress = InetAddress.getByName("localhost");

        if(this.id == 1 || this.id == 2) {
            if (this.valorFirstLayer == 10) {
                this.valorFirstLayer = 0;

                String mensaje = "update-";

                for(int i = 0; i < 25; i++){
                    if(i==25-1){
                        mensaje = mensaje + arrayValors[i];

                        out.println(arrayValors[i]);
                    }else{
                        mensaje = mensaje + arrayValors[i] + "-";
                    }
                }

                System.out.println("Message: " + mensaje);

                DatagramPacket datagram = new DatagramPacket(mensaje.getBytes(), mensaje.length(), hostAddress, portFirstLayer);
                dSocketCore.send(datagram);
            }
        }

    }

    public  void enviaAck(int portClient) throws Exception{

        InetAddress hostAddress = InetAddress.getByName("localhost");

        String msg = "ack-" + this.myPort;

        System.out.println("Envio: \"" + msg + "\" cap al port: " + portClient);

        //Enviem el ACK cap al client, per confirmar que hem rebut el missatge
        DatagramPacket datagram = new DatagramPacket(msg.getBytes(), msg.length(), hostAddress, portClient);
        dSocketCore.send(datagram);

    }

    public void tractaMissatge(DatagramPacket datagram) throws Exception{

        int posicioArray;
        int valor = 0;
        int portClient = 0;
        String msg;
        String parts[];

        //Fem split del missatge
        msg = new String(datagram.getData(), 0, datagram.getLength());

        System.out.println("Missatge: " + msg);
        parts = msg.split("-");

        portClient = Integer.parseInt(parts[0]);
        msg = parts[1];

        posicioArray = Integer.parseInt(parts[2]);
        if(parts.length == 4) {
            valor = Integer.parseInt(parts[3]);
        }

        switch (msg){
            case "read":
                //System.out.println("Valor llegit: " + arrayValors[posicioArray]);
                break;
            case "write":
                arrayValors[posicioArray] = valor;
                actualitzaValorsNodes(posicioArray, valor, portClient); //Actualitzem els valors als nodes de la CoreLayer
                file.actualitzaFitxer(writer, bw, out, arrayValors, versio);
                enviaAck(portClient);
                this.versio++;
                this.valorFirstLayer++;
                break;
            case "update":
                //Faig el update de la variable que m'arriba per fer l'update
                arrayValors[posicioArray] = valor;
                file.actualitzaFitxer(writer, bw, out, arrayValors, versio);
                enviaAck(portClient);
                this.versio++;
                this.valorFirstLayer++;
                break;
            default:
                System.out.println("Missatge mal format: " + msg);
                break;
        }

        //Nomes podem fer una opcio per volta(missatge), aixi segur que enviem quan toca
        checkUpdateFirstLayer();
    }

    public  void run(){

        DatagramPacket datagram;

        while(true){
            try {
                writer = new FileWriter("CoreA" + id + ".txt", true);
                bw = new BufferedWriter(writer);
                out = new PrintWriter(bw);

                datagram = msg.readMessage(dSocketCore);
                tractaMissatge(datagram);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

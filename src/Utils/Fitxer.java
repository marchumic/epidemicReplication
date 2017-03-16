package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Created by Marchu on 5/1/17.
 */
public class Fitxer {

    public Fitxer(){

    }

    //ACtualitzem el fitxer amb la ultima versió de la informació
    public void actualitzaFitxer(FileWriter writer, BufferedWriter bw, PrintWriter out, int arrayValors[], int versio) throws Exception{

        out.println("\n\t\t\t\t\t\tVERSIO: "+ versio);
        for(int i = 0; i < 25; i++){
            if(i==25-1){
                out.println(arrayValors[i]);
            }else{
                out.print(arrayValors[i]+"-");
            }
        }
        out.close();
        bw.close();
        writer.close();

    }
}

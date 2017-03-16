package Main;

import CoreLayer.CoreLayerProcess;
import FirstLayer.FirstLayerProcess;
import SecondLayer.SecondLayerProcess;

import java.awt.*;
import java.nio.file.FileStore;

/**
 * Created by Marchu on 3/1/17.
 */
public class Main {

    private static CoreLayerProcess coreProcs[];
    private static FirstLayerProcess firstLayerProcs[];
    private static SecondLayerProcess secondLayerProcs[];

    public static void main(String args[]) throws Exception {

        coreProcs = new CoreLayerProcess[3];
        firstLayerProcs = new FirstLayerProcess[2];
        secondLayerProcs = new SecondLayerProcess[2];
        for(int i = 0;i < coreProcs.length; i++){
            if(i != 0) {
                coreProcs[i] = new CoreLayerProcess(9000 + i, i, 8000 + i);
            }else{
                coreProcs[i] = new CoreLayerProcess(9000 + i, i, 0);
            }
        }

        firstLayerProcs[0] = new FirstLayerProcess(8001, 0, 7000);
        firstLayerProcs[1] = new FirstLayerProcess(8002, 1, 7001);

        secondLayerProcs[0] = new SecondLayerProcess(7000, 0);
        secondLayerProcs[1] = new SecondLayerProcess(7001, 1);

    }

}

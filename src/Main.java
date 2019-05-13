import gnu.io.SerialPort;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Main {
    static int inc=0;
    static LinkedList<Gesture> history = new LinkedList<>();
    static SerialConnexion myConnexion;

    public static void main(String[] args) {

        myConnexion = new SerialConnexion();


        try {
            myConnexion.connect("COM6");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SettingsChecker sClass = new SettingsChecker();
        Thread settingsThread = new Thread(sClass);
        settingsThread.start();

        do {
            if(myConnexion.dataIncoming()){
                while (myConnexion.dataEnding()){}

                //diff settings
                switch (sClass.mode){
                    case "g":
                        history.add(new Gesture(myConnexion.rawDataArr(),inc + ""));
                        for (Gesture dS : history) {
                            for(int i = 0;i< dS.mySets.size();i++){
                                System.out.println("Data set " + dS.mySets.get(i).myId + " " + dS.mySets.get(i).myData.size());
                            }
                        }
                        inc++;
                        break;
                    case"t":
                        DataSet dataTotest = myConnexion.rawData("testData");

                        double minval = Double.MAX_VALUE;
                        String sel="nothing";

                        for(Gesture dS:history){
                            double comp = dS.readDistance(dataTotest);
                            System.out.println("Data set " + dS.myName + " "+ comp);
                            if(minval> comp){
                                minval=comp;
                                sel = dS.myName;
                            }
                        }

                        System.out.println("matches with gesture " + sel);
                        break;
                    case "r":

                        break;
                }
            }




                /*System.out.println("Enter name");
                String reChoice = scanner.nextLine().trim();
                for(int i = 0;i<history.size();i++){
                    if(history.get(i).myName.equals(reChoice)){
                        System.out.println("reading " + history.get(i).myName);
                        myConnexion.SerialWrite("g");
                        scanner.nextLine().trim();
                        myConnexion.SerialWrite("s");
                        history.get(i).reinforce(myConnexion.rawDataArr());
                        System.out.println("@");
                    }
                }
                for (Gesture dS : history) {
                    for(int i = 0;i< dS.mySets.size();i++){
                        System.out.println("Data set " + dS.mySets.get(i).myId + " " + dS.mySets.get(i).myData.size());
                    }
                }*/

        }while(true);
     //  System.exit(0);
    }

}
class SettingsChecker implements Runnable {
    public String mode;
    Scanner scanner;
    public SettingsChecker(){
        scanner = new Scanner(System.in);
        mode = "g";
    }
    @Override
    public void run() {
        String playerInput;
        do {
            playerInput = scanner.nextLine().trim();
            System.out.println(playerInput);
            switch (playerInput){
                case"g":
                    mode = playerInput;
                    break;
                case"t":
                    mode=playerInput;
                    break;
                case"r":
                    mode=playerInput;
                    break;
            }
            System.out.println(mode);
        } while (true);
    }
}

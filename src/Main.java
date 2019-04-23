import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    static int inc=0;
    static LinkedList<DataSet> history = new LinkedList<>();

    public static void main(String[] args) {
        long lastTime;

        Scanner scanner = new Scanner(System.in);

        SerialConnexion myConnexion = new SerialConnexion();


        try {
            myConnexion.connect("COM3");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String playerInput;
        do {
            playerInput = scanner.nextLine().trim();

            if(playerInput.equals("g")) {
                myConnexion.SerialWrite("g");
                lastTime = System.currentTimeMillis();
                long timeSince;
                do{
                    timeSince = System.currentTimeMillis()-lastTime;
                    System.out.println(5000-timeSince);
                }while (timeSince<5000);
                myConnexion.SerialWrite("s");

                history.add(myConnexion.rawData(inc+""));
                for(DataSet dS:history){
                    System.out.println("Data set " + dS.myName + " "+dS.myLength());
                }
                inc++;
            }else if(playerInput.equals("t")){
                myConnexion.SerialWrite("g");
                lastTime = System.currentTimeMillis();
                long timeSince;
                do{
                    timeSince = System.currentTimeMillis()-lastTime;
                    System.out.println(5000-timeSince);
                }while (timeSince<5000);
                myConnexion.SerialWrite("s");

                DataSet dataTotest = myConnexion.rawData("testData");

                double minval = Double.MAX_VALUE;
                String sel="nothing";

                for(DataSet dS:history){
                    double comp = dS.compareTo(dataTotest);
                    System.out.println("Data set " + dS.myName + " "+ comp);
                    if(minval> comp){
                        minval=comp;
                        sel = dS.myName;
                    }
                }

                System.out.println("matches with dataset " + sel);


            }
        }while(!playerInput.equals("end"));
       System.exit(0);
    }
}

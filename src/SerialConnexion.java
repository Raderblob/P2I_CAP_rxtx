import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class SerialConnexion {

    private int rate = 38400;
    private OutputStream out;
    private SerialReader mysr;

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                //serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                //serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.setSerialPortParams(rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);


                InputStream in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                mysr = new SerialReader(in);

                (new Thread(mysr)).start();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    public DataSet rawData(String name){
        return new DataSet(mysr.getRaw(),name);
    }

    public ArrayList<Data> rawDataArr(){
        return mysr.getRaw();
    }

    public boolean dataIncoming(){
        return mysr.stringLength()>1;
    }
    public boolean dataEnding(){
        return mysr.checkForEnd();
    }



    private class SerialReader implements Runnable {
        private InputStream in;
        private String stringBuffer;
        private boolean reset=false;


        public SerialReader(InputStream in) {
            this.in = in;
            stringBuffer = "";
        }

        public void run() {
            byte[] buffer = new byte[8];
            int len;
            try {
                while ((len = this.in.read(buffer)) > -1) {
                    if (len > 0 ) {
                        String next = new String(buffer, 0, len);
                        stringBuffer += next;
                    }
                    if(reset){
                        stringBuffer="";
                        reset=false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int stringLength(){
            return stringBuffer.length();
        }
        private boolean checkForEnd(){
            return stringBuffer.contains("@");
        }

        private ArrayList<Data> getRaw(){
            String d[] = stringBuffer.split(";");
            String subData[];
            ArrayList<Data> res = new ArrayList<>(d.length);
            for(int i=0;i<d.length;i++){
                subData = d[i].split(",");
                try {
                    res.add(new Data(Double.parseDouble(subData[0]),Double.parseDouble(subData[1]),Double.parseDouble(subData[2])));
                } catch (Exception e) {
                    System.out.println(d[i]);
                }
            }
            reset=true;
            return res;
        }




    }

    public void SerialWrite(String str) {
        byte b[] = str.getBytes();
        try {
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iris;

/**
 *
 * @author DAN
 */
import CSVIO.CSVWriter;
import NeuralNetwork.FeedForwardNeuralNetwork;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVReader {
    private int INPUT_NEURON = 4;
    private int HIDDEN_NEURON = 7;
    private int OUTPUT_NEURON = 3;

    private double[][] bobotIH = new double[INPUT_NEURON][HIDDEN_NEURON];
    private double[][] bobotHO = new double[HIDDEN_NEURON][OUTPUT_NEURON];
    private double[] bobotBiasIH = new double[HIDDEN_NEURON];
    private double[] bobotBiasHO = new double[OUTPUT_NEURON];
    
     public void setBobotIH(double[][] bobotIH) {
        this.bobotIH = bobotIH;
    }

    public void setBobotHO(double[][] bobotHO) {
        this.bobotHO = bobotHO;
    }

    public void setBobotBiasIH(double[] bobotBiasIH) {
        this.bobotBiasIH = bobotBiasIH;
    }

    public void setBobotBiasHO(double[] bobotBiasHO) {
        this.bobotBiasHO = bobotBiasHO;
    }
    
    public void bacaFileTrainingCSV(String csvFile, String mode) {
        //String csvFile = "E:\\MACHINELEARNING\\Iris dataset training.csv";
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        int hitung = 0;
        int MAX_EPOCH = 10000;

        try {       
            FeedForwardNeuralNetwork FFNN = new FeedForwardNeuralNetwork();
            if (mode.equalsIgnoreCase("training")) {
                br = new BufferedReader(new FileReader(csvFile));

                FFNN.generateBobot();

                while ((line = br.readLine()) != null) {
                    hitung++;
                    System.out.println("=========================                "+hitung+"\t ");
                    // use comma as separator
                    String[] data = line.split(csvSplitBy);
                    //System.out.println(data[0] + " \t " + data[1] + " \t "+data[2]+" \t "+data[3]+" \t "+data[4]);

                    //tentukan data yang akan dilatih
                    FFNN.setDataInput(Double.valueOf(data[0]),
                            Double.valueOf(data[1]),
                            Double.valueOf(data[2]),
                            Double.valueOf(data[3]));

                    //tentukan hasil yang diharapkan
                    if (data[4].equalsIgnoreCase("Iris-setosa")){
                        FFNN.setDataTarget(0.49, 0.25, 0.25);
                        FFNN.latih(0);
                    }else if(data[4].equalsIgnoreCase("Iris-versicolor")){
                        FFNN.setDataTarget(0.25, 0.49, 0.25);
                        FFNN.latih(1);
                    }else {
                        FFNN.setDataTarget(0.25, 0.25, 0.49);
                        FFNN.latih(2);
                    }                                            

                    try {
                        //menyimpan bobot hasil pelatihan
                        CSVWriter csvOut = new CSVWriter();
                        csvOut.tulisBobot2D("E:\\MACHINE LEARNING\\bobotIH.csv", FFNN.getBobotIH()); //bobotIH
                        csvOut.tulisBobot2D("E:\\MACHINE LEARNING\\bobotHO.csv", FFNN.getBobotHO()); //bobotHO
                        csvOut.tulisBobot1D("E:\\MACHINE LEARNING\\bobotBiasIH.csv", FFNN.getBobotBiasIH()); //bobotBiasIH
                        csvOut.tulisBobot1D("E:\\MACHINE LEARNING\\bobotBiasHO.csv", FFNN.getBobotBiasHO()); //bobotBiasHO                       
                    } catch (Exception ex) {
                        Logger.getLogger(CSVReader.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }else{
                System.out.println("\n\n==========================================");
                System.out.println("PROSES PENGUJIAN AKURASI");
                
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null) {

                    // use comma as separator
                    String[] data = line.split(csvSplitBy);                    
                    
                    //input data yang akan ditest
                    FFNN.setDataInput(Double.valueOf(data[0]),
                            Double.valueOf(data[1]),
                            Double.valueOf(data[2]),
                            Double.valueOf(data[3]));
                    
                    //sebelum testing, pasang dulu bobot yang telah dilatih
                        FFNN.setBobotBiasHO(bobotBiasHO);
                        FFNN.setBobotBiasIH(bobotBiasIH);
                        FFNN.setBobotHO(bobotHO);
                        FFNN.setBobotIH(bobotIH);
                                       

                    if (data[4].equalsIgnoreCase("Iris-setosa")){                                                
                        FFNN.testing(0);                        
                    }else if(data[4].equalsIgnoreCase("Iris-versicolor")){
                        FFNN.testing(1);
                    }else {
                        FFNN.testing(2);
                    }
                }
                
//                System.out.println("bobot yang digunakan");
//                for (int i = 0; i < bobotIH.length; i++) {
//                    for (int h = 0; h < bobotIH[0].length; h++) {
//                        System.out.print(bobotIH[i][h]+" \t");
//                    }
//                    System.out.println("");
//                }     
            }            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
    * @param    lokasiFile = lokasi file CSV berisi array 1 dimensi (bobot bias)
    * @return   nilai bobot dalam tipe data double
    */
    public double[] bacaFileBobotBias(String lokasiFile){
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";

        try {
            br = new BufferedReader(new FileReader(lokasiFile));           
            
            while ((line = br.readLine()) != null) {                
                String data[] = line.split(csvSplitBy);
                double dataValue[] = new double[data.length];

                for (int i = 0; i < data.length; i++) {
                    dataValue[i] = Double.valueOf(data[i]);
                }

                return dataValue;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return null;
    }
    
    /**
    * @param    lokasiFile = lokasi file CSV berisi array 1 dimensi (bobot bias)
    * @return   nilai bobot dalam tipe data double
    */
    public double[][] bacaFileBobot2D(String lokasiFile, int input, int kolom){
        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        double dataValue[][] = new double[input][kolom];
        try {
            br = new BufferedReader(new FileReader(lokasiFile));           
            
            int baris = 0;
            while ((line = br.readLine()) != null) {                
                String data[] = line.split(csvSplitBy);                

                for (int i = 0; i < data.length; i++) {
                    dataValue[baris][i] = Double.valueOf(data[i]);                    
                }

                baris++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return dataValue;
    }
}

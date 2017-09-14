/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NeuralNetwork;

import iris.Antarmuka;
import iris.CSVReader;

/**
 * Created by DAN on 8/15/2017.
 */
public class FeedForwardNeuralNetwork {
    private int INPUT_NEURON = 4;
    private int HIDDEN_NEURON = 7;
    private int OUTPUT_NEURON = 3;
    private int EPOCH = 100000;
    private static int MAX_SAMPLE = 0;
    
    private double learningRate = 0.05;
    //private double[] dataTraining = {1,0,0,0,1,1};
    //private double[] dataTarget = {0.15,0.15,0.15,0.55};
    private double[] dataInput = new double[INPUT_NEURON];
    private double[] dataTarget = new double[OUTPUT_NEURON];
    private double[][] sampleInput = new double[MAX_SAMPLE][];
    
    //hitung pengenalan benar

    private double[][] bobotIH = new double[INPUT_NEURON][HIDDEN_NEURON];
    private double[][] bobotHO = new double[HIDDEN_NEURON][OUTPUT_NEURON];
    private double[] bobotBiasIH = new double[HIDDEN_NEURON];
    private double[] bobotBiasHO = new double[OUTPUT_NEURON];

    public double[][] getBobotIH() {
        return bobotIH;
    }

    public double[][] getBobotHO() {
        return bobotHO;
    }

    public double[] getBobotBiasIH() {
        return bobotBiasIH;
    }

    public double[] getBobotBiasHO() {
        return bobotBiasHO;
    }

    
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

    private double[] netHidden = new double[HIDDEN_NEURON];
    private double[] aktifHidden = new double[HIDDEN_NEURON];
    private double[] netOutput = new double[OUTPUT_NEURON];
    private double[] aktifOutput = new double[OUTPUT_NEURON];

    private double[] hGrad = new double[HIDDEN_NEURON];
    private double[] oGrad = new double[OUTPUT_NEURON];

    public void setDataInput(double input1, double input2, double input3, double input4){        
        dataInput[0] = input1;
        dataInput[1] = input2;
        dataInput[2] = input3;
        dataInput[3] = input4;
    }
    
    public void setDataTarget(double target1, double target2, double target3){
        dataTarget[0] = target1;
        dataTarget[1] = target2;
        dataTarget[2] = target3;
    }
    
    public void generateBobot(){
        double a = 0.090;
        double b = 0.090;

        for (int i = 0; i < INPUT_NEURON; i++) {
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                a = a + 0.01;
                bobotIH[i][h] = a ;
            }
        }

        //bobot bias input -> hidden
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            bobotBiasIH[h] = a + (h * 0.01);
        }

        for (int h = 0; h < HIDDEN_NEURON; h++) {
            for (int o = 0; o < OUTPUT_NEURON; o++) {
                b = b + 0.01;
                bobotHO[h][o] = b;
            }
        }

        //bobot bias input -> hidden
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            bobotBiasHO[o] = b + (o * 0.01);
        }

    }

    public void feedforward(){
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            double sum = 0.0;
            for (int i = 0; i < INPUT_NEURON; i++) {
                sum = sum + (dataInput[i] * bobotIH[i][h]);
            }
            netHidden[h] = sum + bobotBiasIH[h];
            aktifHidden[h] = Math.tanh(netHidden[h]);
        }

        for (int o = 0; o < OUTPUT_NEURON; o++) {
            double sum = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                sum = sum + (aktifHidden[h] * bobotHO[h][o]);
            }
            netOutput[o] = sum + bobotBiasHO[o];
        }

        //menghitung softmax
        System.out.print("Hasil Output : ");
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            aktifOutput[o] = softmax(netOutput[o]);
            System.out.print(aktifOutput[o]+" \t");
        }
        System.out.println();

    }

    private void hitungError(){
        // /backpropagateError
        //gradien output adalah derivative dari hasil aktivasi neuron output
        System.out.print("ERROR : ");
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            oGrad[o] = (aktifOutput[o]*(1-aktifOutput[o])) * (dataTarget[o] - aktifOutput[o]);
            System.out.print(oGrad[o]+" \t");
        }
        System.out.println();

        //pada hidden layer hGrad = derivative + sum
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            double sum = 0.0;
            double derivative = (1-aktifHidden[h]) * (1+aktifHidden[h]);
            for (int o = 0; o < OUTPUT_NEURON; o++) {
                sum = sum + (bobotHO[h][o] * oGrad[o]);
            }
            hGrad[h] = derivative * sum ;
        }
    }

    private void hitungDelta(){
        for (int i = 0; i < INPUT_NEURON; i++) {
            double delta = 0.0;
            for (int h = 0; h < HIDDEN_NEURON; h++) {
                delta = learningRate * hGrad[h] * dataInput[i] ;
                bobotIH[i][h] = delta + bobotIH[i][h];
            }
        }

        for (int h = 0; h < HIDDEN_NEURON; h++) {
            bobotBiasIH[h] = learningRate * hGrad[h] * 1;
        }

        //Hidden->Output
        for (int h = 0; h < HIDDEN_NEURON; h++) {
            double delta = 0.0;
            for (int o = 0; o < OUTPUT_NEURON; o++) {
                delta = learningRate * oGrad[o] * aktifHidden[h];
                bobotHO[h][o] = delta + bobotHO[h][o];
            }
        }

        for (int o = 0; o < OUTPUT_NEURON; o++) {
            bobotBiasHO[o] = learningRate * oGrad[o] * 1;
        }

    }

    private double softmax(double val){
        double totalExp = 0.0;
        for (int o = 0; o < OUTPUT_NEURON; o++) {
            totalExp = totalExp + Math.exp(netOutput[o]);
        }

        return Math.exp(val)/totalExp;
    }

    public void testing(int indexHasilSeharusnya){        
        String hasil = "";
        
        feedforward();
        //hitung index dengan nilai terbesar
        double max = 0.0;
        int indeksMax = -1;
        for (int i = 0; i < aktifOutput.length; i++) {
            if (aktifOutput[i]> max) {
                max = aktifOutput[i];
                indeksMax = i;
            }
        }
        
        //arti dari index hasil seharusnya
        if (indexHasilSeharusnya == 0) {
            hasil = "Iris-setosa";
        } else if (indexHasilSeharusnya == 1){
            hasil = "Iris-versicolor";
        } else {
            hasil = "Iris-virginica";
        }
        
        if (indeksMax == 0) {
            System.out.println("Hasil Pengenalan = Iris-setosa, Seharusnya = "+hasil);
        }else if(indeksMax == 1){
            System.out.println("Hasil Pengenalan = Iris-versicolor, Seharusnya = "+hasil);
        }else {
            System.out.println("Hasil Pengenalan = Iris-virginica, Seharusnya = "+hasil);
        }
        
//        System.out.println("BOBOT INPUT -> HIDDEN YANG DIGUNAKAN");
//        for (int i = 0; i < INPUT_NEURON; i++) {
//            for (int h = 0; h < HIDDEN_NEURON; h++) {
//                System.out.print(bobotIH[i][h]+"\t");                
//            }            
//            System.out.println("");
//        }
    }
    
    private void simpanBobotBaru(){
        CSVReader csr = new CSVReader();
        csr.setBobotHO(bobotHO);
        csr.setBobotIH(bobotIH);
        csr.setBobotBiasHO(bobotBiasHO);
        csr.setBobotBiasIH(bobotBiasIH);
    }
    
    public void latih(int indexTarget){
        feedforward();
        hitungError();
        hitungDelta();
//        int i = 0;

//        while ((aktifOutput[indexTarget] < 0.49)){
//            i++;
//            System.out.println("=========== Iterasi : "+i+" ========");
//            feedforward();
//            hitungError();
//            hitungDelta();
//            simpanBobotBaru();
//        }
        
        //after the training finisihed pass the value from every weight to the interface class (Antarmuka.java)
        Antarmuka a = new Antarmuka();        
        a.setBobotIH(bobotIH);
    }
    
}




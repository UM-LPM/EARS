package org.um.feri.ears.problems.unconstrained.cec2014;

import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.unconstrained.cec2015.input_data.DataReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public abstract class CEC2014 extends DoubleProblem {

    double[] OShift, M, y, z, x_bound;
    int[] SS;
    int funcNum;

    public CEC2014(int d, int funcNum) {
        super(d, 1, 1, 0);

        if ((funcNum < 1) || (funcNum > 30)) {
            System.err.println("Function number must be between 1 and 30!");
        }
        this.funcNum = funcNum;

        if (!(d == 2 || d == 10 || d == 20 || d == 30 || d == 50 || d == 100)) {
            System.out.println("\nError: CEC2014 test functions are only defined for D=2,10,20,30,50,100.");
        }

        if (d == 2 && ((funcNum >= 17 && funcNum <= 22) || (funcNum >= 29 && funcNum <= 30))) {
            System.out.println("\nError: hf01,hf02,hf03,hf04,hf05,hf06,cf07&cf08 are NOT defined for D=2.\n");
        }

        int cf_num = 10, i, j;

        shortName = "F" + funcNum;

        benchmarkName = "CEC20014";

        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, -100.0));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, 100.0));

        objectiveSpaceOptima[0] = funcNum * 100;

        Locale.setDefault(Locale.US);

        /*Load Matrix M*****************************************************/
        String fn = "/org/um/feri/ears/problems/unconstrained/cec2014/input_data/M_" + funcNum + "_D" + d + ".txt";
        InputStream dataFile = DataReader.class.getResourceAsStream(fn);

        if (dataFile == null) {
            System.out.println("\n Error: Cannot open input file for reading ");
        }

        Scanner input = new Scanner(dataFile);
        input.useLocale(Locale.US);


        if (funcNum < 23) {
            M = new double[d * d];

            for (i = 0; i < d * d; i++) {
                M[i] = input.nextDouble();
            }
        } else {
            M = new double[cf_num * d * d];

            for (i = 0; i < cf_num * d * d; i++) {
                M[i] = input.nextDouble();
            }

        }
        input.close();


        /*Load shift_data***************************************************/

        if (funcNum < 23) {
            fn = "/org/um/feri/ears/problems/unconstrained/cec2014/input_data/shift_data_" + funcNum + ".txt";
            dataFile = DataReader.class.getResourceAsStream(fn);

            if (dataFile == null) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }

            input = new Scanner(dataFile);
            input.useLocale(Locale.US);

            OShift = new double[d];
            for (i = 0; i < d; i++) {
                OShift[i] = input.nextDouble();
                if (OShift == null) {
                    System.out.println("\nError: there is insufficient memory available!");
                }
            }
            input.close();
        } else {

            OShift = new double[d * cf_num];

            try {

                BufferedReader br = new BufferedReader(new InputStreamReader(DataReader.class.getResourceAsStream("/org/um/feri/ears/problems/unconstrained/cec2014/input_data/shift_data_" + funcNum + ".txt")));
                String[] s = new String[100];

                for (i = 0; i < cf_num; i++) {
                    s[i] = br.readLine();
                    String[] array = s[i].split("\\s+");
                    double[] temp = new double[array.length - 1];

                    for (int k = 0; k < array.length - 1; k++) {
                        temp[k] = Double.parseDouble(array[k + 1]);

                    }

                    for (j = 0; j < d; j++) {

                        OShift[i * d + j] = temp[j];
                    }

                }

                br.close();
                //reader.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        input.close();


        /*Load Shuffle_data*******************************************/

        if (funcNum >= 17 && funcNum <= 22) {

            fn = "/org/um/feri/ears/problems/unconstrained/cec2014/input_data/shuffle_data_" + funcNum + "_D" + d + ".txt";
            dataFile = DataReader.class.getResourceAsStream(fn);
            if (dataFile == null) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }
            input = new Scanner(dataFile);
            input.useLocale(Locale.US);

            //SS=(int *)malloc(nx*sizeof(int));
            SS = new int[d];

            for (i = 0; i < d; i++) {
                //fscanf(fpt,"%d",&SS[i]);
                SS[i] = input.nextInt();
            }
        } else if (funcNum == 29 || funcNum == 30) {
            fn = "/org/um/feri/ears/problems/unconstrained/cec2014/input_data/shuffle_data_" + funcNum + "_D" + d + ".txt";
            dataFile = DataReader.class.getResourceAsStream(fn);
            if (dataFile == null) {
                System.out.println("\n Error: Cannot open input file for reading ");
            }
            input = new Scanner(dataFile);
            input.useLocale(Locale.US);


            //SS=(int *)malloc(nx*cf_num*sizeof(int));
            SS = new int[d * cf_num];

            for (i = 0; i < d * cf_num; i++) {
                //fscanf(fpt,"%d",&SS[i]);
                SS[i] = input.nextInt();
            }
        }

        try {
            dataFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        input.close();
        decisionSpaceOptima[0] = OShift;
    }
}

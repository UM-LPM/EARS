package org.um.feri.ears.problems.real_world.cec2011;

public class ProblemsTest {

    public static void main(String[] args) {
        F1 p = new F1();
        double[] x = {1, 1, 1, 1, 1, 1};
        System.out.println("93.1153==" + p.eval(x));
        for (int i = 0; i < 6; i++) {
            System.out.print(ELD_6.Up_Ramp_Limit(i) + " , ");
        }
        System.out.println();
        System.out.println("No_of_POZ_Limits:"
                + ELD_6.noOfPOZLimits());
        for (int j = 0; j < ELD_6.noOfPOZLimits(); j++) {
            for (int i = 0; i < 6; i++) {
                System.out.print(ELD_6
                        .prohibitedOperatingZonesPOZ(j, i) + " , ");
            }
            System.out.println();
        }
        System.out.println();
        // --
        System.out.println("POZ_Lower_Limits:");
        for (int j = 0; j < ELD_6.noOfPOZLimits() / 2; j++) {
            for (int i = 0; i < 6; i++) {
                System.out.print(ELD_6.POZ_Lower_Limits(j, i)
                        + " , ");
            }
            System.out.println();
        }
        System.out.println();// --
        System.out.println("POZ_Upper_Limits:");
        for (int j = 0; j < ELD_6.noOfPOZLimits() / 2; j++) {
            for (int i = 0; i < 6; i++) {
                System.out.print(ELD_6.POZ_Upper_Limits(j, i)
                        + " , ");
            }
            System.out.println();
        }
        System.out.println();
        ELD_6 p11 = new ELD_6();
        x[2] = 222;
        x[3] = 112;
        System.out.println(p11.eval(x));
        //
        System.out.println("No_of_POZ_Limits CEC2011_Problem_11_5_ELD_15:"
                + ELD_15.No_of_POZ_Limits());
        System.out.println("Prohibited_Operating_Zones_POZ!");
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 15; i++) {
                System.out.print(ELD_15
                        .Prohibited_Operating_Zones_POZ(j, i) + " , ");
            }
            System.out.println();
        }

        //
        System.out.println("No_of_POZ_Limits CEC2011_Problem_11_7_ELD_140:"
                + ELD_140.No_of_POZ_Limits());
        System.out.println("Prohibited_Operating_Zones_POZ!");
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 140; i++) {
                System.out.print(ELD_140
                        .Prohibited_Operating_Zones_POZ(j, i) + " , ");
            }
            System.out.println();
        }
    }
}

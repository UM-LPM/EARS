package org.um.feri.ears.problems.unconstrained;

public class TestFunctions {

	public static void main(String[] args) {
		
		Ackley1 p1 = new Ackley1(2);
		System.out.println(p1.getName());
		System.out.println(p1.getGlobalOptimum());
		System.out.println(p1.eval(p1.getOptimalVector()[0]));
		
		Beale p2 = new Beale();
		System.out.println(p2.getName());
		System.out.println(p2.getGlobalOptimum());
		System.out.println(p2.eval(p2.getOptimalVector()[0]));
		
		Bohachevsky1 p3 = new Bohachevsky1();
		System.out.println(p3.getName());
		System.out.println(p3.getGlobalOptimum());
		System.out.println(p3.eval(p3.getOptimalVector()[0]));
		
		Bohachevsky2 p4 = new Bohachevsky2();
		System.out.println(p4.getName());
		System.out.println(p4.getGlobalOptimum());
		System.out.println(p4.eval(p4.getOptimalVector()[0]));
		
		Bohachevsky3 p5 = new Bohachevsky3();
		System.out.println(p5.getName());
		System.out.println(p5.getGlobalOptimum());
		System.out.println(p5.eval(p5.getOptimalVector()[0]));
		
		Booth p6 = new Booth();
		System.out.println(p6.getName());
		System.out.println(p6.getGlobalOptimum());
		System.out.println(p6.eval(p6.getOptimalVector()[0]));
		
		Branin1 p7 = new Branin1();
		System.out.println(p7.getName());
		System.out.println(p7.getGlobalOptimum());
		System.out.println(p7.eval(p7.getOptimalVector()[0]));
		
		Colville p8 = new Colville();
		System.out.println(p8.getName());
		System.out.println(p8.getGlobalOptimum());
		System.out.println(p8.eval(p8.getOptimalVector()[0]));
		
		DixonPrice p9 = new DixonPrice(2);
		System.out.println(p9.getName());
		System.out.println(p9.getGlobalOptimum());
		System.out.println(p9.eval(p9.getOptimalVector()[0]));
		
		Easom p10 = new Easom();
		System.out.println(p10.getName());
		System.out.println(p10.getGlobalOptimum());
		System.out.println(p10.eval(p10.getOptimalVector()[0]));
		
		FletcherPowell2 p11 = new FletcherPowell2();
		System.out.println(p11.getName());
		System.out.println(p11.getGlobalOptimum());
		System.out.println(p11.eval(p11.getOptimalVector()[0]));
		
		FletcherPowell5 p12 = new FletcherPowell5();
		System.out.println(p12.getName());
		System.out.println(p12.getGlobalOptimum());
		System.out.println(p12.eval(p12.getOptimalVector()[0]));
		
		FletcherPowell10 p13 = new FletcherPowell10();
		System.out.println(p13.getName());
		System.out.println(p13.getGlobalOptimum());
		System.out.println(p13.eval(p13.getOptimalVector()[0]));
		
		Foxholes p14 = new Foxholes();
		System.out.println(p14.getName());
		System.out.println(p14.getGlobalOptimum());
		System.out.println(p14.eval(p14.getOptimalVector()[0]));
		
		GoldsteinPrice p15 = new GoldsteinPrice();
		System.out.println(p15.getName());
		System.out.println(p15.getGlobalOptimum());
		System.out.println(p15.eval(p15.getOptimalVector()[0]));
		
		Griewank p16 = new Griewank(2);
		System.out.println(p16.getName());
		System.out.println(p16.getGlobalOptimum());
		System.out.println(p16.eval(p16.getOptimalVector()[0]));
		
		Hartman3 p17 = new Hartman3();
		System.out.println(p17.getName());
		System.out.println(p17.getGlobalOptimum());
		System.out.println(p17.eval(p17.getOptimalVector()[0]));
		
		Hartman6 p18 = new Hartman6();
		System.out.println(p18.getName());
		System.out.println(p18.getGlobalOptimum());
		System.out.println(p18.eval(p18.getOptimalVector()[0]));
		
		Kowalik p19 = new Kowalik();
		System.out.println(p19.getName());
		System.out.println(p19.getGlobalOptimum());
		System.out.println(p19.eval(p19.getOptimalVector()[0]));
		
		ModifiedLangermann2 p20 = new ModifiedLangermann2();
		System.out.println(p20.getName());
		System.out.println(p20.getGlobalOptimum());
		System.out.println(p20.eval(p20.getOptimalVector()[0]));
		double[] sol = {2.00299219,1.006096};
		System.out.println(p20.eval(sol));
		
		ModifiedLangermann5 p21 = new ModifiedLangermann5();
		System.out.println(p21.getName());
		System.out.println(p21.getGlobalOptimum());
		System.out.println(p21.eval(p21.getOptimalVector()[0]));
		
		/*Langerman10 p22 = new Langerman10();
		System.out.println(p22.getName());
		System.out.println(p22.getOptimumEval());
		System.out.println(p22.eval(p22.getOptimalVector()[0]));*/
		
		Matyas p23 = new Matyas();
		System.out.println(p23.getName());
		System.out.println(p23.getGlobalOptimum());
		System.out.println(p23.eval(p23.getOptimalVector()[0]));
		
		Michalewicz2 p24 = new Michalewicz2();
		System.out.println(p24.getName());
		System.out.println(p24.getGlobalOptimum());
		System.out.println(p24.eval(p24.getOptimalVector()[0]));
		
		Michalewicz5 p25 = new Michalewicz5();
		System.out.println(p25.getName());
		System.out.println(p25.getGlobalOptimum());
		System.out.println(p25.eval(p25.getOptimalVector()[0]));
		
		Michalewicz10 p26 = new Michalewicz10();
		System.out.println(p26.getName());
		System.out.println(p26.getGlobalOptimum());
		System.out.println(p26.eval(p26.getOptimalVector()[0]));
		
		Penalized p27 = new Penalized(2);
		System.out.println(p27.getName());
		System.out.println(p27.getGlobalOptimum());
		System.out.println(p27.eval(p27.getOptimalVector()[0]));
		
		Penalized2 p28 = new Penalized2(2);
		System.out.println(p28.getName());
		System.out.println(p28.getGlobalOptimum());
		System.out.println(p28.eval(p28.getOptimalVector()[0]));
		
		Perm1 p29 = new Perm1(2);
		System.out.println(p29.getName());
		System.out.println(p29.getGlobalOptimum());
		System.out.println(p29.eval(p29.getOptimalVector()[0]));
		
		Powell p30 = new Powell(2);
		System.out.println(p30.getName());
		System.out.println(p30.getGlobalOptimum());
		System.out.println(p30.eval(p30.getOptimalVector()[0]));
		
		PowerSum p31 = new PowerSum();
		System.out.println(p31.getName());
		System.out.println(p31.getGlobalOptimum());
		System.out.println(p31.eval(p31.getOptimalVector()[0]));
		
		Quartic p32 = new Quartic(2);
		System.out.println(p32.getName());
		System.out.println(p32.getGlobalOptimum());
		System.out.println(p32.eval(p32.getOptimalVector()[0]));
		
		Rastrigin p33 = new Rastrigin(2);
		System.out.println(p33.getName());
		System.out.println(p33.getGlobalOptimum());
		System.out.println(p33.eval(p33.getOptimalVector()[0]));
		
		RosenbrockDeJong2 p34 = new RosenbrockDeJong2(2);
		System.out.println(p34.getName());
		System.out.println(p34.getGlobalOptimum());
		System.out.println(p34.eval(p34.getOptimalVector()[0]));
		
		Schaffer1 p35 = new Schaffer1();
		System.out.println(p35.getName());
		System.out.println(p35.getGlobalOptimum());
		System.out.println(p35.eval(p35.getOptimalVector()[0]));
		
		Schwefel12 p36 = new Schwefel12();
		System.out.println(p36.getName());
		System.out.println(p36.getGlobalOptimum());
		System.out.println(p36.eval(p36.getOptimalVector()[0]));
		
		Schwefel222 p37 = new Schwefel222(2);
		System.out.println(p37.getName());
		System.out.println(p37.getGlobalOptimum());
		System.out.println(p37.eval(p37.getOptimalVector()[0]));
		
		Schwefel226 p38 = new Schwefel226(6);
		System.out.println(p38.getName());
		System.out.println(p38.getGlobalOptimum());
		System.out.println(p38.eval(p38.getOptimalVector()[0]));
		
		Shekel5 p39 = new Shekel5();
		System.out.println(p39.getName());
		System.out.println(p39.getGlobalOptimum());
		System.out.println(p39.eval(p39.getOptimalVector()[0]));
		
		Shekel7 p40 = new Shekel7();
		System.out.println(p40.getName());
		System.out.println(p40.getGlobalOptimum());
		System.out.println(p40.eval(p40.getOptimalVector()[0]));
		
		Shekel10 p41 = new Shekel10();
		System.out.println(p41.getName());
		System.out.println(p41.getGlobalOptimum());
		System.out.println(p41.eval(p41.getOptimalVector()[0]));
		
		Shubert1 p42 = new Shubert1(2);
		System.out.println(p42.getName());
		System.out.println(p42.getGlobalOptimum());
		System.out.println(p42.eval(p42.getOptimalVector()[0]));
		
		SixHumpCamelBack p43 = new SixHumpCamelBack();
		System.out.println(p43.getName());
		System.out.println(p43.getGlobalOptimum());
		System.out.println(p43.eval(p43.getOptimalVector()[1]));
		
		Sphere p44 = new Sphere(2);
		System.out.println(p44.getName());
		System.out.println(p44.getGlobalOptimum());
		System.out.println(p44.eval(p44.getOptimalVector()[0]));
		
		Step2 p45 = new Step2(2);
		System.out.println(p45.getName());
		System.out.println(p45.getGlobalOptimum());
		System.out.println(p45.eval(p45.getOptimalVector()[0]));
		
		Stepint p46 = new Stepint(2);
		System.out.println(p46.getName());
		System.out.println(p46.getGlobalOptimum());
		System.out.println(p46.eval(p46.getOptimalVector()[0]));
		
		SumSquares p47 = new SumSquares(2);
		System.out.println(p47.getName());
		System.out.println(p47.getGlobalOptimum());
		System.out.println(p47.eval(p47.getOptimalVector()[0]));
		
		Trid6 p48 = new Trid6();
		System.out.println(p48.getName());
		System.out.println(p48.getGlobalOptimum());
		System.out.println(p48.eval(p48.getOptimalVector()[0]));
		
		Trid10 p49 = new Trid10();
		System.out.println(p49.getName());
		System.out.println(p49.getGlobalOptimum());
		System.out.println(p49.eval(p49.getOptimalVector()[0]));
		
		Zakharov p50 = new Zakharov(2);
		System.out.println(p50.getName());
		System.out.println(p50.getGlobalOptimum());
		System.out.println(p50.eval(p50.getOptimalVector()[0]));
		

	}

}

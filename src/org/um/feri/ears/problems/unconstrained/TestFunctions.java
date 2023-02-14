package org.um.feri.ears.problems.unconstrained;

public class TestFunctions {

	public static void main(String[] args) {
		
		Ackley1 p1 = new Ackley1(2);
		System.out.println(p1.getName());
		System.out.println(p1.getGlobalOptima()[0]);
		System.out.println(p1.eval(p1.getDecisionSpaceOptima()[0]));
		
		Beale p2 = new Beale();
		System.out.println(p2.getName());
		System.out.println(p2.getGlobalOptima()[0]);
		System.out.println(p2.eval(p2.getDecisionSpaceOptima()[0]));
		
		Bohachevsky1 p3 = new Bohachevsky1();
		System.out.println(p3.getName());
		System.out.println(p3.getGlobalOptima()[0]);
		System.out.println(p3.eval(p3.getDecisionSpaceOptima()[0]));
		
		Bohachevsky2 p4 = new Bohachevsky2();
		System.out.println(p4.getName());
		System.out.println(p4.getGlobalOptima()[0]);
		System.out.println(p4.eval(p4.getDecisionSpaceOptima()[0]));
		
		Bohachevsky3 p5 = new Bohachevsky3();
		System.out.println(p5.getName());
		System.out.println(p5.getGlobalOptima()[0]);
		System.out.println(p5.eval(p5.getDecisionSpaceOptima()[0]));
		
		Booth p6 = new Booth();
		System.out.println(p6.getName());
		System.out.println(p6.getGlobalOptima()[0]);
		System.out.println(p6.eval(p6.getDecisionSpaceOptima()[0]));
		
		Branin1 p7 = new Branin1();
		System.out.println(p7.getName());
		System.out.println(p7.getGlobalOptima()[0]);
		System.out.println(p7.eval(p7.getDecisionSpaceOptima()[0]));
		
		Colville p8 = new Colville();
		System.out.println(p8.getName());
		System.out.println(p8.getGlobalOptima()[0]);
		System.out.println(p8.eval(p8.getDecisionSpaceOptima()[0]));
		
		DixonPrice p9 = new DixonPrice(2);
		System.out.println(p9.getName());
		System.out.println(p9.getGlobalOptima()[0]);
		System.out.println(p9.eval(p9.getDecisionSpaceOptima()[0]));
		
		Easom p10 = new Easom();
		System.out.println(p10.getName());
		System.out.println(p10.getGlobalOptima()[0]);
		System.out.println(p10.eval(p10.getDecisionSpaceOptima()[0]));
		
		FletcherPowell2 p11 = new FletcherPowell2();
		System.out.println(p11.getName());
		System.out.println(p11.getGlobalOptima()[0]);
		System.out.println(p11.eval(p11.getDecisionSpaceOptima()[0]));
		
		FletcherPowell5 p12 = new FletcherPowell5();
		System.out.println(p12.getName());
		System.out.println(p12.getGlobalOptima()[0]);
		System.out.println(p12.eval(p12.getDecisionSpaceOptima()[0]));
		
		FletcherPowell10 p13 = new FletcherPowell10();
		System.out.println(p13.getName());
		System.out.println(p13.getGlobalOptima()[0]);
		System.out.println(p13.eval(p13.getDecisionSpaceOptima()[0]));
		
		Foxholes p14 = new Foxholes();
		System.out.println(p14.getName());
		System.out.println(p14.getGlobalOptima()[0]);
		System.out.println(p14.eval(p14.getDecisionSpaceOptima()[0]));
		
		GoldsteinPrice p15 = new GoldsteinPrice();
		System.out.println(p15.getName());
		System.out.println(p15.getGlobalOptima()[0]);
		System.out.println(p15.eval(p15.getDecisionSpaceOptima()[0]));
		
		Griewank p16 = new Griewank(2);
		System.out.println(p16.getName());
		System.out.println(p16.getGlobalOptima()[0]);
		System.out.println(p16.eval(p16.getDecisionSpaceOptima()[0]));
		
		Hartman3 p17 = new Hartman3();
		System.out.println(p17.getName());
		System.out.println(p17.getGlobalOptima()[0]);
		System.out.println(p17.eval(p17.getDecisionSpaceOptima()[0]));
		
		Hartman6 p18 = new Hartman6();
		System.out.println(p18.getName());
		System.out.println(p18.getGlobalOptima()[0]);
		System.out.println(p18.eval(p18.getDecisionSpaceOptima()[0]));
		
		Kowalik p19 = new Kowalik();
		System.out.println(p19.getName());
		System.out.println(p19.getGlobalOptima()[0]);
		System.out.println(p19.eval(p19.getDecisionSpaceOptima()[0]));
		
		ModifiedLangermann2 p20 = new ModifiedLangermann2();
		System.out.println(p20.getName());
		System.out.println(p20.getGlobalOptima()[0]);
		System.out.println(p20.eval(p20.getDecisionSpaceOptima()[0]));
		double[] sol = {2.00299219,1.006096};
		System.out.println(p20.eval(sol));
		
		ModifiedLangermann5 p21 = new ModifiedLangermann5();
		System.out.println(p21.getName());
		System.out.println(p21.getGlobalOptima()[0]);
		System.out.println(p21.eval(p21.getDecisionSpaceOptima()[0]));
		
		/*Langerman10 p22 = new Langerman10();
		System.out.println(p22.getName());
		System.out.println(p22.getOptimumEval());
		System.out.println(p22.eval(p22.getDecisionSpaceOptima()[0]));*/
		
		Matyas p23 = new Matyas();
		System.out.println(p23.getName());
		System.out.println(p23.getGlobalOptima()[0]);
		System.out.println(p23.eval(p23.getDecisionSpaceOptima()[0]));
		
		Michalewicz2 p24 = new Michalewicz2();
		System.out.println(p24.getName());
		System.out.println(p24.getGlobalOptima()[0]);
		System.out.println(p24.eval(p24.getDecisionSpaceOptima()[0]));
		
		Michalewicz5 p25 = new Michalewicz5();
		System.out.println(p25.getName());
		System.out.println(p25.getGlobalOptima()[0]);
		System.out.println(p25.eval(p25.getDecisionSpaceOptima()[0]));
		
		Michalewicz10 p26 = new Michalewicz10();
		System.out.println(p26.getName());
		System.out.println(p26.getGlobalOptima()[0]);
		System.out.println(p26.eval(p26.getDecisionSpaceOptima()[0]));
		
		Penalized p27 = new Penalized(2);
		System.out.println(p27.getName());
		System.out.println(p27.getGlobalOptima()[0]);
		System.out.println(p27.eval(p27.getDecisionSpaceOptima()[0]));
		
		Penalized2 p28 = new Penalized2(2);
		System.out.println(p28.getName());
		System.out.println(p28.getGlobalOptima()[0]);
		System.out.println(p28.eval(p28.getDecisionSpaceOptima()[0]));
		
		Perm1 p29 = new Perm1(2);
		System.out.println(p29.getName());
		System.out.println(p29.getGlobalOptima()[0]);
		System.out.println(p29.eval(p29.getDecisionSpaceOptima()[0]));
		
		Powell p30 = new Powell(2);
		System.out.println(p30.getName());
		System.out.println(p30.getGlobalOptima()[0]);
		System.out.println(p30.eval(p30.getDecisionSpaceOptima()[0]));
		
		PowerSum p31 = new PowerSum();
		System.out.println(p31.getName());
		System.out.println(p31.getGlobalOptima()[0]);
		System.out.println(p31.eval(p31.getDecisionSpaceOptima()[0]));
		
		Quartic p32 = new Quartic(2);
		System.out.println(p32.getName());
		System.out.println(p32.getGlobalOptima()[0]);
		System.out.println(p32.eval(p32.getDecisionSpaceOptima()[0]));
		
		Rastrigin p33 = new Rastrigin(2);
		System.out.println(p33.getName());
		System.out.println(p33.getGlobalOptima()[0]);
		System.out.println(p33.eval(p33.getDecisionSpaceOptima()[0]));
		
		RosenbrockDeJong2 p34 = new RosenbrockDeJong2(2);
		System.out.println(p34.getName());
		System.out.println(p34.getGlobalOptima()[0]);
		System.out.println(p34.eval(p34.getDecisionSpaceOptima()[0]));
		
		Schaffer1 p35 = new Schaffer1();
		System.out.println(p35.getName());
		System.out.println(p35.getGlobalOptima()[0]);
		System.out.println(p35.eval(p35.getDecisionSpaceOptima()[0]));
		
		Schwefel12 p36 = new Schwefel12();
		System.out.println(p36.getName());
		System.out.println(p36.getGlobalOptima()[0]);
		System.out.println(p36.eval(p36.getDecisionSpaceOptima()[0]));
		
		Schwefel222 p37 = new Schwefel222(2);
		System.out.println(p37.getName());
		System.out.println(p37.getGlobalOptima()[0]);
		System.out.println(p37.eval(p37.getDecisionSpaceOptima()[0]));
		
		Schwefel226 p38 = new Schwefel226(6);
		System.out.println(p38.getName());
		System.out.println(p38.getGlobalOptima()[0]);
		System.out.println(p38.eval(p38.getDecisionSpaceOptima()[0]));
		
		Shekel5 p39 = new Shekel5();
		System.out.println(p39.getName());
		System.out.println(p39.getGlobalOptima()[0]);
		System.out.println(p39.eval(p39.getDecisionSpaceOptima()[0]));
		
		Shekel7 p40 = new Shekel7();
		System.out.println(p40.getName());
		System.out.println(p40.getGlobalOptima()[0]);
		System.out.println(p40.eval(p40.getDecisionSpaceOptima()[0]));
		
		Shekel10 p41 = new Shekel10();
		System.out.println(p41.getName());
		System.out.println(p41.getGlobalOptima()[0]);
		System.out.println(p41.eval(p41.getDecisionSpaceOptima()[0]));
		
		Shubert1 p42 = new Shubert1(2);
		System.out.println(p42.getName());
		System.out.println(p42.getGlobalOptima()[0]);
		System.out.println(p42.eval(p42.getDecisionSpaceOptima()[0]));
		
		SixHumpCamelBack p43 = new SixHumpCamelBack();
		System.out.println(p43.getName());
		System.out.println(p43.getGlobalOptima()[0]);
		System.out.println(p43.eval(p43.getDecisionSpaceOptima()[1]));
		
		Sphere p44 = new Sphere(2);
		System.out.println(p44.getName());
		System.out.println(p44.getGlobalOptima()[0]);
		System.out.println(p44.eval(p44.getDecisionSpaceOptima()[0]));
		
		Step2 p45 = new Step2(2);
		System.out.println(p45.getName());
		System.out.println(p45.getGlobalOptima()[0]);
		System.out.println(p45.eval(p45.getDecisionSpaceOptima()[0]));
		
		Stepint p46 = new Stepint(2);
		System.out.println(p46.getName());
		System.out.println(p46.getGlobalOptima()[0]);
		System.out.println(p46.eval(p46.getDecisionSpaceOptima()[0]));
		
		SumSquares p47 = new SumSquares(2);
		System.out.println(p47.getName());
		System.out.println(p47.getGlobalOptima()[0]);
		System.out.println(p47.eval(p47.getDecisionSpaceOptima()[0]));
		
		Trid6 p48 = new Trid6();
		System.out.println(p48.getName());
		System.out.println(p48.getGlobalOptima()[0]);
		System.out.println(p48.eval(p48.getDecisionSpaceOptima()[0]));
		
		Trid10 p49 = new Trid10();
		System.out.println(p49.getName());
		System.out.println(p49.getGlobalOptima()[0]);
		System.out.println(p49.eval(p49.getDecisionSpaceOptima()[0]));
		
		Zakharov p50 = new Zakharov(2);
		System.out.println(p50.getName());
		System.out.println(p50.getGlobalOptima()[0]);
		System.out.println(p50.eval(p50.getDecisionSpaceOptima()[0]));
		

	}

}

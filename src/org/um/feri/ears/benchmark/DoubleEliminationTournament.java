package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.EnumStopCriteria;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.StopCriteriaException;
import org.um.feri.ears.problems.moo.DoubleMOProblem;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.qualityIndicator.IndicatorFactory;
import org.um.feri.ears.qualityIndicator.QualityIndicator;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.qualityIndicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.util.PermutationUtility;
import org.um.feri.ears.util.Util;

public class DoubleEliminationTournament {
	
	ArrayList<QualityIndicator> ensemble;
	DoubleMOProblem problem;
	int playerCount;
	HashMap<String, Double> averageRanks;
	ArrayList<MOAlgorithm> players;
	
	public void run(int tournamentSize, ArrayList<IndicatorName> indicators, ArrayList<MOAlgorithm> players, DoubleMOProblem problem, int rep){
		this.problem = problem; 
		playerCount = players.size();
		this.players = players;
		
		fillEnsemble(indicators);
		
		//generate approximations for the tournament
		ArrayList<MOAlgorithmEvalResult> participants = getParticipants(tournamentSize, players);
		fillPlayers(players);
		
		
		startTournament(participants, rep);
	}


	private void fillPlayers(ArrayList<MOAlgorithm> players) {
		
		averageRanks = new HashMap<>();
		
		for (MOAlgorithm moAlgorithm : players) {
			averageRanks.put(moAlgorithm.getID(), 0.0);
		}
		
	}


	private void startTournament(ArrayList<MOAlgorithmEvalResult> participants, int numberOfRepetitions) {
	
		ArrayList<MOAlgorithmEvalResult> participantsCopy = new ArrayList<>(participants);
		for (int i = 0; i < numberOfRepetitions; i++) {
			

			ArrayList<MOAlgorithmEvalResult> ranking = new ArrayList<MOAlgorithmEvalResult>();

			ArrayList<MOAlgorithmEvalResult> win = new ArrayList<MOAlgorithmEvalResult>();
			ArrayList<MOAlgorithmEvalResult> lose = new ArrayList<MOAlgorithmEvalResult>();

			ArrayList<MOAlgorithmEvalResult> win_win = new ArrayList<MOAlgorithmEvalResult>();
			ArrayList<MOAlgorithmEvalResult> lose_win = new ArrayList<MOAlgorithmEvalResult>();
			ArrayList<MOAlgorithmEvalResult> win_lose = new ArrayList<MOAlgorithmEvalResult>();
			ArrayList<MOAlgorithmEvalResult> lose_lose = new ArrayList<MOAlgorithmEvalResult>();

			ArrayList<MOAlgorithmEvalResult> last;
			
			if(participants.size() == 0)
				participants = new ArrayList<>(participantsCopy);
			
			while(participants.size() > 1)
			{
				win = new ArrayList<MOAlgorithmEvalResult>();
				lose = new ArrayList<MOAlgorithmEvalResult>();
				// first round
				setWinLose(participants,win,lose);


				MOAlgorithmEvalResult winner = null;
				while(winner == null)
				{
					//tournament for winners
					setWinLose(win,win_win,lose_win);

					//tournament for losers
					setWinLose(lose,win_lose, lose_lose);

					//eliminate two time losers
					lose_lose.clear();

					// next round winners
					win = win_win;
					win_win = new ArrayList<>();
					//win_win.clear();

					// next round losers
					lose.clear();
					setWinLoseTwoBracket(lose,lose_win,win_lose);
					lose_win.clear();
					win_lose.clear();

					if(win.size() == 1){
						last = new ArrayList<MOAlgorithmEvalResult>();
						setWinLoseTwoBracket(last, win, lose);
						winner = last.get(0);
					}

				}

				ranking.add(winner);
				removeWinner(participants, winner);
			}

			for (int j = 0; j < ranking.size(); j++) {
				averageRanks.put(ranking.get(j).getAl().getID(), averageRanks.get(ranking.get(j).getAl().getID()) + (j+1));
			}
			
			if(ranking.size() != playerCount)
				rankAbsentAlgorithms(ranking);
		}
		
		for(Entry<String, Double> entry : averageRanks.entrySet()) {
		    String name = entry.getKey();
		    Double avgRank = (double) (entry.getValue() / numberOfRepetitions);
		    
		    System.out.println(name+" "+avgRank);
		}
		
		
	}


	private void rankAbsentAlgorithms(ArrayList<MOAlgorithmEvalResult> ranking) {
		
		boolean found;
		int drawNum = playerCount - ranking.size();
		double loseRank = 0;
		for (int i = playerCount; i > (playerCount - drawNum); i--) {
			loseRank+=i;
		}
		loseRank = loseRank / drawNum; 
		
		for (MOAlgorithm player : players) {
			
			found = false;
			for (MOAlgorithmEvalResult alRes : ranking) {
				if(alRes.getAl().getID().equals(player.getID()))
				{
					found = true;
					break;
				}
			}
			
			if(!found)
			{
				averageRanks.put(player.getID(), averageRanks.get(player.getID()) + loseRank);
			}
		}
	}


	private void setWinLoseTwoBracket(ArrayList<MOAlgorithmEvalResult> win,
			ArrayList<MOAlgorithmEvalResult> first, ArrayList<MOAlgorithmEvalResult> second) {

		int[] per1 = (new PermutationUtility()).intPermutation(first.size());
		int[] per2 = (new PermutationUtility()).intPermutation(second.size());

		for(int i = 0; i < per1.length; i++)
		{
			MOAlgorithmEvalResult p1 = first.get(per1[i]);
			MOAlgorithmEvalResult p2 = second.get(per2[i]);
			QualityIndicator qi = getMetric();

			try {
				if(p1.getBest().isFirstBetter(p2.getBest(), qi))
				{
					win.add(p1);
				}
				else
				{
					win.add(p2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	private void setWinLose(ArrayList<MOAlgorithmEvalResult> participants, ArrayList<MOAlgorithmEvalResult> win, ArrayList<MOAlgorithmEvalResult> lose) {
		
		int[] per = (new PermutationUtility()).intPermutation(participants.size());
		int index = 0;
		
		while(index+1 < per.length)
		{
			MOAlgorithmEvalResult p1 = participants.get(per[index]);
			MOAlgorithmEvalResult p2 = participants.get(per[index + 1]);
			QualityIndicator qi = getMetric();
			
			try {
				if(p1.getBest().isFirstBetter(p2.getBest(), qi))
				{
					win.add(p1);
					lose.add(p2);
				}
				else
				{
					win.add(p2);
					lose.add(p1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			index+=2;
		}
		
		// odd number of participants, add last one to win and lose
		if(index < per.length)
		{
			win.add(participants.get(per[index]));
			lose.add(participants.get(per[index]));
		}
		
	}


	private void removeWinner(ArrayList<MOAlgorithmEvalResult> participants, MOAlgorithmEvalResult winner) {

		Iterator<MOAlgorithmEvalResult> i = participants.iterator();

		while (i.hasNext()) {
			MOAlgorithmEvalResult res = i.next(); 
			if(res.getAl().getID().equals(winner.getAl().getID()))
				i.remove();
		}

	}

	private ArrayList<MOAlgorithmEvalResult> getParticipants(int tournamentSize, ArrayList<MOAlgorithm> players) {
		
		
		ArrayList<MOAlgorithmEvalResult> participants = new ArrayList<MOAlgorithmEvalResult>();
		ArrayList<MOAlgorithmEvalResult> results = new ArrayList<MOAlgorithmEvalResult>();
		
		DoubleMOTask task;
		
		for(int i = 0; i < tournamentSize; i++){
			
			//TODO number of evaluations
			// get random quality indicator from ensemble
			QualityIndicator qi = getMetric();
			task = new DoubleMOTask(EnumStopCriteria.EVALUATIONS, 15000, 500, 300, 0.001, problem);
			for (MOAlgorithm al: players) {
				
				ParetoSolution bestByALg = null;
				
				try {
					bestByALg = al.execute(task);
					task.resetCounter();
				} catch (StopCriteriaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				results.add(new MOAlgorithmEvalResult(bestByALg, al)); 
			}
			
			FitnessComparator fc = new FitnessComparator(task, qi);
	        Collections.sort(results, fc); //best first
	        participants.add(results.get(0));
	        results.clear();
		}
		
		return participants;
	}


	private void fillEnsemble(ArrayList<IndicatorName> indicators) {
		ensemble = new ArrayList<QualityIndicator>();
		
		for (IndicatorName name : indicators) {
			ensemble.add(IndicatorFactory.createIndicator(name, problem.getNumberOfObjectives(),problem.getFileName()));
		}
	}
	
	private QualityIndicator getMetric()
	{
		return ensemble.get(Util.rnd.nextInt(ensemble.size()));
	}
	
	class FitnessComparator implements Comparator<MOAlgorithmEvalResult> {
        MOTask t;
        QualityIndicator qi;
        public FitnessComparator(MOTask t, QualityIndicator qi) {
            this.t = t;
            this.qi = qi;
        }
        @Override
        public int compare(MOAlgorithmEvalResult arg0, MOAlgorithmEvalResult arg1) {
            if (arg0.getBest()!=null) {
                if (arg1.getBest()!=null){
                   // if (resultEqual(arg0.getBest(), arg1.getBest())) return 0; Normal sor later!
                	if(qi.getIndicatorType() == IndicatorType.Unary)
                	{
                		try {
							arg0.getBest().evaluate(qi);
							arg1.getBest().evaluate(qi);
						} catch (Exception e) {
							e.printStackTrace();
						}
                	}
                    try {
						if (t.isFirstBetter(arg0.getBest(),arg1.getBest(), qi)) return -1;
						else return 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
                } else return -1; //second is null
            } else
                if (arg1.getBest()!= null) return 1; //first null
            return 0; //both equal
        }
    }

}

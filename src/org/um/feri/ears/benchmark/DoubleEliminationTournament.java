package org.um.feri.ears.benchmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.um.feri.ears.algorithms.MOAlgorithm;
import org.um.feri.ears.problems.DoubleMOTask;
import org.um.feri.ears.problems.DoubleProblem;
import org.um.feri.ears.problems.MOTask;
import org.um.feri.ears.problems.moo.ParetoSolution;
import org.um.feri.ears.quality_indicator.IndicatorFactory;
import org.um.feri.ears.quality_indicator.QualityIndicator;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorName;
import org.um.feri.ears.quality_indicator.QualityIndicator.IndicatorType;
import org.um.feri.ears.util.Util;

public class DoubleEliminationTournament {
	
	ArrayList<QualityIndicator> ensemble;
	DoubleMOTask task;
	int playerCount;
	HashMap<String, Double> averageRanks;
	ArrayList<MOAlgorithm<DoubleProblem, DoubleMOTask, Double>> players;
	
	public void run(int tournamentSize, ArrayList<IndicatorName> indicators, ArrayList<MOAlgorithm<DoubleProblem, DoubleMOTask, Double>> players, DoubleMOTask task, int rep){
		this.task = task; 
		playerCount = players.size();
		this.players = players;
		
		fillEnsemble(indicators);
		
		//generate approximations for the tournament
		ArrayList<MOAlgorithmEvalResult> participants = getParticipants(tournamentSize, players, true);
		fillPlayers(players);
		
		
		startTournament(participants, rep);
	}


	private void fillPlayers(ArrayList<MOAlgorithm<DoubleProblem, DoubleMOTask, Double>> players) {
		
		averageRanks = new HashMap<>();
		
		for (MOAlgorithm<DoubleProblem, DoubleMOTask, Double> moAlgorithm : players) {
			averageRanks.put(moAlgorithm.getId(), 0.0);
		}
		
	}


	private void startTournament(ArrayList<MOAlgorithmEvalResult> participants, int numberOfRepetitions) {
	
		ArrayList<MOAlgorithmEvalResult> participantsCopy = new ArrayList<>(participants);
		for (int i = 0; i < numberOfRepetitions; i++) {
			
			System.out.println("Repetition: "+i);

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
				averageRanks.put(ranking.get(j).getAl().getId(), averageRanks.get(ranking.get(j).getAl().getId()) + (j+1));
			}
			
			if(ranking.size() != playerCount)
				rankAbsentAlgorithms(ranking);
		}
		
		sortByComparator(averageRanks,false);
		
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
				if(alRes.getAl().getId().equals(player.getId()))
				{
					found = true;
					break;
				}
			}
			
			if(!found)
			{
				averageRanks.put(player.getId(), averageRanks.get(player.getId()) + loseRank);
			}
		}
	}


	private void setWinLoseTwoBracket(ArrayList<MOAlgorithmEvalResult> win,
			ArrayList<MOAlgorithmEvalResult> first, ArrayList<MOAlgorithmEvalResult> second) {


		int[] per1 = Util.randomPermutation(first.size());
		int[] per2 = Util.randomPermutation(second.size());

		for(int i = 0; i < per1.length; i++)
		{
			MOAlgorithmEvalResult p1 = first.get(per1[i]);
			MOAlgorithmEvalResult p2 = second.get(per2[i]);
			QualityIndicator qi = getMetric();
			
			try {
				p1.getBest().evaluate(qi);
				p2.getBest().evaluate(qi);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
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
		
		int[] per = Util.randomPermutation(participants.size());
		int index = 0;
		
		while(index+1 < per.length)
		{
			MOAlgorithmEvalResult p1 = participants.get(per[index]);
			MOAlgorithmEvalResult p2 = participants.get(per[index + 1]);
			QualityIndicator qi = getMetric();
			

			try {
				p1.getBest().evaluate(qi);
				p2.getBest().evaluate(qi);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
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
			if(res.getAl().getId().equals(winner.getAl().getId()))
				i.remove();
		}

	}

	private ArrayList<MOAlgorithmEvalResult> getParticipants(int tournamentSize, ArrayList<MOAlgorithm<DoubleProblem, DoubleMOTask, Double>> players, boolean preliminaries) {
		
		
		ArrayList<MOAlgorithmEvalResult> participants = new ArrayList<MOAlgorithmEvalResult>();
		ArrayList<MOAlgorithmEvalResult> results = new ArrayList<MOAlgorithmEvalResult>();
		
		if(preliminaries)
		{
			for(int i = 0; i < tournamentSize; i++){

				// get random quality indicator from ensemble
				QualityIndicator qi = getMetric();
				
				
		    	task.resetCounter();
		    	ExecutorService pool = Executors.newFixedThreadPool(players.size());
		        Set<Future<AlgorithmRunResult>> set = new HashSet<>();
		        for (MOAlgorithm<DoubleProblem, DoubleMOTask, Double> al: players) {
		          Future<AlgorithmRunResult> future = pool.submit(al.createRunnable(al, (DoubleMOTask) task.clone()));
		          set.add(future);
		        }

		        for (Future<AlgorithmRunResult> future : set) {
		        	try {
						AlgorithmRunResult<ParetoSolution<Double>, MOAlgorithm<DoubleProblem, DoubleMOTask, Double>,DoubleMOTask> res = future.get();

		        		results.add(new MOAlgorithmEvalResult(res.solution, res.algorithm, res.task));


					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
		        }
		        
		    	pool.shutdown();
				
				/*for (MOAlgorithm al: players) {

					ParetoSolution bestByALg = null;

					try {
						bestByALg = al.execute(task);
						task.resetCounter();
					} catch (StopCriterionException e) {
						e.printStackTrace();
					}
					results.add(new MOAlgorithmEvalResult(bestByALg, al)); 
				}*/

				FitnessComparator fc = new FitnessComparator(task, qi);
				Collections.sort(results, fc); //best first
				participants.add(results.get(0));
				results.clear();
			}
		}
		else //same number of approximations for all paticipating algorithms
		{
			if(tournamentSize % players.size() != 0)
			{
				System.out.println("Increasing tournament size!");
				tournamentSize += tournamentSize % players.size();
			}
			int approxNum = tournamentSize / players.size();
			
			for (int i = 0; i < approxNum; i++) {
				
				//System.out.println("Run: "+i);
		    	task.resetCounter();
		    	ExecutorService pool = Executors.newFixedThreadPool(players.size());
		        Set<Future<AlgorithmRunResult>> set = new HashSet<>();
		        for (MOAlgorithm<DoubleProblem, DoubleMOTask, Double> al: players) {
		          Future<AlgorithmRunResult> future = pool.submit(al.createRunnable(al, (DoubleMOTask) task.clone()));
		          set.add(future);
		        }

		        for (Future<AlgorithmRunResult> future : set) {
		        	try {
						AlgorithmRunResult<ParetoSolution<Double>, MOAlgorithm<DoubleProblem, DoubleMOTask, Double>,DoubleMOTask> res = future.get();

		        		participants.add(new MOAlgorithmEvalResult(res.solution, res.algorithm, res.task));


					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
		        }
		        
		    	pool.shutdown();
				
				/*for (MOAlgorithm al: players) {
					ParetoSolution bestByALg = null;

					try {
						bestByALg = al.execute(task);
						task.resetCounter();
					} catch (StopCriterionException e) {
						e.printStackTrace();
					}
					participants.add(new MOAlgorithmEvalResult(bestByALg, al)); 
				}*/
			}
		}
		
		return participants;
	}
	
    private static HashMap<String, Double> sortByComparator(HashMap<String, Double> unsortMap, final boolean order)
    {

        List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Double>>()
        {
            public int compare(Entry<String, Double> o1,
                    Entry<String, Double> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }


	private void fillEnsemble(ArrayList<IndicatorName> indicators) {
		ensemble = new ArrayList<>();
		
		for (IndicatorName name : indicators) {
			ensemble.add(IndicatorFactory.createIndicator(name, task.problem.getNumberOfObjectives(),task.problem.getReferenceSetFileName()));
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
                	if(qi.getIndicatorType() == IndicatorType.UNARY)
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

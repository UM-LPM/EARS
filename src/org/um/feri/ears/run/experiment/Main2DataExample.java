/**
 * Insert data
 * <p>
 * 
 * @author Matej Crepinsek
 * @version 1
 * 
 *          <h3>License</h3>
 * 
 *          Copyright (c) 2011 by Matej Crepinsek. <br>
 *          All rights reserved. <br>
 * 
 *          <p>
 *          Redistribution and use in source and binary forms, with or without
 *          modification, are permitted provided that the following conditions
 *          are met:
 *          <ul>
 *          <li>Redistributions of source code must retain the above copyright
 *          notice, this list of conditions and the following disclaimer.
 *          <li>Redistributions in binary form must reproduce the above
 *          copyright notice, this list of conditions and the following
 *          disclaimer in the documentation and/or other materials provided with
 *          the distribution.
 *          <li>Neither the name of the copyright owners, their employers, nor
 *          the names of its contributors may be used to endorse or promote
 *          products derived from this software without specific prior written
 *          permission.
 *          </ul>
 *          <p>
 *          THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *          "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *          LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 *          FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 *          COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 *          INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 *          BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *          LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 *          CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 *          LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 *          ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *          POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package org.um.feri.ears.run.experiment;

import net.sourceforge.jswarm_pso.SwarmAlgorithm;

import org.um.feri.ears.algorithms.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingRPUOed30;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.run.RunMain;

import com.erciyes.karaboga.bee.BeeColonyAlgorithm;
import com.um.feri.brest.de.DEAlgorithm;

/**
 * @author Administrator
 * 
 */
public class Main2DataExample {
    public static void main(String[] args) {
        RunMain m = new RunMain(false, false, new RatingRPUOed30(30,1)) ;
        m.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new RandomWalkAMAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new ES1p1sAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new SwarmAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new BeeColonyAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));
        for (int k=1;k<11;k++)
            m.addAlgorithm(new DEAlgorithm(k,20),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin),new Rating(1500, 350, 0.06));
        m.run(30);
        System.out.println(m);
    }
    /*
    public static void main(String[] args) {
        
        Util.rnd.setSeed(System.currentTimeMillis());
        long stTime = System.currentTimeMillis();
        RatingBenchmark.debugPrint = true; // prints one on one results
        EDBenchmarkRunArena data = new EDBenchmarkRunArena();
        data.ID = UUID.randomUUID().toString();
        //data.runType = EDEnumBenchmarkRunType.TEST;
        ArrayList<Algorithm> players = new ArrayList<Algorithm>();
        players.add(new RandomWalkAlgorithm());
        // players.add(new RandomWalkAMAlgorithm());
        players.add(new ES1p1sAlgorithm());
        players.add(new SwarmAlgorithm());
        players.add(new BeeColonyAlgorithm());
        players.add(new TLBOAlgorithm());
        for (int k=1;k<11;k++)
         players.add(new DEAlgorithm(k,20));
        players.add(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 20));

        ResultArena ra = new ResultArena(100);
        RatingRPUOed2 suopm = new RatingRPUOed2();
        ArrayList<PlayerAlgorithmExport> listAll = new ArrayList<PlayerAlgorithmExport>();
        PlayerAlgorithmExport tmp;
        Rating tmpr;
        for (Algorithm al : players) {
            // ra.addPlayer(al.getID(), 1500, 350, 0.06,0,0,0);
            tmpr = new Rating(1500, 350, 0.06); // TODO Read from file
            tmp = new PlayerAlgorithmExport(al, tmpr, 0, 0, 0);
            listAll.add(tmp);
            ra.addPlayer(tmp);
            suopm.registerAlgorithm(al);
        }
        suopm.run(ra, 10);
        ra.recalcRangs();
        Collections.sort(listAll, new Player.RatingComparator());
        int i = 0;
        ArrayList<String> keys = new ArrayList<String>();
        EDStatP2PList spl = new EDStatP2PList(data.ID);
        EDStatP2TaskList sptl = new EDStatP2TaskList(data.ID);
        EDStatPlayer2Player p2pTmp;
        data.players = new org.um.feri.ears.export.data.EDPlayer[listAll.size()];
        EDStatPlayerMoreInfoList playersMoreData = new EDStatPlayerMoreInfoList(data.ID);
        for (PlayerAlgorithmExport p: listAll) {
            System.out.println(p);  
            data.players[i++] = p.getExportPlayer();
            playersMoreData.list.add(p.getExportPlayerMoreInfo());
            Set<String> play = p.wldPlayers.keySet();
            for (String alid : play) {
                if (!keys.contains(alid + p.getPlayerId())) //get info from A or B
                    if (!keys.contains(p.getPlayerId() + alid)) {
                        p2pTmp = new EDStatPlayer2Player();
                        p2pTmp.idPlayerOne = p.getPlayerId();
                        p2pTmp.idPlayerTwo = alid;
                        p2pTmp.data = new EDWinnLossDraw();
                        org.um.feri.ears.rating.WinLossDraw wl = p.wldPlayers.get(alid);
                        p2pTmp.data.d = wl.getDraw();
                        p2pTmp.data.w = wl.getWin();
                        p2pTmp.data.l = wl.getLoss();
                        spl.list.add(p2pTmp);
                        keys.add(alid + p.getPlayerId());
                    }
            }
            Set<String> problist = p.wldProblems.keySet();
            EDStatPlayer2Task sptTmp;
            for (String probid: problist) {
                sptTmp = new EDStatPlayer2Task();
                org.um.feri.ears.rating.WinLossDraw wl = p.wldProblems.get(probid);
                sptTmp.taskID = probid;
                sptTmp.stat = new EDWinnLossDraw();;
                sptTmp.stat.d = wl.getDraw();
                sptTmp.stat.w = wl.getWin();
                sptTmp.stat.l = wl.getLoss();
                sptl.list.add(sptTmp);
            }
        }
        long endTime = System.currentTimeMillis();
        data.duration = endTime - stTime;
        data.benchmark = suopm.export();
        data.arenaName = "ER Year 2011";
        data.arenaOwner = "matej crepinsek";
        data.runType = EDEnumBenchmarkRunType.TEST;
        Gson gson = new Gson();
        String jsonRepresentation = gson.toJson(data, EDBenchmarkRunArena.class);
        String jsonP2P = gson.toJson(spl, EDStatP2PList.class);
        String jsonP2T = gson.toJson(sptl, EDStatP2TaskList.class);
        String jsonPlayerMoreDataLString = gson.toJson(playersMoreData, EDStatPlayerMoreInfoList.class);
        // String jsonRepresentation = gson.toJson(p2);
        System.out.println(jsonRepresentation);
        System.out.println(jsonP2P);
        System.out.println(jsonP2T);
        System.out.println(jsonPlayerMoreDataLString);
        EDBenchmarkRunArena a;
        a = gson.fromJson(jsonRepresentation, EDBenchmarkRunArena.class); //from string!
        System.out.println(a.benchmark.acronym);
        System.out.println(a.runDate);
    }*/
}

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import net.sourceforge.jswarm_pso.SwarmAlgorithm;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.PlayerAlgorithmExport;
import org.um.feri.ears.algorithms.es.ES1p1sAlgorithm;
import org.um.feri.ears.algorithms.random.RandomWalkAMAlgorithm;
import org.um.feri.ears.algorithms.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.RatingBenchmark;
import org.um.feri.ears.benchmark.RatingRPUOed2;
import org.um.feri.ears.export.data.EDBenchmark;
import org.um.feri.ears.export.data.EDBenchmarkRunArena;
import org.um.feri.ears.export.data.EDEnumBenchmarkRunType;
import org.um.feri.ears.export.data.EDPlayerMoreInfo;
import org.um.feri.ears.export.data.EDStatP2PList;
import org.um.feri.ears.export.data.EDStatP2TaskList;
import org.um.feri.ears.export.data.EDStatPlayer2Player;
import org.um.feri.ears.export.data.EDStatPlayer2Task;
import org.um.feri.ears.export.data.EDStatPlayerMoreInfoList;
import org.um.feri.ears.export.data.EDWinnLossDraw;
import org.um.feri.ears.problems.results.BankOfResults;
import org.um.feri.ears.problems.results.FriedmanTransport;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.rating.ResultArena;
import org.um.feri.ears.run.RunMain;
import org.um.feri.ears.util.Util;

import com.erciyes.karaboga.bee.BeeColonyAlgorithm;
import com.google.gson.Gson;
import com.um.feri.brest.de.DEAlgorithm;

/**
 * @author Administrator
 * 
 */
public class ExperimentPrintResults {
    public static void main(String[] args) {
        RunMain m = new RunMain(false, false, new RatingRPUOed2()) ;
        m.addAlgorithm(new RandomWalkAlgorithm(),new Rating(1500, 350, 0.06));
        m.addAlgorithm(new BeeColonyAlgorithm(),new Rating(1500, 350, 0.06));
       // m.addAlgorithm(new TLBOAlgorithm(),new Rating(1500, 350, 0.06));
       // m.addAlgorithm(new DEAlgorithm(DEAlgorithm.JDE_rand_1_bin, 20),new Rating(1500, 350, 0.06));
        m.run(2);
        BankOfResults br = m.getBankOfResults();
        FriedmanTransport fr = br.calc4Friedman();
        fr.print();
        System.out.println(br);
        System.out.println(m);
    }
}

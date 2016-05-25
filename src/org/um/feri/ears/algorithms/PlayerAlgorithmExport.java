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
package org.um.feri.ears.algorithms;

import org.um.feri.ears.export.data.EDAuthor;
import org.um.feri.ears.export.data.EDPlayer;
import org.um.feri.ears.export.data.EDPlayerMoreInfo;
import org.um.feri.ears.rating.Player;
import org.um.feri.ears.rating.Rating;
import org.um.feri.ears.util.Util;

/**
 * @author Administrator
 *
 */
public class PlayerAlgorithmExport<T extends AlgorithmBase> extends Player {
    Rating start;
    T alg; 
    public PlayerAlgorithmExport(T a, Rating start, int win, int l, int d){
        super(a.getID(), start, win, l, d);
        this.start = new Rating(start);
        alg = a;     
    }
    public T getAlgorithm() {
        return alg;
    }
    public EDPlayerMoreInfo getExportPlayerMoreInfo() {
        EDPlayerMoreInfo p = new EDPlayerMoreInfo();
        p.id = alg.ai.getVersionAcronym();
        p.description = alg.ai.getVersionDescription();
        p.source = alg.ai.getPaperBib(); //TODO safe format escapes!
        p.info =   alg.ai.getParameters().toString(); //TODO
        p.sourceCode = new EDAuthor();
        p.sourceCode.email = alg.au.getEmail(); //TODO replaces @ with at...!
        p.sourceCode.firstName = alg.au.getFirstName();
        p.sourceCode.lastName = alg.au.getLastName();
        p.sourceCode.nickName = alg.au.getNickName();
        p.sourceCode.info = alg.au.getInfo();
        return p;
    }
    
    public EDPlayer getExportPlayer() {
        EDPlayer p = new EDPlayer();
        p.id_version = alg.ai.getVersionAcronym();
        if (alg.au!=null)
            p.info = alg.au.getNickName();
        else p.info ="";//   alg.ai.getParameters().toString(); //TODO
        p.benchmarkRunDuration = 0;
        if (alg.art!=null) {
            p.benchmarkRunDuration = alg.art.getSumTime();
        }
        p.oldRating = new org.um.feri.ears.export.data.EDRating();
        p.oldRating.rating = Util.roundDouble3(start.getRating());
        p.oldRating.RD = Util.roundDouble3(start.getRD());
        p.oldRating.rv = Util.roundDouble3(start.getRatingVolatility());
        p.newRating = new org.um.feri.ears.export.data.EDRating();
        p.newRating.rating = Util.roundDouble3(this.getRatingData().getRating());
        p.newRating.RD = Util.roundDouble3(this.getRatingData().getRD());
        p.newRating.rv = Util.roundDouble3(this.getRatingData().getRatingVolatility());
        p.stat = new org.um.feri.ears.export.data.EDWinnLossDraw();
        p.stat.d = sumWinLossDraw.getDraw();
        p.stat.l = sumWinLossDraw.getLoss();
        p.stat.w = sumWinLossDraw.getWin();
        return p;
    }

}

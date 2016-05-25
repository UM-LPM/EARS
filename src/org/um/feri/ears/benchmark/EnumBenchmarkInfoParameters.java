package org.um.feri.ears.benchmark;
/**
 * This parameters can be used for dynamically tuning algorithms, based
 * on parameters!
 * 
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
public enum EnumBenchmarkInfoParameters {
    DIMENSION("number of real parameters","D"),  
    EVAL("number of evaluations","E"),
    CONSTRAINED("constrained optimization","C_O"),
    STOP_IF_GLOBAL("stops when global optimum was founded","GLOBAL_STOP"),
    DRAW_PARAM("sets condition for draw","DRAW_PARAM"),
    NUMBER_OF_TEST_CONFIGURATIONS("maximum number of tested parameter configurations","BEST_OF_X_CONFIGURATIONS"),
    NUMBER_OF_TASKS("number of problems","NUMBER_OF_TASKS"), NUMBER_OF_DEULS("number of duels / repetitions","NUMBER_OF_DEULS");
    
    private String description, shortName;
    private EnumBenchmarkInfoParameters(String s, String sh) {
        description = s;
        shortName = sh;
        
    }   
    public String getDescription() {
        return description;
    }

    public String getShortName() {
        return shortName;
    }

    public String toString() {
        return shortName;
    }
}

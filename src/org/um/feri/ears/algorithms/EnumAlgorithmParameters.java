package org.um.feri.ears.algorithms;
/**
 * Standardize parameters, and their abbreviations.
 * Easy to see what are changes!
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
public enum EnumAlgorithmParameters {
    POP_SIZE("population size","pop_size"),  
    P_C("crossover probability","pc"),
    P_M("mutation probability","pm"),
    CR("CR DE parameter","CR"), 
    F("F DE parameter", "F"), //DE parameter
    TURNAMENT_SIZE("turnament size", "TS"), //DE parameter
    ELITE("Number of unchangerd individuals","elite"), //number of elite
    LAMBDA("ES lambda offspring","lambda"), //- number of samples pre iteratiun
    MU("ES mu","mu"),
    SIGMA("ES sigma -","sigma"),
    TAO_0("ES learning rate ","tao_0"),
    K_ITERATIONS("recalculate/refresh after k iterations","k"),
    C_FACTOR("ES sigma*c","c"),
    W_INTERIA("interia","w"), //http://dsp.szu.edu.cn/pso/ispo/download/a%20modified%20pso.pdf
    C1("particle increment","c1"),
    C2("particle globalincrement","c2"),
    UNNAMED1("Unnamed parameter 1","param1"),  //if there is no parameter to select
    UNNAMED2("Unnamed parameter 2","param2"),  //if there is no parameter to select
    UNNAMED3("Unnamed parameter 3","param3"),  //if there is no parameter to select
    UNNAMED4("Unnamed parameter 4","param4"),  //if there is no parameter to select
    UNNAMED5("Unnamed parameter 5","param5"),  //if there is no parameter to select
    UNNAMED6("Unnamed parameter 6","param6"),  //if there is no parameter to select
    UNNAMED7("Unnamed parameter 7","param7"), 
    SETTINGS_PARAM_COMBINATION("Algorithem settings parameters selection ID","PARA_ID"); //if there is no parameter to select
    private String description, shortName;
    private EnumAlgorithmParameters(String s, String sh) {
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

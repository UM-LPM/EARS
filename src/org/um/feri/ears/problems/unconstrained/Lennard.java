
package org.um.feri.ears.problems.unconstrained;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.um.feri.ears.problems.EvaluationStorage;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.misc.SoilModelProblem;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

/*
Global minima of Lennard-Jones clusters where D less than 150
http://doye.chem.ox.ac.uk/jon/structures/LJ/tables.150.html
*/

public class Lennard extends Problem {

    static class LennardJonesSolution {

        public LennardJonesSolution(){}

        public LennardJonesSolution(int dimension, double energy, double[] points) {
            this.dimension = dimension;
            this.energy = energy;
            this.points = points;
        }

        public int dimension;
        public double energy;
        public double[] points;
    }

    double globalOptimum = 0.0;

    public Lennard(int d) {
        this(d, -10, 10);
    }

    public Lennard(int d, double lower, double upper) {
        super(d*3 , 0); // d = number of atoms, translation to 3D euclidean space
        assert (d <= 150 && d > 2);

        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("LennardJonesOptima.json");
        ObjectMapper mapper = new ObjectMapper();

        try {
            assert inputStream != null;
            TypeReference<HashMap<Integer, LennardJonesSolution>> typeRef = new TypeReference<HashMap<Integer, LennardJonesSolution>>() {};
            HashMap<Integer, LennardJonesSolution> optima = mapper.readValue(inputStream, typeRef);

            LennardJonesSolution solution = optima.get(d);
            optimum[0] = new double[d];
            optimum[0] = solution.points;
            globalOptimum = solution.energy;

        } catch (IOException e) {
            e.printStackTrace();
        }


        lowerLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, lower));
        upperLimit = new ArrayList<>(Collections.nCopies(numberOfDimensions, upper));
        name = "LennardJones";
    }

    @Override
    public double eval(double[] x) {
        double energy = 0.0;
        double xd, yd, zd, ed, ud;

        for (int i = 0; i < numberOfDimensions - 1; i += 3) {
            for (int j = i + 3; j < numberOfDimensions; j += 3) {
                xd = x[i] - x[j];
                yd = x[i + 1] - x[j + 1];
                zd = x[i + 2] - x[j + 2];
                ed = xd * xd + yd * yd + zd * zd;
                ud = ed * ed * ed;
                if (ud > 1.0e-10) {

                    energy += (1 / (ud * ud)) - (1 / ud);
                } else {
                    energy += 1.0e20;
                }
            }
        }

        energy *= 4;
        return energy;
    }

    @Override
    public double getGlobalOptimum() {
        return globalOptimum;
    }
}
package org.um.feri.ears.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class MeanStDev {
    public double mean, stdev;
    ArrayList<Double> list;
    public static NumberFormat meanFormat = new DecimalFormat("#,##0.0"), stdFormat = new DecimalFormat("#,##0.0");
    public static void SetLocalEnglish() {
        SetLocal(new Locale("en", "US"));
    }
    public static void SetLocal(Locale locale) {
        NumberFormat  tmp = NumberFormat.getInstance(locale);
        ((DecimalFormat) tmp).applyPattern("#,##0.0");
        meanFormat = tmp;
        tmp = NumberFormat.getInstance(locale);
        ((DecimalFormat) tmp).applyPattern("#,##0.0");
        stdFormat = tmp;
    }
    public MeanStDev(double m, double s) {
        mean = m;
        stdev = s;
    }

    public MeanStDev(ArrayList<Double> list) {
        super();
        this.list = list;
        stdev = standardDeviation(list);
        mean = mean(list);
    }

    public String toStringMeans() {
        if (stdev < 0.000000000001) return "{\\footnotesize $" + meanFormat.format(mean) + "$}";
        return "{\\footnotesize $" + meanFormat.format(mean) + "$}";
    }

    public String toStringMean() {
        if (stdev < 0.000000000001) return "$" + meanFormat.format(mean) + "$";
        return "$" + meanFormat.format(mean) + "$";
    }

    public String toStringMeanFootnotesize() {
        if (stdev < 0.000000000001) return "{\\footnotesize $" + meanFormat.format(mean) + "$}";
        return "{\\footnotesize $" + meanFormat.format(mean) + "$}";
    }

    public String toStringStDevs() {
        if (stdev < 0.000000000001) return "{\\footnotesize $\\pm 0$}";
        return "{\\footnotesize $" + "\\pm" + stdFormat.format(stdev) + "$}";
    }

    public String toStringStDev() {
        if (stdev < 0.000000000001) return "$\\pm 0$";
        return "$" + "\\pm" + stdFormat.format(stdev) + "$";
    }

    public String toStringStDevFootnotesize() {
        if (stdev < 0.000000000001) return "{\\footnotesize $\\pm 0$}";
        return "{\\footnotesize $" + "\\pm" + stdFormat.format(stdev) + "$}";
    }

    public String toString() {
        if (stdev < 0.000000000001) return "{\\footnotesize $" + meanFormat.format(mean) + "\\pm 0$}";
        return "{\\footnotesize $" + meanFormat.format(mean) + "\\pm" + stdFormat.format(stdev) + "$}";
    }

    public double getMean() {
        return mean;
    }

    public ArrayList<Double> getList() {
        return list;
    }

    public static double divide(double a, double b) {
        if (b == 0)
            return 0;
        return a / b;
    }

    /**
     * @param population an array containing the population
     * @return the variance
     */
    public double variance(ArrayList<Double> population) {
        long n = 0;
        double mean = 0;
        double s = 0.0;

        for (Double x : population) {
            n++;
            double delta = x - mean;
            mean += delta / n;
            s += delta * (x - mean);
        }
        return divide(s, (n - 1));
        //return (s / n);
    }

    /**
     * @param population an array containing the population
     * @return the standard deviation
     */
    public double standardDeviation(ArrayList<Double> population) {
        if (population.isEmpty()) return 0;
        double v = variance(population);
        if (v > 0)
            return Math.sqrt(variance(population));
        else return 0;
    }

    public double mean(ArrayList<Double> population) {
        if (population.isEmpty()) return 0;
        double sum = 0;
        for (Double d : population) {
            sum += d;
        }
        return divide(sum, population.size());
    }

    public String toString2Lines() {
        StringBuffer sb = new StringBuffer();
        sb.append("\\begin{tabular}{@{}r@{}} ");
        if (this.stdev < 1.0E-12D) {
            sb.append("$").append(meanFormat.format(this.mean) + "$ \\\\ $\\pm 0$");
        } else {
            sb.append("$").append(meanFormat.format(this.mean) + "$ \\\\ $\\pm" + stdFormat.format(this.stdev) + "$");
        }

        sb.append("\\end{tabular}");
        return sb.toString();
    }

    public String toString2Lines(int devide) {
        StringBuffer sb = new StringBuffer();
        sb.append("\\begin{tabular}{@{}r@{}} ");
        if (this.stdev < 1.0E-12D) {
            sb.append("$").append(meanFormat.format(this.mean / (double)devide) + "$ \\\\ $\\pm 0$");
        } else {
            sb.append("$").append(meanFormat.format(this.mean / (double)devide) + "$ \\\\ $\\pm" + stdFormat.format(this.stdev / (double)devide) + "$");
        }

        sb.append("\\end{tabular}");
        return sb.toString();
    }

    public String toString2LinesFootsize() {
        StringBuffer sb = new StringBuffer();
        sb.append("{\\footnotesize \\begin{tabular}{@{}r@{}} ");
        if (this.stdev < 1.0E-12D) {
            sb.append("$").append(meanFormat.format(this.mean) + "$ \\\\ $\\pm 0$");
        } else {
            sb.append("$").append(meanFormat.format(this.mean) + "$ \\\\ $\\pm" + stdFormat.format(this.stdev) + "$");
        }

        sb.append("\\end{tabular}}");
        return sb.toString();
    }
}
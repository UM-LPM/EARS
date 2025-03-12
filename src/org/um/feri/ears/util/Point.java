package org.um.feri.ears.util;

import org.um.feri.ears.problems.NumberSolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


public class Point<N extends Number> implements List<Double> {

    private double[] point;

    /**
     * Constructor
     *
     * @param dimensions Dimensions of the point
     */
    public Point(int dimensions) {
        point = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            point[i] = 0.0;
        }
    }

    /**
     * Copy constructor
     *
     * @param point
     */
    public Point(Point point) {

        this.point = new double[point.getNumberOfDimensions()];

        for (int i = 0; i < point.getNumberOfDimensions(); i++) {
            this.point[i] = point.getDimensionValue(i);
        }
    }

    /**
     * Constructor from a solution
     *
     * @param solution
     */
    public Point(NumberSolution<N> solution) {

        int dimensions = solution.getNumberOfObjectives();
        point = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            point[i] = solution.getObjective(i);
        }
    }

    /**
     * Constructor from an array of double values
     *
     * @param point
     */
    public Point(double[] point) {
        this.point = new double[point.length];
        System.arraycopy(point, 0, this.point, 0, point.length);
    }

    /**
     * Constructor from an ArrayList of double values
     *
     * @param point
     */
    public Point(ArrayList<N> point) {
        this.point = point.stream().mapToDouble(Number::doubleValue).toArray();
    }

    /**
     * Constructor from an List of double values
     *
     * @param point
     */
    public Point(List<N> point) {
        this.point = point.stream().mapToDouble(Number::doubleValue).toArray();
    }

    public Point<N> copy() {
        return new Point<N>(point);
    }

    public int getNumberOfDimensions() {
        return point.length;
    }

    public double[] getValues() {
        return point;
    }


    public double getDimensionValue(int index) {
        return point[index];
    }

    public void setDimensionValue(int index, double value) {
        point[index] = value;
    }

    public boolean equals(Point p) {
        if (this == p)
            return true;
		return Arrays.equals(point, p.point);
	}

    public Point<N> subtract(Point<N> p) {
		if (p == null) {
			System.err.println("The second point is null");
            return null;
		} else if (point.length != p.getNumberOfDimensions()) {
			System.err.println("Mismatch in the number of dimensions");
            return null;
        }

        for (int i = 0; i < point.length; i++) {
            point[i] -= p.point[i];
        }
        return this;
    }

    public Point<N> subtractCopy(Point<N> p) {
		if (p == null) {
			System.err.println("The second point is null");
            return null;
		} else if (point.length != p.getNumberOfDimensions()) {
			System.err.println("Mismatch in the number of dimensions");
            return null;
        }

        Point<N> result = new Point<N>(point);
        result.subtract(p);
        return result;
    }

    public Point<N> add(Point<N> p) {
		if (p == null) {
			System.err.println("The second point is null");
			return null;
		} else if (point.length != p.getNumberOfDimensions()) {
			System.err.println("Mismatch in the number of dimensions");
            return null;
        }

        for (int i = 0; i < point.length; i++) {
            point[i] += p.point[i];
        }
        return this;
    }

    public Point<N> addCopy(Point<N> p) {
		if (p == null) {
			System.err.println("The second point is null");
			return null;
		} else if (point.length != p.getNumberOfDimensions()) {
			System.err.println("Mismatch in the number of dimensions");
            return null;
        }

        Point<N> result = new Point<N>(point);
        result.add(p);
        return result;
    }

    public Point<N> add(double d) {
        for (int i = 0; i < point.length; i++) {
            point[i] += d;
        }
        return this;
    }

    public Point<N> subtract(double d) {
        for (int i = 0; i < point.length; i++) {
            point[i] -= d;
        }
        return this;
    }
    
    public Point<N> divide(double d) {
        for (int i = 0; i < point.length; i++) {
            point[i] /= d;
        }
        return this;
    }

    public Point<N> multiply(double d) {
        for (int i = 0; i < point.length; i++) {
            point[i] *= d;
        }
        return this;
    }

    public Point<N> multiplyCopy(double d) {
        Point<N> result = new Point<N>(point);
        result.multiply(d);
        return result;
    }

    public Point<N> multiply(Point<N> p) {
        if (p == null) {
			System.err.println("The second point is null");
			return null;
		} else if (point.length != p.getNumberOfDimensions()) {
			System.err.println("Mismatch in the number of dimensions");
            return null;
        }
        
        for (int i = 0; i < point.length; i++) {
            point[i] *= p.point[i];
        }

        return this;
    }

    public Point<N> multiplyCopy(Point<N> p) {
        Point<N> result = new Point<N>(point);
        result.multiply(p);
        return result;
    }

    /**
     *  Apply the natural logarithm to all of the components separately
     */
    public Point<N> log() {
        for (int i = 0; i < point.length; i++) {
            point[i] = Math.log(point[i]);
        }
        return this;
    }

    /**
     * Apply a logarithm with a custom base to all of the components separately
     *
     * @param base Base of the logarithm
     * @return reference to the same object
     */
    public Point<N> log(double base) {
        for (int i = 0; i < point.length; i++) {
            point[i] = Math.log(point[i]) / Math.log(base);
        }
        return this;
    }

    /**
     * Apply the sqrt to all of the components separately
     * @return reference to the same object
     */
    public Point<N> sqrt() {
        for (int i = 0; i < point.length; i++) {
            point[i] = Math.sqrt(point[i]);
        }
        return this;
    }

    /**
     * Apply the pow(p[i], exp) to all of the components p separately
     * @return reference to the same object
     */
    public Point<N> power(double exp) {
        for (int i = 0; i < point.length; i++) {
            point[i] = Math.pow(point[i], exp);
        }
        return this;
    }

    /**
     * Apply sin to all of the components separately
     * @return reference to the same object
     */
    public Point<N> sin() {
        for (int i = 0; i < point.length; i++) {
            point[i] = Math.sin(point[i]);
        }
        return this;
    }

    /**
     * Apply cos to all of the components separately
     * @return reference to the same object
     */
    public Point<N> cos() {
        for (int i = 0; i < point.length; i++) {
            point[i] = Math.cos(point[i]);
        }
        return this;
    }

    @Override
    public int size() {
        return point.length;
    }

    @Override
    public boolean isEmpty() {
        return point.length == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Double)) return false;
        double value = (Double) o;
        for (double coordinate : point) {
            if (coordinate == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Double> iterator() {
        return new Iterator<Double>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < point.length;
            }

            @Override
            public Double next() {
                if (!hasNext()) throw new NoSuchElementException();
                return point[index++];
            }
        };
    }

    @Override
    public Double get(int index) {
        if (index < 0 || index >= point.length) throw new IndexOutOfBoundsException();
        return point[index];
    }

    @Override
    public Double set(int index, Double element) {
        if (index < 0 || index >= point.length) throw new IndexOutOfBoundsException("Index " + index + " out of bounds.");
        double oldValue = point[index];
        point[index] = element;
        return oldValue;
    }

    @Override
    public void add(int index, Double element) {
        throw new UnsupportedOperationException("Cannot add to a fixed-size array.");
    }

    @Override
    public Double remove(int index) {
        throw new UnsupportedOperationException("Cannot remove from a fixed-size array.");
    }

    @Override
    public int indexOf(Object o) {
        if (!(o instanceof Double)) return -1;
        double value = (Double) o;
        for (int i = 0; i < point.length; i++) {
            if (point[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (!(o instanceof Double)) return -1;
        double value = (Double) o;
        for (int i = point.length - 1; i >= 0; i--) {
            if (point[i] == value) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<Double> listIterator() {
        throw new UnsupportedOperationException("ListIterator not supported.");
    }

    @Override
    public ListIterator<Double> listIterator(int index) {
        throw new UnsupportedOperationException("ListIterator not supported.");
    }

    @Override
    public List<Double> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("SubList not supported.");
    }

    @Override
    public boolean add(Double element) {
        throw new UnsupportedOperationException("Cannot add to a fixed-size array.");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Cannot remove from a fixed-size array.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Double> c) {
        throw new UnsupportedOperationException("Cannot add to a fixed-size array.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> c) {
        throw new UnsupportedOperationException("Cannot add to a fixed-size array.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Cannot remove from a fixed-size array.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Cannot modify a fixed-size array.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Cannot clear a fixed-size array.");
    }

    @Override
    public Object[] toArray() {
        Double[] result = new Double[point.length];
        for (int i = 0; i < point.length; i++) {
            result[i] = point[i];
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Generic toArray not supported.");
    }
}

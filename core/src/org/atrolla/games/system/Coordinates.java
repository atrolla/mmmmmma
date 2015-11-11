package org.atrolla.games.system;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public final class Coordinates {

    public static final Coordinates NULL = null;

    public static final Coordinates DEFAULT = new Coordinates(0d,0d);

    private final double x;
    private final double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Coordinates translateX(double tx) {
        return new Coordinates(x + tx, y);
    }

    public Coordinates translateY(double ty) {
        return new Coordinates(x, y + ty);
    }

    public Coordinates translateXandY(double tx, double ty) {
        return new Coordinates(x + tx, y + ty);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (Double.compare(that.x, x) != 0) return false;
        return Double.compare(that.y, y) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}

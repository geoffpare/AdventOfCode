package y2021.day5;

public class LineSegment {
    Integer x1, y1, x2, y2;

    public LineSegment(Integer x1, Integer y1, Integer x2, Integer y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Boolean isHorizontal() {
        return x1.equals(x2);
    }

    public Boolean isVertical() {
        return y1.equals(y2);
    }

    // A line is diagonal if the x and y move the same amount of distance
    public Boolean isDiagonal() {
        Integer xDist = Math.abs(x1-x2);
        Integer yDist = Math.abs(y1-y2);
        return xDist.equals(yDist);
    }

    public String toString() {
        return String.format("(%d,%d) -> (%d,%d)", x1, y1, x2, y2);
    }
}

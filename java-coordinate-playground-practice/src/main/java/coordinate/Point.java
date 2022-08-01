package coordinate;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Point {
    private static final String ERROR_OUT_OF_POINT_RANGE
            = "잘못된 범위의 입력값입니다. 정수 범위는 " + Point.MIN_VALUE + " ~ " + Point.MAX_VALUE + " 사이의 수로 입력해 주세요.";
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 24;
    private final int x;
    private final int y;

    public Point(int x, int y) {
        checkRangeOf(x, y);
        this.x = x;
        this.y = y;
    }

    private void checkRangeOf(int x, int y) {
        if (exceedRange(x) || exceedRange(y)) {
            throw new IllegalArgumentException(ERROR_OUT_OF_POINT_RANGE);
        }
    }

    private boolean exceedRange(int coordinate) {
        return coordinate < MIN_VALUE || coordinate > MAX_VALUE;
    }
}

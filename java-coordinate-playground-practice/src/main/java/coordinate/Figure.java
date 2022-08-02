package coordinate;

import java.util.List;

public interface Figure {
    boolean hasPoint(int x, int y);

    double area();

    String getAreaInfo();
}

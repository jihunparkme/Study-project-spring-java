package controller;

import coordinate.Figure;
import view.InputView;

public class CoordinateCalculator {
    public void run() {
        Figure figure = InputView.inputCoordinates();
        OutputView.showCoordinatePlane(figure);
        OutputView.showArea(figure);
    }
}

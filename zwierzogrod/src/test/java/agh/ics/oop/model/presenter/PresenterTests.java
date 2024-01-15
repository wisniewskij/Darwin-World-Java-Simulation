package agh.ics.oop.model.presenter;

import agh.ics.oop.presenter.SimulationPresenter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class PresenterTests {
    @Test void testAssureInRangeAndToString() {
        int val = 0;
        String result = SimulationPresenter.assureInRangeAndToString(val, 10, 100);
        Assertions.assertEquals("10", result);
    }
    @Test void testAssureInRangeAndToString2() {
        int val = 1000;
        String result = SimulationPresenter.assureInRangeAndToString(val, 10, 100);
        Assertions.assertEquals("100", result);
    }
    @Test void testAssureInRangeAndToString3() {
        int val = 100;
        String result = SimulationPresenter.assureInRangeAndToString(val, 10, 1001);
        Assertions.assertEquals("100", result);
    }
}

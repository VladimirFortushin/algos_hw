package ru.mephi.fortushin.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.*;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {


    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();//O(1)

        if (unitsByRow == null || unitsByRow.isEmpty()) {//O(1)
            return suitableUnits;
        }

        Map<Integer, Set<Integer>> aliveUnits = getAliveUnits(unitsByRow);//O(n)
        for (int row = 0; row < unitsByRow.size(); row++) {//O(unitsByRow.size()*rowUnits.size())
            List<Unit> rowUnits = unitsByRow.get(row);
            if (rowUnits == null) continue;

            Set<Integer> aliveYCoords = aliveUnits.get(row);

            for (Unit unit : rowUnits) {
                if (unit == null || !unit.isAlive()) {
                    continue;
                }

                int y = unit.getyCoordinate();
                boolean isBlocked;

                if (isLeftArmyTarget) {
                    isBlocked = aliveYCoords.contains(y + 1);
                } else {
                    isBlocked = aliveYCoords.contains(y - 1);
                }
                if (!isBlocked) {
                    suitableUnits.add(unit);
                }
            }
        }

        return suitableUnits;
    }

    private static Map<Integer, Set<Integer>> getAliveUnits(List<List<Unit>> unitsByRow) {//O(n)
        Map<Integer, Set<Integer>> aliveUnitsInRow = new HashMap<>();
        for (int row = 0; row < unitsByRow.size(); row++) {
            Set<Integer> yCoords = new HashSet<>();
            List<Unit> rowUnits = unitsByRow.get(row);

            if (rowUnits != null) {
                for (Unit unit : rowUnits) {
                    if (unit != null && unit.isAlive()) {
                        yCoords.add(unit.getyCoordinate());
                    }
                }
            }
            aliveUnitsInRow.put(row, yCoords);
        }
        return aliveUnitsInRow;
    }
}

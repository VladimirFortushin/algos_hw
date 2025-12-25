package ru.mephi.fortushin.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    private final int UNIT_LIMIT = 11;
    private final int ARCHER_INDEX = 0;
    private final int KNIGHT_INDEX = 1;
    private final int SWORDSMAN_INDEX = 2;
    private final int PIKEMAN_INDEX = 3;

    private final int HEIGHT = 21;
    private final int DEPLOYMENT_COLUMNS = 3;

    @Override
    public Army generate(List<Unit> unitPresets, int maxPoints) {

        //O(4*log4) = O(1)
        Unit[] sortedPresets = unitPresets.toArray(new Unit[4]);
        Arrays.sort(sortedPresets, (a, b) -> {
            double ratioA = (double) a.getBaseAttack() / a.getCost();
            double ratioB = (double) b.getBaseAttack() / b.getCost();
            int cmp = Double.compare(ratioB, ratioA);
            if (cmp != 0) return cmp;
            double healthRatioA = (double) a.getHealth() / a.getCost();
            double healthRatioB = (double) b.getHealth() / b.getCost();
            return Double.compare(healthRatioB, healthRatioA);
        });

        //O(4×11) = O(44) = O(1)
        List<Unit> armyUnits = new ArrayList<>();
        int totalCost = 0;
        int[] typeCount = new int[4];
        for (Unit preset : sortedPresets) {
            int typeIndex = getTypeIndex(preset.getUnitType());
            int unitCost = preset.getCost();
            int maxUnitsOfThisType = Math.min(
                    UNIT_LIMIT,
                    (maxPoints - totalCost) / unitCost
            );
            for (int i = 0; i < maxUnitsOfThisType; i++) {
                Unit newUnit = getUnit(preset, ++typeCount[typeIndex]);
                armyUnits.add(newUnit);
                totalCost += unitCost;
                if (typeCount[typeIndex] >= UNIT_LIMIT) {
                    break;
                }
            }
        }
        placeUnitsOptimized(armyUnits);
        Army army = new Army();
        army.setUnits(armyUnits);
        army.setPoints(totalCost);
        return army;
    }
    //~O(1)
    private static Unit getUnit(Unit preset, int typeCount) {
        Unit newUnit = new Unit(
                preset.getName() + " " + typeCount,
                preset.getUnitType(),
                preset.getHealth(),
                preset.getBaseAttack(),
                preset.getCost(),
                preset.getAttackType(),
                new HashMap<>(preset.getAttackBonuses()),
                new HashMap<>(preset.getDefenceBonuses()),
                0, 0
        );
        newUnit.setAlive(true);
        return newUnit;
    }

    //O(n)
    private void placeUnitsOptimized(List<Unit> units) {
        int count = units.size();
        int cellsPerColumn = (count + DEPLOYMENT_COLUMNS - 1) / DEPLOYMENT_COLUMNS;
        int index = 0;
        for (int x = 0; x < DEPLOYMENT_COLUMNS && index < count; x++) {
            for (int y = 0; y < cellsPerColumn && index < count; y++) {
                int actualY = (y * HEIGHT) / cellsPerColumn;
                units.get(index).setxCoordinate(x);
                units.get(index).setyCoordinate(actualY);
                index++;
            }
        }
    }

    private int getTypeIndex(String unitType) {
        return switch (unitType) {
            case "Archer" -> ARCHER_INDEX;
            case "Knight" -> KNIGHT_INDEX;
            case "Swordsman" -> SWORDSMAN_INDEX;
            case "Pikeman" -> PIKEMAN_INDEX;
            default -> -1;
        };
    }
}
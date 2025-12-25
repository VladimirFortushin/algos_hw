package ru.mephi.fortushin.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;

public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        //O(1)
        PriorityQueue<Unit> turnQueue = new PriorityQueue<>(
                (u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack())
        );

        //O(n)
        Map<Unit, Boolean> unitToArmyMap = new HashMap<>();
        Set<Unit> playerAliveSet = new HashSet<>();
        Set<Unit> computerAliveSet = new HashSet<>();

        //O(n)
        initArmyStructures(playerArmy.getUnits(), turnQueue, unitToArmyMap, playerAliveSet, true);
        initArmyStructures(computerArmy.getUnits(), turnQueue, unitToArmyMap, computerAliveSet, false);

        while (!turnQueue.isEmpty()) {
            List<Unit> movedThisRound = new ArrayList<>();

            //O(n*log n)
            while (!turnQueue.isEmpty()) {
                Unit attacker = turnQueue.poll(); //O(log n)
                if (!attacker.isAlive()) {
                    continue;
                }

                //O(1)
                Boolean isPlayerUnit = unitToArmyMap.get(attacker);
                if (isPlayerUnit == null) continue;

                Set<Unit> enemyAliveSet = isPlayerUnit ? computerAliveSet : playerAliveSet;

                //O(1)
                if (enemyAliveSet.isEmpty()) {
                    movedThisRound.add(attacker);
                    continue;
                }

                Unit target = attacker.getProgram().attack();
                if (target != null && target.isAlive()) {
                    printBattleLog.printBattleLog(attacker, target);

                    if (!target.isAlive()) {
                        removeDeadUnit(target, enemyAliveSet, unitToArmyMap);
                    }
                }

                if (attacker.isAlive()) {
                    movedThisRound.add(attacker);
                }

                //O(1)
                if (playerAliveSet.isEmpty() || computerAliveSet.isEmpty()) {
                    break;
                }
            }

            // O(n*log n)
            for (Unit unit : movedThisRound) {
                if (unit.isAlive()) {
                    turnQueue.add(unit); //O(log n)
                }
            }

            //O(1)
            if (playerAliveSet.isEmpty() || computerAliveSet.isEmpty()) {
                break;
            }
        }

        System.out.println("\nЭТА БИТВА БЫЛА ЛЕГЕНДАРНОЙ\nПобедил " +
                (!playerAliveSet.isEmpty() ? "игрок" : "комплюктер"));
    }

    // O(units.size())
    private void initArmyStructures(List<Unit> units, PriorityQueue<Unit> queue,
                                    Map<Unit, Boolean> unitToArmyMap,
                                    Set<Unit> aliveSet, boolean isPlayer) {
        if (units == null) return;

        for (Unit unit : units) {
            if (unit != null && unit.isAlive()) {
                queue.add(unit);
                unitToArmyMap.put(unit, isPlayer);
                aliveSet.add(unit);
            }
        }
    }

    //O(1)
    private void removeDeadUnit(Unit unit, Set<Unit> aliveSet, Map<Unit, Boolean> unitToArmyMap) {
        aliveSet.remove(unit);
        unitToArmyMap.remove(unit);
    }
}
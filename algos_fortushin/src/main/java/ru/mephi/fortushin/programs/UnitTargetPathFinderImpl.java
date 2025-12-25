package ru.mephi.fortushin.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;
//import ru.mephi.fortushin.util.PathFinder.*;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {//O(n*log n)

        boolean[][] blockedCells = new boolean[WIDTH][HEIGHT];

        for (Unit unit : existingUnitList) {
            if (unit != targetUnit) {
                blockedCells[unit.getxCoordinate()][unit.getyCoordinate()] = true;
            }
        }

        double[][] minimalDistance = new double[WIDTH][HEIGHT];
        Edge[][] previousCell = new Edge[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                minimalDistance[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        int attackX = attackUnit.getxCoordinate();
        int attackY = attackUnit.getyCoordinate();
        int targetX = targetUnit.getxCoordinate();
        int targetY = targetUnit.getyCoordinate();

        minimalDistance[attackX][attackY] = 0;

        PriorityQueue<Edge> pq = new PriorityQueue<>(
                Comparator.comparingDouble(cell -> minimalDistance[cell.getX()][cell.getY()])
        );

        pq.add(new Edge(attackX, attackY));//O(1)
//(-11)(01)(11)
//(-10)(00)(10)
//(-1-1)(0-1)(1-1)
        int[] directionX = {-1,-1,-1,0,0,1,1,1};
        int[] directionY = {-1,0,1,-1,1,-1,0,1};
        //алгоритм Дейкстры
        while (!pq.isEmpty()) {
            Edge currentCell = pq.poll();
            int x = currentCell.getX();
            int y = currentCell.getY();

            if (x == targetX && y == targetY) break;

            for (int i = 0; i < 8; i++) {
                int neighborX = x + directionX[i];
                int neighborY = y + directionY[i];

                if (neighborX < 0 || neighborY < 0 || neighborX >= WIDTH || neighborY >= HEIGHT)
                    continue;

                if (blockedCells[neighborX][neighborY])
                    continue;

                double cost = (directionX[i] == 0 || directionY[i] == 0) ? 1.0 : 1.4;//1.4 == √2 по Пифагору
                double newDistance = minimalDistance[x][y] + cost;

                if (newDistance < minimalDistance[neighborX][neighborY]) {
                    minimalDistance[neighborX][neighborY] = newDistance;
                    previousCell[neighborX][neighborY] = new Edge(x, y);
                    pq.add(new Edge(neighborX, neighborY));
                }
            }
        }

        if (Double.isInfinite(minimalDistance[targetX][targetY])) {
            return new ArrayList<>();
        }

        LinkedList<Edge> path = new LinkedList<>();
        int cellX = targetX, cellY = targetY;

        while (!(cellX == attackX && cellY == attackY)) {
            path.addFirst(new Edge(cellX, cellY));
            Edge cell = previousCell[cellX][cellY];
            cellX = cell.getX();
            cellY = cell.getY();
        }

        path.addFirst(new Edge(attackX, attackY));
        return path;
    }

}

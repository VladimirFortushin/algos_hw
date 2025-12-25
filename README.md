.jar использует Maven для сборки, все библиотеки внутри .jar. Для запуска достаточно bash: java -jar "Heroes Battle-1.0.0.jar"

Список операций, сложность которых в данном obf.jar выше условных O(1):

--GeneratePresetImpl:
  Arrays.sort: для 4 пресетов юнитов: O(4*log4) == (m * log m)
  for loop: для размещения 44 юнитов O(~44) == O(n)
    -Задание: O(44*4) == O(n*m)
--SimulateBattleImpl:
  PriorityQueue.poll: худший случай O(log n)
  PriorityQueue.add: худший случай O(log n)
  HashMap.put: худший случай O(log n)
  PriorityQueue.add in for loop: O(n*log n)
    -Задание: O(n^2 * log n)
--SuitableForAttackUnitsFinderImpl:
  List.get: O(n)
  Set.add in for loop: ~O(n)
  for loop(n) in for loop(m): O(n*m)
    -Задание: O(n*m)
--UnitTargetPathFinderImpl:
  PriorityQueue.poll: O(log WIDTH × HEIGHT)
  PriorityQueue.add: O(log WIDTH × HEIGHT)
  PriorityQueue.add in for loop (8) in while loop: O(8*WIDTH × HEIGHT * log WIDTH × HEIGHT)
    -Задание: O(WIDTH × HEIGHT * log WIDTH × HEIGHT)
  

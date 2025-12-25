.jar использует Maven для сборки, все библиотеки внутри .jar. Для запуска достаточно bash: java -jar "Heroes Battle-1.0.0.jar"<br>
<br>
Список операций, сложность которых в данном obf.jar выше условных O(1):<br>
<br>
--GeneratePresetImpl:<br>
  Arrays.sort: для 4 пресетов юнитов: O(4*log4) == (m * log m)<br>
  for loop: для размещения 44 юнитов O(~44) == O(n)<br>
    -Задание: O(44*4) == O(n*m)<br>
--SimulateBattleImpl:<br>
  PriorityQueue.poll: худший случай O(log n)<br>
  PriorityQueue.add: худший случай O(log n)<br>
  HashMap.put: худший случай O(log n)<br>
  PriorityQueue.add in for loop: O(n*log n)<br>
    -Задание: O(n^2 * log n)<br>
--SuitableForAttackUnitsFinderImpl:<br>
  List.get: O(n)<br>
  Set.add in for loop: ~O(n)<br>
  for loop(n) in for loop(m): O(n*m)<br>
    -Задание: O(n*m)<br>
--UnitTargetPathFinderImpl:<br>
  PriorityQueue.poll: O(log WIDTH × HEIGHT)<br>
  PriorityQueue.add: O(log WIDTH × HEIGHT)<br>
  PriorityQueue.add in for loop (8) in while loop: O(8*WIDTH × HEIGHT * log WIDTH × HEIGHT)<br>
    -Задание: O(WIDTH × HEIGHT * log WIDTH × HEIGHT)<br>
  

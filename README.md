# Курсовая работа МГТУ им. Баумана, "Построение CSG сцен"

# Настройка окружения
Понадобится любое [jdk/jre 11+](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html) версии и корректно настроенный [gradle](https://gradle.org).

# Сборка
```bash
git clone https://github.com/RomanQed/csg.git
cd csg
gradle shadowJar
```

# Запуск
```bash
java -jar build/libs/csg.jar
```

## Примечание
В программе используются рекурсивные алгоритмы, при достаточно большом количестве полигонов в результирующей сцене для корректной работы необходимо повысить размер стека jvm с помощью java -Xss(StackSize)M

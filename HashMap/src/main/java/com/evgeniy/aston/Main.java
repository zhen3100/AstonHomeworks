package com.evgeniy.aston;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        CustomHashMap<Integer, String> map = new CustomHashMap<>();
        map.put(212133, "Лидия Аркадьевна Бубликова");
        map.put(162348, "Иван Михайлович Серебряков");
        map.put(8082771, "Дональд Джон Трамп");
        map.put(162348, "Виктор Михайлович Стычкин");
        System.out.println(map.remove(8082771));
        System.out.println(map.get(8082771));
        System.out.println(map.get(162348));
    }
}
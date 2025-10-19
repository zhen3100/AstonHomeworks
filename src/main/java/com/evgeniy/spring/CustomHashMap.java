package com.evgeniy.spring;

import java.util.LinkedList;


/**
 * Реализация кастомного HashMap
 * @param <K> тип ключей
 * @param <V> тип ззначения
 */
public class CustomHashMap<K,V> {
    private static final int defualtCapacity = 16; // Начальный размер массива
    private static final double defualtLoadFactor = 0.75; // Коэфициент загрузки
    private LinkedList<Entry<K, V>>[] buckets; // Массив связанных списков
    private int size;
    private double loadFactor;

    /**
     * Конструктор по умолчанию
     */
    public CustomHashMap() {
        this.buckets = new LinkedList[defualtCapacity];
        this.loadFactor = defualtLoadFactor;
        this.size = 0;
        initializeBuckets();

    }
    /**
     * Конструктор с указанием начальной емкости и коэффициента загрузки.
     *
     * @param initialCapacity начальная емкость
     * @param loadFactor коэффициент загрузки
     */
    public CustomHashMap(int initialCapacity, double loadFactor) {
        this.buckets = new LinkedList[initialCapacity];
        this.loadFactor = loadFactor;
        this.size = 0;
        initializeBuckets();
    }

    /**
     * Конструктор с указанием начальной емкости.
     *
     * @param initialCapacity начальная емкость
     */
    public CustomHashMap(int initialCapacity) {
        this.buckets = new LinkedList[initialCapacity];
        this.loadFactor = defualtLoadFactor;
        this.size = 0;
        initializeBuckets();
    }

    /**
     * Внутренний класс для хранения пар ключ-значение.
     */
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        /**
         * Сравнивает две записи по ключу.
         * Используется для проверки существования ключа в бакете.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Entry<?, ?> entry = (Entry<?, ?>) obj;
            return key.equals(entry.key);
        }
        /**
         * Хеш-код записи определяется хеш-кодом ключа.
         */
        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }
    /**
     * Метод добавления элементов
     *
     * @param key ключ
     * @param value значение
     * @return предыдущее значение или null
     * @throws NullPointerException если ключ null
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Ключ не может быть равен Null");
        }

        /*
         * Проверяем не превышен ли коэф.загрузки
         * если да, то увеличиваем размер массива
         */
        if ((double) size / buckets.length >= loadFactor) {
            resize();
        }

        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];

        /*
         * Проверяем, существует ли уже запись с таким ключом
         * если да, то сохраняем значение
         * обновляем на новое значение
         * производим возврат старого значения
         */
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V oldValue = entry.value;
                entry.value = value;
                return oldValue;
            }
        }
        /*
         * Если ключ не найден, то создаем новую запись
         * добавляем ее в связный список
         */
        bucket.add(new Entry<>(key, value));
        size++;
        return null;
    }

    /**
     * Удаление пары элементов ключ-значение по ключу
     *
     * @param key ключ
     * @return удаляемое значение или Null
     * @throws NullPointerException если ключ null
     */
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("Ключ не может быть равен Null");
        }

        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];

        /*
         * Проверяем, существует ли уже запись с таким ключом
         * если да, то сохраняем значение перед удалением
         * удаляем запись
         * уменьшаем счетчик
         * производим возврат удаляемого значения
         */
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                V removedValue = entry.value;
                bucket.remove(entry);
                size--;
                return removedValue;
            }
        }

        return null;
    }

    /**
     * Возвращает значение по ключу.
     *
     * @param key ключ
     * @return значение или Null
     * @throws NullPointerException если ключ Null
     */
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("Использование null ключа недопустимо");
        }

        int bucketIndex = getBucketIndex(key);
        LinkedList<Entry<K, V>> bucket = buckets[bucketIndex];

        /*
         * Линейный поиск по бакету
         * нашли ключ - вернули значение
         */
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }

        return null;
    }

    /**
     * Вычисляет индекс бакета для ключа.
     *
     * @param key ключ
     * @return индекс бакета
     */
    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode) % buckets.length;
    }

    /**
     * Увеличивает емкость мапы и перераспределяет элементы.
     */
    private void resize() {
        // Создаем новый массив, которы будет в 2 раза больше предыдущего.
        LinkedList<Entry<K,V>>[] newList = new LinkedList[buckets.length * 2 ];

        // Инициализируем все бакеты нового массива
        for (int i=0; i<buckets.length*2; i++) {
            newList[i] = new LinkedList<>();
        }
        /*
         * Выполняем рехэшинг.
         * проходимся по всем элементам старого массива и перераспределяем по новым бакетам.
         */
        for (LinkedList<Entry<K,V>> bucket : buckets) {
            for (Entry<K,V> entry : bucket) {
                int newElementIndex = Math.abs(entry.hashCode()) % (newList.length* 2);
                newList[newElementIndex].add(entry);
            }
        }
        buckets = newList;
    }

    /**
     * Инициализирует все бакеты пустыми связными списками.
     */
    private void initializeBuckets() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

}

package org.example.seckill.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class UnionFind {
    private static class Element<V> {
        public V value;

        public Element(V value) {
            this.value = value;
        }
    }

    public static class UnionFindSet<V> {
        // 将用户传递过来的值转化为并查集中的对象 存放所有的元素
        public HashMap<V, Element<V>> elementMap;
        // 记录当前节点的父节点 son-father
        public HashMap<Element<V>, Element<V>> parentMap;
        // 记录某个集合的大小（代表元素所在的集合的大小）
        public HashMap<Element<V>, Integer> sizeMap;

        public UnionFindSet(List<V> values) {
            elementMap = new HashMap<>();
            parentMap = new HashMap<>();
            sizeMap = new HashMap<>();
            for (V value : values) {
                Element<V> element = new Element<>(value);
                elementMap.put(value, element);
                parentMap.put(element, element);
                sizeMap.put(element, 1);
            }
        }

        /**
         * 查找当前节点的最上面父节点
         * @param curr
         * @return
         */
        private Element<V> findHead(Element<V> curr) {
            Stack<Element<V>> stack = new Stack<>();
            while (curr != parentMap.get(curr)) {
                stack.push(curr);
                curr = parentMap.get(curr);
            }
            // 扁平化
            while (!stack.isEmpty()) {
                parentMap.put(stack.pop(), curr);
            }
            return curr;
        }

        /**
         * 判断两个元素是否在同一个集合中
         * @param a
         * @param b
         * @return
         */
        public boolean isSameSet(V a, V b) {
            if (elementMap.containsKey(a) && elementMap.containsKey(b)) {
                // 如果a和b都在并查集中 并且a和b的最上面的父节点是同一个节点，那么a和b就在同一个集合中
                return findHead(elementMap.get(a)) == findHead(elementMap.get(b));
            } else {
                // 如果a或b不在并查集中 直接返回false
                return false;
            }
        }

        /**
         * 将a和b所在的集合合并
         * @param a
         * @param b
         */
        public void union(V a, V b) {
            if (elementMap.containsKey(a) && elementMap.containsKey(b)) {
                Element<V> aHead = findHead(elementMap.get(a));
                Element<V> bHead = findHead(elementMap.get(b));
                if (aHead != bHead) {
                    // 看看a所在的集合和b所在的集合 哪个元素个数多 元素少的挂在元素多的代表节点下
                    Element<V> big = sizeMap.get(aHead) >= sizeMap.get(bHead) ? aHead : bHead;
                    Element<V> small = big == aHead ? bHead : aHead;
                    parentMap.put(small, big);
                    sizeMap.put(big, sizeMap.get(big) + sizeMap.get(small));
                    sizeMap.remove(small);
                }
            }
        }
    }
}

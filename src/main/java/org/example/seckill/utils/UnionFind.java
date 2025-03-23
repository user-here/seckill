package org.example.seckill.utils;

import java.util.HashMap;
import java.util.List;

public class UnionFind {

    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        UnionFindInner unionFindInner = new UnionFindInner(equations.size() * 2);
        HashMap<String, Integer> map = new HashMap<>();
        int d = 0;

        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0);
            String b = equations.get(i).get(1);
            if (!map.containsKey(a)) {
                map.put(a, d++);
            }
            if (!map.containsKey(b)) {
                map.put(b, d++);
            }
            unionFindInner.union(map.get(a), map.get(b), values);
        }

        double[] result = new double[queries.size()];

        for (int i = 0; i < queries.size(); i++) {
            String a = queries.get(i).get(0);
            String b = queries.get(i).get(1);
            if (!map.containsKey(a) || !map.containsKey(b)) {
                result[i] = -1.0d;
            }

        }
        return result;
    }

    private class UnionFindInner {

        private int[] parent;
        private double[] weight;

        public UnionFindInner(int n) {
            parent = new int[n];
            weight = new double[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                weight[i] = 1.0;
            }
        }

        private int find(Integer i) {
            if (this.parent[i] != i) {
                int pre = this.parent[i];
                this.parent[i] = find(this.parent[i]);
                this.weight[i] = this.weight[i] * this.weight[pre];
            }
            return this.parent[i];
        }

        public void union(Integer a, Integer b, double[] value) {
            int fa = this.find(a);
            int fb = this.find(b);
            if (fa != fb) {
                this.parent[fb] = fa;
                this.weight[fb] = this.weight[a] * value[0] / this.weight[b];
            }
        }

        public double query(Integer a, Integer b) {
            if (this.find(a) != this.find(b)) {
                return -1.0d;
            } else {
                return this.weight[a] / this.weight[b];
            }
        }

    }

}

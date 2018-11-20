package com.lzp.core.mtask;

import android.support.v4.util.ArraySet;

public class MTaskList {
    private Node root;
    private Node cur;

    private ArraySet<Integer> keys = new ArraySet<>();

    public void insert(MTask task) {
        if (!keys.contains(task.name().hashCode())) {
            keys.add(task.name().hashCode());
            if (root == null) {
                cur = root = new Node(task);
            } else {
                cur.next = new Node(task);
                cur = cur.next;
            }
        }
    }

    public MTask pop() {
        if (root == null) return null;
        MTask result = root.value;
        Node del = root;
        root = root.next;
        del = null;
        return result;
    }

    private static class Node {
        public Node(MTask task) {
            this.value = task;
            this.next = null;
        }

        private MTask value;
        Node next;
    }
}

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

    private MTask pop() {
        if (root == null) return null;
        MTask result = root.value;
        Node del = root;
        root = root.next;
        del = null;
        return result;
    }

    public void exec() {
        _exec(root);
    }

    private void _exec(final Node cur) {
        if (cur != null) {
            //下一个task跟当前task有直接的依赖关系，下一次task需要等到这个task执行完之后才能执行
            if (cur.next != null && cur.next.value.isDependOn(cur.value.name())) {
                cur.value.getScheduler().scheduleDirect(new Runnable() {
                    @Override
                    public void run() {
                        cur.value.exec();
                        _exec(cur.next);
                    }
                });
            } else {
                cur.value.getScheduler().scheduleDirect(cur.value);
                _exec(cur.next);
            }
        }
    }

    private static class Node {
        private MTask value;
        private Node next;

        public Node(MTask task) {
            this.value = task;
            this.next = null;
        }
    }
}

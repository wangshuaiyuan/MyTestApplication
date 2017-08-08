package wsy.org.javacase;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * Created by wsy on 2017/8/4.
 */

public class TreeSetTest {

    public static void main(String[] args) {
        testTreeSet();
    }

    private static void testTreeSet() {
        //有序数据结构
        TreeSet<String> treeSet = new TreeSet<>();
        treeSet.add("b");
        treeSet.add("e");
        treeSet.add("c");
        treeSet.add("d");
        treeSet.add("f");
        treeSet.add("a");

        for (Iterator<String> iterator = treeSet.iterator(); iterator.hasNext(); ) {
            String value = iterator.next();
            System.out.println(value);
        }
    }
}

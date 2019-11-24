package base;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        //System.out.println("hello world!");
        testAddAll();
    }

    private static void testAddAll() {
        List list = new ArrayList();
        //报错
        list.addAll(null);

    }
}

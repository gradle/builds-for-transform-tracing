package org.example.app;

import org.apache.commons.text.numbers.DoubleFormat;
import org.example.util.Utils1;
import org.example.util.Utils2;

public class App {
    public static void main(String[] args) {
        System.out.println("Running app1");

        System.out.println(Utils1.loud("hey!"));
        System.out.println(DoubleFormat.PLAIN.builder().minusSign('~').build().apply(-1.0));

        System.out.println(Utils2.dup("there"));
    }
}

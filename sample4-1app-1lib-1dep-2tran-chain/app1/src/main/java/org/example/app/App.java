package org.example.app;

import org.apache.commons.text.numbers.DoubleFormat;
import org.example.util.Utils1;

public class App {
    public static void main(String[] args) {
        System.out.println("Running app1");
        System.out.println(Utils1.loud("hey!"));
        System.out.println(DoubleFormat.PLAIN.builder().minusSign('~').build().apply(-1.0));
    }
}

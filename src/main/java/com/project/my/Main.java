package com.project.my;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new Window().setVisible(true);
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    new Window().setVisible(true);
//                } catch (Exception ex) {
//                    System.out.println(ex.getMessage());
//                }
//            }
//        });
    }
}
// 0..7  - step                             -last comand
// 8..15 - take(neutralize poison)          -last comand
//16..23 - look                             -over ten
//24..31 - duration
//32..63 - not reflecs go to   012
//pois - +1                    7 3
//ston - +2                    654
//bot  - +3
//food - +4
//void - +5

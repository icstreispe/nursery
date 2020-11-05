package com.x13.synchro;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Synchronizer {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        File fs = new File ("p:\\media\\2.ixus1");
        File fd = new File ("d:\\media\\2.ixus1");
        Map ms = buildByName(fs, 1);
        Map md = buildByName(fd, 1);
        System.out.println ("Count: " + ms.size());

        System.out.format ("Size: %,d\n" , size (ms));

        System.out.println(fs.getAbsolutePath() + " \t\t<->\t\t " + fd.getAbsolutePath());
        compareBySize (ms, md);
    }

    private static void compareBySize(Map<String, File> ms, Map<String, File> md) {


        for (String k : ms.keySet()){
            File fs = ms.get(k);
            File fd = md.get(k);
            compare (fs, fd);
        }


        //+ pt cele din fd dar nu in fs
        Set<String> s = new TreeSet<>(md.keySet());
        s.removeAll(ms.keySet());
        for (String k : s){
            File fs = ms.get(k);
            File fd = md.get(k);
            compare (fs, fd);
        }
    }

    private static void compare(File fs, File fd) {
        if (fd == null){
            System.out.println (fs.getAbsolutePath() + " -> ");
            return;
        }
        if (fs == null){
            System.out.println ("\t\t\t\t\t\t\t<- " + fd.getAbsolutePath());
            return;
        }
        if (fs.length() == fd.length()){
            //System.out.println (fs.getAbsolutePath() + " = \t\t\t" + fd.getAbsolutePath());
        } else {
            System.out.println (fs.getAbsolutePath() + " <> \t\t\t" + fd.getAbsolutePath());
        }
    }

    private static long size(Map<String, File> m) {
        long l = 0;
        for (File f : m.values()){
            l += f.length();
        }
        return l;
    }

    private static Map buildBySize(File d, int level) {
        Map<String, File> files = new HashMap();
        for (File f : d.listFiles()) {
            String key = "" + f.length();
            if (!f.isDirectory()){
                if (!files.containsKey(key)) {
                    files.put(key, f);
                } else {
                    print(level, f);
                    System.out.print("\t");
                    print(level, files.get(key));
                    System.out.println();
                }
            }


            if (f.isDirectory()) {
                Map m = buildBySize(f, level);
                files.putAll(m);
            }

        }
        return files;
    }

    private static Map<String, File> buildByName(File d, int level) {
        Map<String, File> files = new HashMap();
        for (File f : d.listFiles()) {
            String key = "" + f.getName();
            if (!f.isDirectory()){
                if (!files.containsKey(key)) {
                    files.put(key, f);
                } else {
                    print(level, f);
                    //System.out.print("\t");
                    print(level, files.get(key));
                    //System.out.println();
                }
            }


            if (f.isDirectory()) {
                Map m = buildByName(f, level);
                files.putAll(m);
            }

        }
        return files;
    }

    private static void print(int level, File f) {

        String s = String.format("%" + (2 * level) + "s", " ");
        if (f.isDirectory()) {
            s = ANSI_BLUE + s + f.getName() + "/\t" + String.format("%,d", f.length());
        } else {
            s = ANSI_YELLOW + s + f.getName() + "\t" + String.format("%,d", f.length());
        }
        System.out.println(s);
    }
}

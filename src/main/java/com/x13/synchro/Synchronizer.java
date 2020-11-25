package com.x13.synchro;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


//TODO tr sincronizata si structura de directoare (poate separat inainte de a incepe sincroniz fis!!!!) ca altfel nu prea merge
public class Synchronizer {

    public static final String FILES_KEY = "files";
    private boolean REAL_SYNC = true;           //!!!!!!!!!!!!!!!!!!!! daca sa si faca syncronizarea cu adevarat!!!
    public static final String ROOT_SRC = "E:\\users\\mihai\\media\\00.skanhex";
    public static final String ROOT_DEST = "E:\\users\\mihai\\media\\00.skanhex - Copy";

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
        sync();
    }


    //compara si sync dir mai intai si apoi si fisierele
    private static void sync() {
        File fs = new File (ROOT_SRC);
        File fd = new File (ROOT_DEST);
        Map ms = buildByName(ROOT_SRC, fs, 1);
        Map md = buildByName(ROOT_DEST, fd, 1);
        System.out.println ("Count: " + ms.size());
        System.out.format ("Size: %,d\n" , size (ms));

        System.out.println(fs.getAbsolutePath() + " \t\t\t<->\t\t\t " + fd.getAbsolutePath());

        compareDirs("", ms, md);
        compareFiles((Map)ms.get(FILES_KEY), (Map)md.get(FILES_KEY));
    }


    private static void compareFiles(Map<String, File> ms, Map<String, File> md) {

        for (String k : ms.keySet()){
                File fs = ms.get(k);
                File fd = md.get(k);
                compare(fs, fd);
        }

        //+ cele din fd dar nu in fs
        Set<String> sd = new TreeSet<>(md.keySet());
        sd.removeAll(ms.keySet());
        for (String k : sd){
                File fs = ms.get(k);
                File fd = md.get(k);
                compare(fs, fd);
        }
    }


    private static void compareDirs(String key, Map<String, ?> fs, Map<String, ?> fd) {
        String pathD = getAbsolutePath(key, ROOT_DEST);
        String pathS = getAbsolutePath(key, ROOT_SRC);

        if (fs == null){                                            //deleted
            System.out.println (ANSI_RED + "[D]\t\t\t\t\t\t\t<- " + pathD);
            rm (pathD);
            return;
        } else if (fd == null){                                            //new
            System.out.println (ANSI_GREEN + "[N]" + pathS + " -> \t\t\t[" + pathD + "]");
            md(pathD);
            fd = new HashMap<>();   //fake pt continuare unitara
        }

        //sync content dirs
        for (String k : fs.keySet()) {
            if (k != FILES_KEY) {
                Map ms = (Map) fs.get(k);
                Map md = (Map) fd.get(k);
                compareDirs(k, ms, md);
            }
        }

        Set<String> sd = new TreeSet<>(fd.keySet());
        sd.removeAll(fs.keySet());
        for (String k : sd) {
            if (k != FILES_KEY) {
                Map ms = (Map) fs.get(k);
                Map md = (Map) fd.get(k);
                compareDirs(k, ms, md);
            }
        }

    }

    private static void compare(File fs, File fd) {
        if (fd == null){                                            //new
            System.out.println (ANSI_GREEN + "[N]" + fs.getAbsolutePath() + " -> \t\t\t[" + buildPath(fs.getAbsolutePath(), ROOT_SRC, ROOT_DEST) + "]");
            copy (fs.getAbsolutePath(), buildPath(fs.getAbsolutePath(), ROOT_SRC, ROOT_DEST));
            return;
        }
        if (fs == null){                                            //deleted
            System.out.println (ANSI_RED + "[D]\t\t\t\t\t\t\t<- " + fd.getAbsolutePath());      //removed
            rm (fd.getAbsolutePath());
            return;
        }

        if (fs.length() != fd.length()) {                            //modified
            System.out.println(ANSI_YELLOW + "[M]" + fs.getAbsolutePath() + fs.length() + " <> \t\t\t" + fd.getAbsolutePath() + fd.length());
            copy (fs.getAbsolutePath(), fd.getAbsolutePath());
        }

    }


    //rootDest + absolutePath-rootSrc
    private static String buildPath(String absolutePath, String rootSrc, String rootDest) {
        return rootDest + absolutePath.substring(rootSrc.length());
    }

    private static String getAbsolutePath(String relativePath, String root) {
        return root + relativePath;
    }

    //copiere prin suprascriere!
    //dar tr sa existe toate directoarele pana la fisier!
    private static void copy (String pathS, String pathD) {
        try {
            Path from = Paths.get(pathS);
            Path to = Paths.get(pathD);
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void md (String pathS){
        try {
            Path path = Paths.get(pathS);
            Files.createDirectories(path);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    //delete in cascada
    //TODO de inlocuit cu mutare in alt director
    private static void rm(String pathS) {

        File f = new File(pathS);
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                rm(c.getAbsolutePath());
            }
        }
        boolean b = f.setReadable(true);
         b = f.setWritable(true);
         b = f.delete();

    }

    private static long size(Map<String, File> m) {
        long l = 0;
        for (Object f : m.values()){
            if (f instanceof Map) {
                l += size((Map)f);
            } else {
                l += ((File)f).length();
            }
        }
        return l;
    }

    /* TODO
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
     */

    //construieste un map cu toate fis&dir din dir
    //la directoare tr ordine (si sunt structurate), dar fis pot fi toate la gramada
    private static Map<String, Object> buildByName(String root, File d, int level) {
        Map<String, Object> all = new HashMap();
        Map<String, File> files = new HashMap();
        all.put(FILES_KEY, files);

        for (File f : d.listFiles()) {

            if (!f.isDirectory()){
                String key = getRelativePath(root, f);
                files.put(key, f);

            } else if (f.isDirectory()) {

                Map m = buildByName(root, f, level+1);
                Map filesChild = (Map)m.get(FILES_KEY);
                m.remove(FILES_KEY);
                files.putAll(filesChild);

                String key = getRelativePath(root, f);
                all.put(key, m);
            }

        }
        return all;
    }



    private static String getRelativePath(String root, File f) {
        return f.getAbsolutePath().substring(root.length());
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

    private class Folders {
        Map files;
        Map folders;
    }
}

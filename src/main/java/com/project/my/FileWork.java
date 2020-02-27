package com.project.my;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileWork {
    private File file;

    public FileWork(String path){
        file = new File(path);
    }
    public int[] readValue() throws IOException {
        if (file.exists()) {
            FileReader fileReader = new FileReader(file);
            Scanner scn = new Scanner(fileReader);
            int[] d = new int[64];
            int i=0;
            while (scn.hasNext()){
                d[i] = Integer.parseInt(scn.next());
                i++;
            }
            fileReader.close();
            return d;
        } else {
            System.out.println(file.getPath());
            System.out.println("Файл не существует или путь указан неверно.");
            return null;
        }
    }
    void writeValue(String number) throws IOException {
        if(file.exists()){
            FileWriter fileWriter = new FileWriter(file);
            String str ="";
            str+=number;
            fileWriter.write(str);
            fileWriter.close();
        }else {
            System.out.println("Файл не существует или путь указан неверно.");
        }
    }
}

package com.project.my;

import java.io.IOException;
import java.util.Vector;

public class Logic {
    public final int BACKGROUND = 0;
    public final int STONE = 1;
    public final int FOOD = 2;
    public final int POISON = 3;
    public final int OLD_CEIL = 5;
    public final int YOUNG_CEIL = 4;
    //------------------------------------------
    public final int WIDTH = 45;
    public final int HEIGHT = 25;
    public final int START_COUNT_FOOD = 64;
    public final int START_COUNT_POISON = 32;
    public final int MAX_HEALTH =90;
    public final int START_HEALTH =20;
    public final int MAX_CELLS = 64;
    public final int MIN_GEN_MUT = 4;
    public final int MAX_GEN_NUT = 4;
    private int[] startDNK = new int[64];
    private Cell[] cells;
    private int countLivingCells;
    private int numberGeneration;

    private int countFood = 0;


    public Logic() {
        cells = new Cell[MAX_CELLS];
        for(int i=0;i<MAX_CELLS;i++){
            cells[i] = new Cell();
        }
        for(int i=0;i<64;i++){
            startDNK[i] = 3;
        }
    }

    public void createField(int arr[][]) {
        String strField =
                "#############################################" +
                "#00000000#0000000000000000000000000000000000#" +
                "#00000000#0000000000000000000000000000000000#" +
                "#00000000#0000000000000000000000000000000000#" +
                "#00000000#0000000000000000000000000000000000#" +
                "#00000000#0000000000000000000000000000000000#" +
                "#00000000#0000000000000000000000000000000000#" +
                "#00000000#0000000000000#00000000000000000000#" +
                "#0000000000000000000000#00000000000000000000#" +
                "#0000000000000000000000#00000000000000000000#" +
                "#0000000000000000000000#00000000000000000000#" +
                "#0000000000000000000000#00000000000000000000#" +
                "#0000000000000000000000#00000000000000000000#" +
                "#0000000000000000000000#00000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000######000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#0000000000000000000000000000000000000000000#" +
                "#############################################";
        numberGeneration = 1;
        countFood = 0;
        for (int i=0;i<HEIGHT;i++){
            for(int j=0;j<WIDTH;j++){
                switch (strField.charAt(i*WIDTH+j)){
                    case '#':arr[i][j]=STONE;
                    break;
                    case '0':arr[i][j]=BACKGROUND;
                    break;
                }
            }
        }
        fillAt(FOOD,START_COUNT_FOOD,arr);
        fillAt(POISON,START_COUNT_POISON,arr);
        for(int i=0;i<MAX_CELLS;i++) {
            int iCheck = (int) (Math.random() * HEIGHT);
            int jCheck = (int) (Math.random() * WIDTH);
            if (arr[iCheck][jCheck] == 0) {
                cells[i].generateNewCeil(MAX_HEALTH, START_HEALTH, (int) (Math.random() * 8), iCheck, jCheck, startDNK);
                arr[iCheck][jCheck] = OLD_CEIL;
            }else{
                i--;
            }
        }
        countLivingCells = MAX_CELLS;
    }
    private void fillAt(int object, int count, int[][] arr) {
        if(countFood<=START_COUNT_FOOD || object != FOOD) {
            for (int i = 0; i < count; i++) {
                int iCheck = (int) (Math.random() * HEIGHT);
                int jCheck = (int) (Math.random() * WIDTH);
                if (arr[iCheck][jCheck] == 0) {
                    arr[iCheck][jCheck] = object;
                } else {
                    i--;
                }
            }
            if (object == FOOD) {
                countFood += count;
            }
        }
    }
    //очищаем карту от старья, заполняем едой и ядом,формируем новое поколение с мутантами, заполняем новыми клетками
    public void createNewGenerationsAndRefresh(int[][] arr) throws IOException {
        Vector<Cell> bestCells = new Vector<Cell>();
        for(int i=0;i<MAX_CELLS;i++) {
            if(cells[i].getHealth() > 0){
                bestCells.add(cells[i]);
                arr[cells[i].getPositionForI()][cells[i].getPositionForJ()] = BACKGROUND;
            }
        }
        for(Cell item:bestCells){
            arr[item.getPositionForI()][item.getPositionForJ()] = 0;
        }
        for(int i=0;i<MAX_CELLS;i++){
            int iCheck =(int)(Math.random()*HEIGHT);
            int jCheck = (int)(Math.random()*WIDTH);
            if(arr[iCheck][jCheck] == 0) {
                cells[i].generateNewCeil(MAX_HEALTH, START_HEALTH, (int) (Math.random() * 8), iCheck, jCheck, bestCells.get(i % 8).getDnk());
                //[] - 0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7,0...
                arr[iCheck][jCheck] = OLD_CEIL;
            }else{
                i--;
            }
        }
        for(int i=0;i<8;i++){
            cells[i].mutation(MIN_GEN_MUT,MAX_GEN_NUT);
        }
        countLivingCells = MAX_CELLS;
        numberGeneration++;
    }
    public boolean isEnd(){
        return countLivingCells<9;
    }
     public void nextStep(int[][] arr) {
        for(int i=0;i<MAX_CELLS;i++) {
            if (cells[i].getHealth() > 0) {
                int count = 0;
                while (count < 10 && activityOneCell(cells[i], arr)) {
                    count++;
                    if (count == 10) {
                        cells[i].deleteHealth(1);
                    }
                }
                if (cells[i].getHealth() == 0) {
                    arr[cells[i].getPositionForI()][cells[i].getPositionForJ()] = BACKGROUND;
                    countLivingCells--;
                }
            }
            if(countLivingCells==8){
                return;
            }
        }
    }

    private boolean activityOneCell(Cell cell, int[][] arr) {
        if(cell.getCurrentDNKCell()>-1 &&cell.getCurrentDNKCell()<8){
            Coord coord = new Coord();
            coord = getCoordForNumber(cell.getCurrentDNKCell()%8,cell.getPositionForI(),cell.getPositionForJ());
            cell.increaseNumberDNK(getCurrentIncrease(coord.i,coord.j,arr));
            cell.deleteHealth(1);
            switch (getCurrentIncrease(coord.i,coord.j,arr)){
                case 1:
                    arr[coord.i][coord.j] = OLD_CEIL;
                    arr[cell.getPositionForI()][cell.getPositionForJ()] = BACKGROUND;
                    cell.setPositionForI(coord.i);
                    cell.setPositionForJ(coord.j);
                    fillAt(POISON,1,arr);
                    cell.deleteHealth(10);
                    break;
                case 4:
                    arr[coord.i][coord.j] = OLD_CEIL;
                    arr[cell.getPositionForI()][cell.getPositionForJ()] = BACKGROUND;
                    cell.setPositionForI(coord.i);
                    cell.setPositionForJ(coord.j);
                    fillAt(FOOD,1,arr);
                    cell.addHealth(10);
                    countFood--;
                    break;
                case 5:
                    arr[coord.i][coord.j] = OLD_CEIL;
                    arr[cell.getPositionForI()][cell.getPositionForJ()] = BACKGROUND;
                    cell.setPositionForI(coord.i);
                    cell.setPositionForJ(coord.j);
                    break;
            }
            return false;
        }
        if(cell.getCurrentDNKCell()>7 && cell.getCurrentDNKCell()<16){
            Coord coord = new Coord();
            coord = getCoordForNumber(cell.getCurrentDNKCell()%8,cell.getPositionForI(),cell.getPositionForJ());
            cell.increaseNumberDNK(getCurrentIncrease(coord.i,coord.j,arr));
            cell.deleteHealth(1);
            switch (getCurrentIncrease(coord.i,coord.j,arr)){
                case 1:
                    arr[coord.i][coord.j] = FOOD;
                    fillAt(POISON,1,arr);
                    countFood++;
                    break;
                case 4:
                    cell.addHealth(10);
                    fillAt(FOOD,1,arr);
                    arr[coord.i][coord.j] = BACKGROUND;
                    countFood--;
                    break;
            }
            return false;
        }
        if(cell.getCurrentDNKCell()>15 && cell.getCurrentDNKCell()<24){
            Coord coord = new Coord();
            coord = getCoordForNumber(cell.getCurrentDNKCell()%8,cell.getPositionForI(),cell.getPositionForJ());
            cell.increaseNumberDNK(getCurrentIncrease(coord.i,coord.j,arr));
            return true;
        }
        if(cell.getCurrentDNKCell()>23 && cell.getCurrentDNKCell()<32){
            cell.addDir(cell.getCurrentDNKCell());
            cell.increaseNumberDNK(1);
            return true;
        }
        if(cell.getCurrentDNKCell()>31 && cell.getCurrentDNKCell()<64){
            cell.increaseNumberDNK(cell.getCurrentDNKCell());
            return true;
        }
        return true;
    }
    private int getCurrentIncrease(int iPos, int jPos, int[][]arr) {
        switch (arr[iPos][jPos]){
            case POISON:
                return 1;
            case STONE:
                return 2;
            case OLD_CEIL:
                return 3;
            case FOOD:
                return 4;
            case BACKGROUND:
                return 5;
        }
        System.out.println("Если высветилось это сообщение то что-то идёт не так");
        return 0;
    }
    private Coord getCoordForNumber(int val, int startI, int startJ) {
        Coord coord = new Coord();
        switch (val) {
            case 0:
                coord.i = startI - 1;
                coord.j = startJ - 1;
                return coord;
            case 1:
                coord.i = startI - 1;
                coord.j = startJ;
                return coord;
            case 2:
                coord.i = startI - 1;
                coord.j = startJ + 1;
                return coord;
            case 3:
                coord.i = startI;
                coord.j = startJ + 1;
                return coord;
            case 4:
                coord.i = startI + 1;
                coord.j = startJ + 1;
                return coord;
            case 5:
                coord.i = startI + 1;
                coord.j = startJ;
                return coord;
            case 6:
                coord.i = startI + 1;
                coord.j = startJ - 1;
                return coord;
            case 7:
                coord.i = startI;
                coord.j = startJ - 1;
                return coord;
        }
        System.out.println("Если высветилось это сообщение то что-то идёт не так");
        coord.i = 0;
        coord.j = 1;
        return coord;
    }

    public int getNumberGeneration() {
        return numberGeneration;
    }

    public int getCountFood() {
        return countFood;
    }

    public int[] getDnk(){
        return cells[0].getDnk();
    }
    public void setGenom(int[] fileDNK, int[][] arr) {
        startDNK = fileDNK;
        createField(arr);
    }
}

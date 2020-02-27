package com.project.my;

public class Cell {
    private int maxHealth = 90;
    private int health;
    private int dir;
    private int positionForI;
    private int positionForJ;
    private int[] dnk;
    private int numberDNKCell;

    public Cell() {
        dnk = new int[64];
    }

    public void generateNewCeil(int maxHealth, int health, int dir, int positionForI, int positionForJ, int[] dnk) {
        this.maxHealth = maxHealth;
        this.health = health;
        this.dir = dir;
        this.positionForI = positionForI;
        this.positionForJ = positionForJ;
        this.numberDNKCell = 0;
        for(int i=0;i<dnk.length;i++){
            this.dnk[i] = dnk[i];
        }
    }

    public int getHealth() {
        return health;
    }

    public int[] getDnk() {
        return dnk;
    }
    public void mutation(int minGen, int maxGen){
        int countMut = (int)(Math.random()*minGen + Math.random()*(maxGen-minGen));
        for(int i=0;i<countMut;i++){
            int iPos = (int)(Math.random()*64);
            int numberChange = (int)(Math.random()*64);
            dnk[iPos] = numberChange;
        }
    }

    public int getPositionForI() {
        return positionForI;
    }

    public int getPositionForJ() {
        return positionForJ;
    }
    public int getCurrentDNKCell() {
        return dnk[numberDNKCell];
    }
    public void increaseNumberDNK(int val) {
        numberDNKCell += val;
        numberDNKCell %= 64;
    }
    public void addHealth(int val) {
        health += val;
        if(health>maxHealth){
            health = maxHealth;
        }
    }
    public void deleteHealth(int val){
        health -= val;
        if(health<0){
            health = 0;
        }
    }

    public void addDir(int val) {
        dir+=val;
        dir%=8;
    }

    public void setPositionForI(int positionForI) {
        this.positionForI = positionForI;
    }

    public void setPositionForJ(int positionForJ) {
        this.positionForJ = positionForJ;
    }
}

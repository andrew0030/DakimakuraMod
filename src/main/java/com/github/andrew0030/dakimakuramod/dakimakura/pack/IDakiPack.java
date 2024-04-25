package com.github.andrew0030.dakimakuramod.dakimakura.pack;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;

import java.util.ArrayList;

public interface IDakiPack
{
    String getResourceName();

    String getName();

    int getDakiCount();

    Daki getDaki(String dakiDirName);

    void addDaki(Daki daki);

    ArrayList<Daki> getDakisInPack();

    byte[]  getResource(String path);

    boolean resourceExists(String path);
}
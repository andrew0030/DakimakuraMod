package moe.plushie.dakimakuramod.common.dakimakura.pack;

import java.util.ArrayList;

import moe.plushie.dakimakuramod.common.dakimakura.Daki;

public interface IDakiPack {
    
    public String getResourceName();
    
    public String getName();
    
    public int getDakiCount();

    public Daki getDaki(String dakiDirName);

    public void addDaki(Daki daki);
    
    public ArrayList<Daki> getDakisInPack();
    
    public byte[]  getResource(String path);
    
    public boolean resourceExists(String path);
}

package com.github.andrew0030.dakimakuramod.dakimakura.pack;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.DakiManager;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;

public abstract class AbstractDakiPack implements IDakiPack
{
    protected final DakiManager dakiManager;
    private final String resourceName;
    protected final LinkedHashMap<String, Daki> dakiMap;

    protected AbstractDakiPack(String resourceName)
    {
        this.dakiManager = DakimakuraMod.getDakimakuraManager();
        this.resourceName = resourceName;
        this.dakiMap = new LinkedHashMap<>();
    }

    @Override
    public String getResourceName()
    {
        return this.resourceName;
    }

    @Override
    public int getDakiCount()
    {
        return this.dakiMap.size();
    }

    @Override
    public Daki getDaki(String dakiDirName)
    {
        return this.dakiMap.get(this.getResourceName() + ":" + dakiDirName);
    }

    @Override
    public void addDaki(Daki daki)
    {
        this.dakiMap.put(this.getResourceName() + ":" + daki.getDakiDirectoryName(), daki);
    }

    @Override
    public ArrayList<Daki> getDakisInPack()
    {
        return new ArrayList<>(dakiMap.values());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.resourceName == null) ? 0 : this.resourceName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        AbstractDakiPack other = (AbstractDakiPack) obj;
        return Objects.equals(this.resourceName, other.resourceName);
    }
}
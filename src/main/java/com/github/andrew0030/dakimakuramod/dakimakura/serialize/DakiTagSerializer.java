package com.github.andrew0030.dakimakuramod.dakimakura.serialize;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * Helper class to more easily handle storing and retrieving values, to and from a {@link CompoundTag}
 */
public final class DakiTagSerializer
{
    public static final String PACK_NAME_KEY = "PackName";
    public static final String DIR_NAME_KEY = "DirName";
    public static final String FACING_KEY = "Facing";
    public static final String FLIPPED_KEY = "Flipped";

    /**
     * Gets and stores the Daki pack name and directory name in a new {@link CompoundTag}
     * @param daki The Daki object that will be serialized
     * @return A new {@link CompoundTag} containing the pack name and directory name
     */
    public static CompoundTag serialize(Daki daki)
    {
        return DakiTagSerializer.serialize(daki, new CompoundTag());
    }

    /**
     * Gets and stores the {@link Daki} pack name and directory name in a given {@link CompoundTag}
     * @param daki The {@link Daki} object that will be serialized
     * @param compound The {@link CompoundTag} that will hold the values
     * @return The given {@link CompoundTag} containing the {@link Daki} pack name and directory name
     */
    public static CompoundTag serialize(Daki daki, CompoundTag compound)
    {
        if(compound == null)
            return DakiTagSerializer.serialize(daki);
        compound.putString(PACK_NAME_KEY, daki.getPackDirectoryName());
        compound.putString(DIR_NAME_KEY, daki.getDakiDirectoryName());
        return compound;
    }

    /**
     * Gets the {@link Daki} pack name and directory name from a given {@link CompoundTag}
     * @param compound The {@link CompoundTag} containing the {@link Daki} pack name and directory name
     * @return The {@link Daki} which corresponds to the given {@link CompoundTag} pack name and directory name
     */
    public static Daki deserialize(CompoundTag compound)
    {
        if (compound == null)
            return null;
        if (compound.contains(PACK_NAME_KEY, Tag.TAG_STRING) && compound.contains(DIR_NAME_KEY, Tag.TAG_STRING))
        {
            String packName = compound.getString(PACK_NAME_KEY);
            String dirName = compound.getString(DIR_NAME_KEY);
            return DakimakuraMod.getDakimakuraManager().getDakiFromMap(packName, dirName);
        }
        return null;
    }

    /**
     * Sets the given value to be the "Flipped" value on a given {@link CompoundTag}
     * @param compound The {@link CompoundTag} that will receive the "Flipped" Tag
     * @param flipped The value the "Flipped" Tag will have
     */
    public static void setFlipped(CompoundTag compound, boolean flipped)
    {
        if (compound == null)
            return;
        compound.putBoolean(FLIPPED_KEY, flipped);
    }

    /**
     * Gets the value of "Flipped" from a given {@link CompoundTag}
     * @param compound The {@link CompoundTag} containing the "Flipped" Tag
     * @return The value stored in the "Flipped" Tag
     */
    public static boolean isFlipped(CompoundTag compound)
    {
        if (compound == null)
            return false;
        return compound.getBoolean(FLIPPED_KEY);
    }
}
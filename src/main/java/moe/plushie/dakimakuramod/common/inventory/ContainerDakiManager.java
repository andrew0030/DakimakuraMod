package moe.plushie.dakimakuramod.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerDakiManager extends Container {

    private final EntityPlayer player;
    
    public ContainerDakiManager(EntityPlayer player) {
        this.player = player;
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}

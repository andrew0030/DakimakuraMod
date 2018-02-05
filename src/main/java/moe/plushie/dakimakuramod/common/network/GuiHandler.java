package moe.plushie.dakimakuramod.common.network;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.client.gui.GuiDakiManager;
import moe.plushie.dakimakuramod.common.inventory.ContainerDakiManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

    public GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(DakimakuraMod.getInstance(), this);
    }
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new ContainerDakiManager(player);
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return new GuiDakiManager();
    }
}

package moe.plushie.dakimakuramod.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDakimakura extends AbstractModItem {

    public ItemDakimakura() {
        super("dakimakura");
        setHasSubtypes(true);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(LibModInfo.ID + ":dakimakura");
    }
    
    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        EntityDakimakura entityDakimakura = new EntityDakimakura(world, x, y, z, stack.getItemDamage(), player.isSneaking());
        world.spawnEntityInWorld(entityDakimakura);
        return false;
    }
}

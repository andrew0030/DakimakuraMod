package moe.plushie.dakimakuramod.common.items;

import java.util.List;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractModItem extends Item {

    public AbstractModItem(String name) {
        this(name, true);
    }
    
    public AbstractModItem(String name, boolean addCreativeTab) {
        if (addCreativeTab) {
            setCreativeTab(DakimakuraMod.creativeTabDakimakura);
        }
        setUnlocalizedName(name);
        setHasSubtypes(false);
        setMaxStackSize(1);
        setNoRepair();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips) {
        String unlocalized;
        String localized;
        unlocalized = itemStack.getUnlocalizedName() + ".tooltip";
        /*
        localized = StatCollector.translateToLocal(unlocalized);
        if (!unlocalized.equals(localized)) {
            if (localized.contains("%n")) {
                String[] split = localized.split("%n");
                for (int i = 0; i < split.length; i++) {
                    list.add(split[i]);
                }
            } else {
                list.add(localized);
            }
        }
        */
        super.addInformation(itemStack, player, list, advancedItemTooltips);
    }
    
    @Override
    public Item setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        //setRegistryName(new ResourceLocation(LibModInfo.ID, "item." + name));
        GameRegistry.register(this, new ResourceLocation(LibModInfo.ID, "item." + name));
        return this;
    }
    
    @Override
    public String getUnlocalizedName() {
        return getModdedUnlocalizedName(super.getUnlocalizedName());
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return getModdedUnlocalizedName(super.getUnlocalizedName(itemStack), itemStack);
    }

    protected String getModdedUnlocalizedName(String unlocalizedName) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        if (hasSubtypes) {
            return "item." + LibModInfo.ID.toLowerCase() + ":" + name + ".0";
        } else {
            return "item." + LibModInfo.ID.toLowerCase() + ":" + name;
        }
    }
    
    protected String getModdedUnlocalizedName(String unlocalizedName, ItemStack stack) {
        String name = unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
        if (hasSubtypes) {
            return "item." + LibModInfo.ID.toLowerCase() + ":" + name + "." + stack.getItemDamage();
        } else {
            return "item." + LibModInfo.ID.toLowerCase() + ":" + name;
        }
    }
}

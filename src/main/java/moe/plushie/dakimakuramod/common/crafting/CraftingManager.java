package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = LibModInfo.ID)
public final class CraftingManager {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeDaki().setRegistryName(new ResourceLocation(LibModInfo.ID, "daki_design")));
        if (ConfigHandler.enableRecycleRecipe) {
            event.getRegistry().register(new RecipeDakiRecycle().setRegistryName(new ResourceLocation(LibModInfo.ID, "design_recycle")));
        }
        
        if (ConfigHandler.enableClearingRecipe) {
            event.getRegistry().register(new RecipeDakiClear().setRegistryName(new ResourceLocation(LibModInfo.ID, "daki_clear")));
        }
        
        if (ConfigHandler.enableRecipe) {
            if (!ConfigHandler.useAltRecipe) {
                event.getRegistry().register(new ShapedOreRecipe(null, new ItemStack(ModBlocks.blockDakimakura, 1, 0), new Object[] {
                        "ww",
                        "ww",
                        "ww",
                        'w', new ItemStack(Blocks.WOOL, 1, 0)
                        }).setRegistryName(new ResourceLocation(LibModInfo.ID, "daki")));
            } else {
                event.getRegistry().register(new ShapedOreRecipe(null, new ItemStack(ModBlocks.blockDakimakura, 1, 0), new Object[] {
                        "sws",
                        "sws",
                        "sws",
                        'w', new ItemStack(Blocks.WOOL, 1, 0),
                        's', "string"
                        }).setRegistryName(new ResourceLocation(LibModInfo.ID, "daki-alt")));
            }
        }
    }
}

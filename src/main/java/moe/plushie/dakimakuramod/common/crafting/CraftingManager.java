package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LibModInfo.ID)
public final class CraftingManager {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeDaki().setRegistryName(new ResourceLocation(LibModInfo.ID, "daki_design")));
        event.getRegistry().register(new RecipeDakiRecycle().setRegistryName(new ResourceLocation(LibModInfo.ID, "design_recycle")));
    }
    
    public static void init() {
        /*
        RecipeSorter.INSTANCE.register("dakimakuramod:daki", RecipeDaki.class, Category.SHAPELESS, "after:minecraft:shapeless");
        GameRegistry.addRecipe(new RecipeDaki());
        
        if (ConfigHandler.enableRecycleRecipe) {
            RecipeSorter.INSTANCE.register("dakimakuramod:dakirecycle", RecipeDakiRecycle.class, Category.SHAPELESS, "after:minecraft:shapeless");
            GameRegistry.addRecipe(new RecipeDakiRecycle());
        }
        
        if (ConfigHandler.enableRecipe) {
            if (!ConfigHandler.useAltRecipe) {
                addShapedRecipe(new ItemStack(ModBlocks.blockDakimakura, 1, 0), new Object[] {
                        "ww",
                        "ww",
                        "ww",
                        'w', new ItemStack(Blocks.WOOL, 1, 0)
                        });
            } else {
                addShapedRecipe(new ItemStack(ModBlocks.blockDakimakura, 1, 0), new Object[] {
                        "sws",
                        "sws",
                        "sws",
                        'w', new ItemStack(Blocks.WOOL, 1, 0),
                        's', "string"
                        });
            }
        }
        */
    }
    /*
    public static void addShapelessRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
    }

    public static void addShapedRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
    }
    */
}

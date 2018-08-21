package moe.plushie.dakimakuramod.common.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class CraftingManager {

    public static void init() {
        RecipeSorter.INSTANCE.register("dakimakuramod:shapeless", RecipeDaki.class, Category.SHAPELESS, "after:minecraft:shapeless");
        GameRegistry.addRecipe(new RecipeDaki());
        
        if (ConfigHandler.enableRecycleRecipe) {
            RecipeSorter.INSTANCE.register("dakimakuramod:shapeless", RecipeDakiRecycle.class, Category.SHAPELESS, "after:minecraft:shapeless");
            GameRegistry.addRecipe(new RecipeDakiRecycle());
        }
        
        if (ConfigHandler.enableClearingRecipe) {
            RecipeSorter.INSTANCE.register("dakimakuramod:shapeless", RecipeDakiClear.class, Category.SHAPELESS, "after:minecraft:shapeless");
            GameRegistry.addRecipe(new RecipeDakiClear());
        }
        
        if (ConfigHandler.enableRecipe) {
            if (!ConfigHandler.useAltRecipe) {
                addShapedRecipe(new ItemStack(ModBlocks.blockDakimakura, 1, 0), new Object[] {
                        "ww",
                        "ww",
                        "ww",
                        'w', new ItemStack(Blocks.wool, 1, 0)
                        });
            } else {
                addShapedRecipe(new ItemStack(ModBlocks.blockDakimakura, 1, 0), new Object[] {
                        "sws",
                        "sws",
                        "sws",
                        'w', new ItemStack(Blocks.wool, 1, 0),
                        's', new ItemStack(Items.string, 1, 0)
                        });
            }
        }
    }

    public static void addShapelessRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
    }

    public static void addShapedRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
    }
}

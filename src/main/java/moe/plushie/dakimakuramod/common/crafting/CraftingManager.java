package moe.plushie.dakimakuramod.common.crafting;

import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class CraftingManager {

    public static void init() {
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
    }

    public static void addShapelessRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
    }

    public static void addShapedRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
    }
}

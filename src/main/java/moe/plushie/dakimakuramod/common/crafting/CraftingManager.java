package moe.plushie.dakimakuramod.common.crafting;

import cpw.mods.fml.common.registry.GameRegistry;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class CraftingManager {

    public static void init() {
        addShapedRecipe(new ItemStack(ModBlocks.blockDakimakura, 1, 0), new Object[] {
                "ww",
                "ww",
                "ww",
                'w', new ItemStack(Blocks.wool, 1, 0)});
    }

    public static void addShapelessRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapelessOreRecipe(result, recipe));
    }

    public static void addShapedRecipe(ItemStack result, Object[] recipe) {
        GameRegistry.addRecipe(new ShapedOreRecipe(result, recipe));
    }
}

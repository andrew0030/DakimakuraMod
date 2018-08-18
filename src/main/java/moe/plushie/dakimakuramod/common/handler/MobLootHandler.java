package moe.plushie.dakimakuramod.common.handler;

import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.items.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MobLootHandler {
    
    public MobLootHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent(priority=EventPriority.LOW)
    public void onLivingDropsEvent(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityMob) {
            EntityLivingBase entity = event.getEntityLiving();
            World world = entity.getEntityWorld();
            if (world.getGameRules().getBoolean("doMobLoot")) {
                float dropChance = ConfigHandler.mobDropChance;
                float lootingBonus = ConfigHandler.mobDropLootingBonus;
                if (dropChance > 0F) {
                    dropChance += lootingBonus * (float)event.getLootingLevel();
                    if (world.rand.nextFloat() * 100F <= dropChance) {
                        ItemStack itemStack = new ItemStack(ModItems.dakiDesign);
                        EntityItem entityItem = new EntityItem(world, entity.posX, entity.posY, entity.posZ, itemStack);
                        event.getDrops().add(entityItem);
                    }
                }
            }
        }
    }
}

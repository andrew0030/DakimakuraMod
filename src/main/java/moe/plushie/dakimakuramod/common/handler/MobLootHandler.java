package moe.plushie.dakimakuramod.common.handler;

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
        /*
        if (event.entity instanceof EntityMob) {
            EntityLivingBase entity = event.entityLiving;
            World world = entity.worldObj;
            if (world.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                float dropChance = ConfigHandler.mobDropChance;
                float lootingBonus = ConfigHandler.mobDropLootingBonus;
                if (dropChance > 0F) {
                    dropChance += lootingBonus * (float)event.lootingLevel;
                    if (world.rand.nextFloat() * 100F <= dropChance) {
                        ItemStack itemStack = new ItemStack(ModItems.dakiDesign);
                        EntityItem entityItem = new EntityItem(world, entity.posX, entity.posY, entity.posZ, itemStack);
                        event.drops.add(entityItem);
                    }
                }
            }
        }
        */
    }
}

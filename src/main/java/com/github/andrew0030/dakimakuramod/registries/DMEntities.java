package com.github.andrew0030.dakimakuramod.registries;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.entities.dakimakura.Dakimakura;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DMEntities
{
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DakimakuraMod.MODID);

    public static final RegistryObject<EntityType<Dakimakura>> DAKIMAKURA = ENTITIES.register("dakimakura", () -> EntityType.Builder.of(Dakimakura::new, MobCategory.MISC).sized(3.0F, 0.25F).clientTrackingRange(10).build(new ResourceLocation(DakimakuraMod.MODID, "dakimakura").toString()));
}
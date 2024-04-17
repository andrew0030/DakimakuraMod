package com.github.andrew0030.dakimakuramod.registries;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.block_entities.dakimakura.DakimakuraBlockEntity;
import com.github.andrew0030.dakimakuramod.block_entities.dakimakura.DakimakuraBlockEntityRenderer;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DMBlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DakimakuraMod.MODID);

    public static final RegistryObject<BlockEntityType<DakimakuraBlockEntity>> DAKIMAKURA = BLOCK_ENTITY_TYPES.register("dakimakura", () -> new BlockEntityType<>(DakimakuraBlockEntity::new, Sets.newHashSet(DMBlocks.DAKIMAKURA.get()), null));

    public static void registerBlockEntityRenderers()
    {
        BlockEntityRenderers.register(DAKIMAKURA.get(), DakimakuraBlockEntityRenderer::new);
    }
}
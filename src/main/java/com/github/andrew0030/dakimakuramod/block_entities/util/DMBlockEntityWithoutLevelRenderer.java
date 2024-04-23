package com.github.andrew0030.dakimakuramod.block_entities.util;

import com.github.andrew0030.dakimakuramod.block_entities.dakimakura.DakimakuraBlockEntity;
import com.github.andrew0030.dakimakuramod.registries.DMBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

public class DMBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer
{
    private final Lazy<DakimakuraBlockEntity> blockEntity = Lazy.of(() -> new DakimakuraBlockEntity(BlockPos.ZERO, DMBlocks.DAKIMAKURA.get().defaultBlockState()));

    public DMBlockEntityWithoutLevelRenderer()
    {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
//        this.blockEntity.get().setFlipped(DakiNbtSerializer.isFlipped(stack.getTag()));
        Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(this.blockEntity.get(), poseStack, buffer, packedLight, packedOverlay);
    }
}
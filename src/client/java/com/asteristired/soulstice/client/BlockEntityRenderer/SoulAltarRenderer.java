package com.asteristired.soulstice.client.BlockEntityRenderer;

import com.asteristired.soulstice.BlockEntities.SoulAltarBlockEntity;
import com.asteristired.soulstice.Items.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

import java.util.Objects;

public class SoulAltarRenderer implements BlockEntityRenderer<SoulAltarBlockEntity> {

    public SoulAltarRenderer(BlockEntityRendererFactory.Context context) {
    }
    @Override
    public void render(SoulAltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack stack = new ItemStack(ModItems.SOUL);
        MinecraftClient client = MinecraftClient.getInstance();
        if (!stack.isEmpty()) {
            matrices.push();

            float X = 0.5f;
            float Y = 1;
            float Z = 0.5f;

            matrices.translate(X, Y, Z);
            matrices.scale(2f, 2f, 2f);

            // do rotation
            long time = Objects.requireNonNull(entity.getWorld()).getTime();
            float rotationSpeed = 0.05f;
            float angle = (time + tickDelta) * rotationSpeed;
            angle = angle % (2 * (float)Math.PI);

            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(angle));

            client.getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, null, 0);

            matrices.pop();
        }
    }
}

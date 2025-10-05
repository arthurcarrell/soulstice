package com.asteristired.soulstice.client;

import com.asteristired.soulstice.BlockEntities.ModBlockEntities;
import com.asteristired.soulstice.client.BlockEntityRenderer.SoulAltarRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoulsticeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntities.SOUL_ALTAR, SoulAltarRenderer::new);
    }
}

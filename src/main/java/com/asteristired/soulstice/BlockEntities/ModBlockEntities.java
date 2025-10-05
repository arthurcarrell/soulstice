package com.asteristired.soulstice.BlockEntities;

import com.asteristired.soulstice.Blocks.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.asteristired.soulstice.Soulstice.MOD_ID;

public class ModBlockEntities {
    public static BlockEntityType<SoulAltarBlockEntity> SOUL_ALTAR = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(MOD_ID, "soul_altar"),
            FabricBlockEntityTypeBuilder.create(
                    SoulAltarBlockEntity::new,
                    ModBlocks.SOUL_ALTAR
            ).build()
    );

    public static void Initalise() {}
}
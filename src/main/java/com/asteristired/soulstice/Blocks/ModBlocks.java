package com.asteristired.soulstice.Blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import static com.asteristired.soulstice.Soulstice.MOD_ID;

public class ModBlocks {
    public static Block SOUL_ALTAR = Register(new SoulAltar(AbstractBlock.Settings.create().nonOpaque().strength(0.5f)), "soul_altar");
    public static BlockItem SOUL_ALTAR_ITEM = Register(new BlockItem(SOUL_ALTAR, new Item.Settings()), "soul_altar");

    public static Block Register(Block block, String id) {
        return Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, id), block);
    }

    public static BlockItem Register(BlockItem block, String id) {
        return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), block);
    }

    public static void AddToCreativeMenu() {
        //ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(itemGroup -> {
           // itemGroup.addAfter(Items.SPAWNER, ModBlocks.SOUL_ALTAR_ITEM);
        //});
    }

    public static void Initalise() {}

}
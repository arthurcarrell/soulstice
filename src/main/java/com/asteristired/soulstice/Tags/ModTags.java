package com.asteristired.soulstice.Tags;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import static com.asteristired.soulstice.Soulstice.MOD_ID;

public class ModTags {
    public static final TagKey<Item> SOULSPARKER_INPUT = TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, "soulsparker_input"));


    public static class Soulsparker {
        public static final TagKey<EntityType<?>> UNDEAD = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MOD_ID, "soulsparker/undead"));
        public static final TagKey<EntityType<?>> FIRE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MOD_ID, "soulsparker/fire"));
        public static final TagKey<EntityType<?>> WITHER = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MOD_ID, "soulsparker/wither"));
        public static final TagKey<EntityType<?>> HEAL = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MOD_ID, "soulsparker/heal"));
        public static final TagKey<EntityType<?>> SLOW = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(MOD_ID, "soulsparker/slow"));
    }
    public static void Initalise() {}
}

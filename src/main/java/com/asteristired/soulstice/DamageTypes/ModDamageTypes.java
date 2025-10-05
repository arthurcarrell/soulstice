package com.asteristired.soulstice.DamageTypes;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static com.asteristired.soulstice.Soulstice.MOD_ID;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> SOULSPARKER_FIRE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(MOD_ID, "soulsparker_fire"));
    public static void Initalise() {}
}


package com.asteristired.soulstice.StatusEffects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.asteristired.soulstice.Soulstice.MOD_ID;

public class ModStatusEffects {

    public static StatusEffect SOUL = Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "soul"), new SoulEffect());
    public static void Initalise() {}
}

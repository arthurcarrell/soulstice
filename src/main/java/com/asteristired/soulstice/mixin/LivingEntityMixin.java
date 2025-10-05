package com.asteristired.soulstice.mixin;

import com.asteristired.soulstice.Interface.SoulTrackerAccessor;
import com.asteristired.soulstice.StatusEffects.ModStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);


    @Inject(method = "dropLoot", at = @At("HEAD"), cancellable = true)
    private void DropLootCheck(DamageSource damageSource, boolean causedByPlayer, CallbackInfo ci) {
        if (this.hasStatusEffect(ModStatusEffects.SOUL)) {
            ci.cancel();
        }
    }

    @Inject(method = "dropXp", at = @At("HEAD"), cancellable = true)
    private void DropXpCheck(CallbackInfo ci) {
        if (this.hasStatusEffect(ModStatusEffects.SOUL)) {
            ci.cancel();
        }
    }
}

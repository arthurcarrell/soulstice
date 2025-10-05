package com.asteristired.soulstice.Items.ItemClasses;

import com.asteristired.soulstice.Interface.SoulTrackerAccessor;
import com.asteristired.soulstice.StatusEffects.ModStatusEffects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ModStatus;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Soul extends Item {
    public Soul(Settings settings) {
        super(settings);
    }

    public EntityType<?> getStoredEntityType(ItemStack itemStack) {
        if (itemStack.getOrCreateNbt().contains("StoredEntity")) {
            assert itemStack.getNbt() != null;
            return EntityType.get(itemStack.getNbt().getString("StoredEntity")).orElse(null);
        }
        return null;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        EntityType<?> entityType = getStoredEntityType(stack);

        if (entityType != null) {
            // add the tooltip saying the stored mob.
            tooltip.add(entityType.getName().copyContentOnly().formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("tooltip.soulstice.soul.no_mob").formatted(Formatting.RED));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            ServerWorld serverWorld = (ServerWorld) world;
            EntityType<?> entityType = getStoredEntityType(user.getStackInHand(hand));
            if (entityType != null) {
                Entity entity = entityType.spawn(serverWorld, user.getBlockPos(), SpawnReason.SPAWN_EGG);
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SOUL, 10000000, 1, false, false, false));
                    serverWorld.spawnParticles(ParticleTypes.SOUL, entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.2, 0.5, 0.2, 0);
                }

            }
        }

        if (!user.isCreative()) {
            user.getStackInHand(hand).decrement(1);
        }
        return super.use(world, user, hand);
    }
}

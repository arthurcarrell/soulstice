package com.asteristired.soulstice.ServerEvents;

import com.asteristired.soulstice.Items.ItemClasses.Soul;
import com.asteristired.soulstice.Items.ModItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.apache.logging.log4j.core.jmx.Server;

public class AfterDeath {

    public static void Initalise() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity.getWorld().isClient) return;
            if (entity instanceof LivingEntity) {
                if (entity.getAttacker() instanceof LivingEntity) {
                    LivingEntity attacker = entity.getAttacker();
                    if (attacker.isHolding(ModItems.SOULCLEAVE)) {
                        EntityType<?> entityType = entity.getType();

                        // create the soul item
                        ItemStack soulItem = ModItems.SOUL.getDefaultStack();
                        soulItem.getOrCreateNbt().putString("StoredEntity", Registries.ENTITY_TYPE.getKey(entityType).get().getValue().toString());

                        // spawn it
                        ItemEntity item = new ItemEntity(entity.getWorld(), entity.getX(), entity.getY(), entity.getZ(), soulItem);
                        entity.getWorld().spawnEntity(item);
                    }
                }
            }
        });


    }
}

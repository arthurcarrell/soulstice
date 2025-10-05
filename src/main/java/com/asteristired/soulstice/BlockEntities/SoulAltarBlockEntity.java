package com.asteristired.soulstice.BlockEntities;

import com.asteristired.soulstice.Interface.SoulTrackerAccessor;
import com.asteristired.soulstice.StatusEffects.ModStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import static com.asteristired.soulstice.Soulstice.LOGGER;

import java.util.ArrayList;
import java.util.List;

public class SoulAltarBlockEntity extends BlockEntity {
    public static final BooleanProperty ACTIVE = BooleanProperty.of("active");

    int tickCooldown = 0;
    public SoulAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SOUL_ALTAR, pos, state);
    }

    private void spawnMob(EntityType<?> entityType, World world, BlockPos blockPos) {

        int randomX = Random.create().nextBetween(-7,7);
        int randomZ = Random.create().nextBetween(-7,7);
        blockPos = blockPos.add(randomX, 0, randomZ);
        if (!world.isClient) {
            Entity entity = entityType.spawn((ServerWorld) world, blockPos, SpawnReason.MOB_SUMMONED);
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.addStatusEffect(new StatusEffectInstance(ModStatusEffects.SOUL, 10000, 0));
                //livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, Integer.MAX_VALUE, 0));
                ((ServerWorld) world).spawnParticles(ParticleTypes.SOUL, entity.getX(), entity.getY()+1, entity.getZ(), 100, 0.2, 0.5, 0.2, 0);
                livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10000, 0));
            }
        }
    }

    public void activate(World world, BlockPos blockPos, int difficulty) {

        tickCooldown = 20;
        List<EntityType<?>> eligibleTypes = new ArrayList<>();
        eligibleTypes.add(EntityType.SKELETON);
        eligibleTypes.add(EntityType.WITHER_SKELETON);
        eligibleTypes.add(EntityType.BLAZE);
        eligibleTypes.add(EntityType.MAGMA_CUBE);
        eligibleTypes.add(EntityType.ENDERMAN);
        eligibleTypes.add(EntityType.HOGLIN);


        Random random = Random.create();
        for (int i=0; i < 5; i++) {
            EntityType<?> entityType = eligibleTypes.get(random.nextBetween(0,eligibleTypes.size()-1));
            spawnMob(entityType, world, blockPos);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        //Inventories.writeNbt(nbt, items);
        //System.out.println("Saving pot items: " + items);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        //Inventories.readNbt(nbt,items);
        //System.out.println("Loading pot items: " + items);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        writeNbt(nbt);
        return nbt;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        // pass

        if (state.get(ACTIVE)) {
            int amount = 0;
            for (LivingEntity arenaEntity : world.getEntitiesByClass(LivingEntity.class, new Box(pos.getX()-30, pos.getY()-30, pos.getZ()-30, pos.getX()+30, pos.getY()+30, pos.getZ()+30), livingEntity -> livingEntity.hasStatusEffect(StatusEffects.GLOWING))) {
                amount++;
            }

            LOGGER.info(String.valueOf(amount));
            LOGGER.info(String.valueOf(tickCooldown));
            if (amount == 0 && tickCooldown <= 0) {
                world.setBlockState(pos, state.with(ACTIVE, false));
                LOGGER.info("disabling ACTIVE, as every enemy is dead");
            }
        }

        if (tickCooldown > 0) {
            tickCooldown--;
        }
    }

    public boolean isActive() {
        assert world != null;
        return world.getBlockState(this.getPos()).get(ACTIVE);
    }
}

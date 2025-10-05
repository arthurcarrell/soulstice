package com.asteristired.soulstice.Items.ItemClasses;

import com.asteristired.soulstice.DamageTypes.ModDamageTypes;
import com.asteristired.soulstice.Items.ModItems;
import com.asteristired.soulstice.Tags.ModTags;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.asteristired.soulstice.Soulstice.LOGGER;
public class Soulsparker extends Item {

    private final int HEAT_LIMIT = 100;
    private final int HEAT_INCREASE = 15;
    public Soulsparker(Settings settings) {
        super(settings);
    }

    public void placeItem(ItemStack stack, ItemStack itemToAdd) {
        // reduce the stack size to one


        NbtCompound nbt = stack.getNbt();
        ItemStack input = itemToAdd.copy();
        input.setCount(1);

        assert nbt != null;
        NbtCompound itemNbt = new NbtCompound();
        input.writeNbt(itemNbt);
        nbt.put("StoredItem", itemNbt);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xff7700;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        assert stack.getNbt() != null;
        int heat = GetOrMakeHeatNBT(stack);
        return Math.round(13.0f * ((float) heat / HEAT_LIMIT));
    }

    public EntityType<?> getStoredEntityType(ItemStack stack) {
        if (stack.getOrCreateNbt().contains("StoredItem")) {
            ItemStack soul = ItemStack.fromNbt(stack.getOrCreateNbt().getCompound("StoredItem"));
            if (soul.getOrCreateNbt().contains("StoredEntity")) {
                assert soul.getNbt() != null;
                return EntityType.get(soul.getNbt().getString("StoredEntity")).orElse(null);
            }
        }
        return null;
    }

    private void ApplyDamage(PlayerEntity attacker, LivingEntity victim, float damage, EntityType<?> entityType) {
        int firetime = 3;
        DamageSource damageSource = victim.getDamageSources().create(ModDamageTypes.SOULSPARKER_FIRE, attacker);

        if (entityType != null) {
            if (entityType.isIn(ModTags.Soulsparker.WITHER)) {
                victim.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 100, 0));
            } else if (entityType.isIn(ModTags.Soulsparker.UNDEAD)) {
                victim.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
            } else if (entityType.isIn(ModTags.Soulsparker.FIRE)) {
                firetime *= 3;
            } else if (entityType.isIn(ModTags.Soulsparker.SLOW)) {
                victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 100, 0));
            }
        }


        if (entityType != null && entityType.isIn(ModTags.Soulsparker.HEAL)) {
            victim.heal(damage);
            if (attacker.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, victim.getX(), victim.getY()+1, victim.getZ(), 10, 0.2, 0.5, 0.2, 0);
            }
        } else {
            victim.setOnFireFor(firetime);
            victim.damage(damageSource, damage);
        }
    }

    public void Flamethrow(World world, PlayerEntity user, float range, float spread, float damage, EntityType<?> entityType) {
        // do a raycast
        // Flamethrow must be cast server-side!
        ServerWorld serverWorld = (ServerWorld) world;
        Vec3d startPosition = user.getEyePos().add(0, -0.25f, 0);
        Vec3d rotationVector = user.getRotationVec(1.0f);
        Vec3d endPosition = startPosition.add(rotationVector.multiply(range));

        HitResult raycast = world.raycast(new RaycastContext(
                startPosition,
                endPosition,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                user
        ));

        Vec3d hitPosition = raycast.getPos();

        int steps = (int) (range * 10); // (range * x) where x is amount of particles per  block
        for (int i = 0; i < steps; i++) {
            double t = i / (double) steps;
            Vec3d pos = startPosition.lerp(hitPosition, t);
            double currentSpread = spread * (t * 5);
            // small random spread
            double velocityX = rotationVector.x * 0.1 + (world.random.nextDouble() - 0.5) * currentSpread;
            double velocityY = rotationVector.y * 0.1 + (world.random.nextDouble() - 0.5) * currentSpread;
            double velocityZ = rotationVector.z * 0.1 + (world.random.nextDouble() - 0.5) * currentSpread;

            serverWorld.spawnParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    pos.x, pos.y, pos.z,
                    1, velocityX, velocityY, velocityZ, 0.01
            );
        }

        // do damage
        double stepSize = 0.5; // check every 0.5 block along the ray
        for (double d = 0; d < range; d += stepSize) {
            Vec3d stepPos = startPosition.add(rotationVector.multiply(d));
            Box box = new Box(stepPos.subtract(0.5, 0.5, 0.5), stepPos.add(0.5, 0.5, 0.5));

            for (Entity e : world.getOtherEntities(user, box)) {
                if (e instanceof LivingEntity target) {
                    ApplyDamage(user, target, damage, entityType);
                }
            }
        }
    }

    private int GetOrMakeHeatNBT(ItemStack stack) {
        if (!stack.getOrCreateNbt().contains("Heat")) {
            assert stack.getNbt() != null;
            stack.getNbt().putInt("Heat", 0);
            return 0;
        }
        assert stack.getNbt() != null;
        return stack.getNbt().getInt("Heat");
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && !user.getItemCooldownManager().isCoolingDown(this)) {
            ServerWorld serverWorld = (ServerWorld) world;
            ItemStack stack = user.getStackInHand(hand);
            if (stack.getOrCreateNbt().contains("StoredItem")) {
                Flamethrow(world, user, 7.5f, 0.5f, 5, getStoredEntityType(stack));
                serverWorld.playSound(null, user.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS);

                // add to heat exhaustion
                assert stack.getNbt() != null;
                stack.getNbt().putInt("Heat", GetOrMakeHeatNBT(stack)+HEAT_INCREASE);

                if (GetOrMakeHeatNBT(stack) >= HEAT_LIMIT) {
                    stack.getNbt().putInt("Heat", HEAT_LIMIT);
                    user.getItemCooldownManager().set(this, HEAT_LIMIT);
                }

            } else {
                return TypedActionResult.success(stack);
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {

        assert stack.getNbt() != null;
        if (otherStack.isIn(ModTags.SOULSPARKER_INPUT)) {
            // get nbt data
            if (clickType.equals(ClickType.LEFT)) {
                if (!stack.getNbt().contains("StoredItem")) {
                    LOGGER.info(otherStack.toString());
                    placeItem(stack, otherStack);
                    otherStack.decrement(1);
                    return true;
                }
            }
        }

        if (otherStack.toString().equals("0 air")) {
            if (stack.getNbt().contains("StoredItem") && clickType.equals(ClickType.RIGHT)) {

                // set the item in the cursor to be the item in the satchel
                ItemStack item = ItemStack.fromNbt(stack.getOrCreateNbt().getCompound("StoredItem"));
                cursorStackReference.set(item);
                // remove the item from the satchel
                stack.getNbt().remove("StoredItem");
                return true;
            }
        }

        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        EntityType<?> entityType = getStoredEntityType(stack);
        if (entityType != null) {
            // add the tooltip saying the stored mob.
            tooltip.add(entityType.getName().copyContentOnly().formatted(Formatting.GRAY));

            // do checks for entities
            if (entityType.isIn(ModTags.Soulsparker.WITHER)) {
                tooltip.add(Text.translatable("tooltip.soulstice.soulsparker.wither").formatted(Formatting.DARK_PURPLE));
            } else if (entityType.isIn(ModTags.Soulsparker.FIRE)) {
                tooltip.add(Text.translatable("tooltip.soulstice.soulsparker.fire").formatted(Formatting.GOLD));
            } else if (entityType.isIn(ModTags.Soulsparker.UNDEAD)) {
                tooltip.add(Text.translatable("tooltip.soulstice.soulsparker.undead").formatted(Formatting.DARK_GREEN));
            } else if (entityType.isIn(ModTags.Soulsparker.HEAL)) {
                tooltip.add(Text.translatable("tooltip.soulstice.soulsparker.heal").formatted(Formatting.GREEN));
            } else if (entityType.isIn(ModTags.Soulsparker.SLOW)) {
                tooltip.add(Text.translatable("tooltip.soulstice.soulsparker.slow").formatted(Formatting.BLUE));
            }

        } else {
            tooltip.add(Text.translatable("tooltip.soulstice.soulsparker.no_soul").formatted(Formatting.GRAY));
        }


        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // get heat
        int heat = GetOrMakeHeatNBT(stack);
        if (heat > 0) {
            stack.getOrCreateNbt().putInt("Heat", heat - 1);
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}

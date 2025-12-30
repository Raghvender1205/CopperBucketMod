package org.rc.copperbucket.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.consume.UseAction;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class CopperWaterBucketItem extends BucketItem {

    private final Item emptyCopperBucket;

    public CopperWaterBucketItem(Fluid fluid, Settings settings, Item emptyCopperBucket) {
        super(fluid, settings);
        this.emptyCopperBucket = emptyCopperBucket;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        // Sneak = always drink (even if looking at a block)
        if (user.isSneaking()) {
            user.setCurrentHand(hand);
            return ActionResult.CONSUME;
        }

        // If aiming at a block, act like a normal water bucket (placing water)
        HitResult hit = raycast(world, user, RaycastContext.FluidHandling.NONE);
        if (hit.getType() == HitResult.Type.BLOCK) {
            return super.use(world, user, hand);
        }

        // Otherwise, drink
        user.setCurrentHand(hand);

        return ActionResult.CONSUME;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        // Called when drinking finishes
        if (!world.isClient()) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 60, 0)); // 3s
            world.playSound(
                    null,
                    user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENTITY_GENERIC_DRINK,
                    SoundCategory.PLAYERS,
                    1.0F, 1.0F
            ); // play sound on server side
            user.heal(4.0F); // 2 hearts
        }

        // for creative mode
        if (user instanceof PlayerEntity player && player.getAbilities().creativeMode) {
            return stack;
        }

        if (user instanceof PlayerEntity player) {
            return ItemUsage.exchangeStack(stack, player, new ItemStack(emptyCopperBucket));
        }

        // Non-player: just consume and return empty
        stack.decrement(1);

        return new ItemStack(emptyCopperBucket);
    }
}

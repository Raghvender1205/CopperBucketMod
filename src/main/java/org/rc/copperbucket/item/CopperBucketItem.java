package org.rc.copperbucket.item;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.rc.copperbucket.ModItems;

public class CopperBucketItem extends Item {

    public CopperBucketItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stackInHand = user.getStackInHand(hand);

        // Only pick up from SOURCE water blocks
        HitResult hit = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return ActionResult.PASS;
        }

        BlockHitResult blockHit = (BlockHitResult) hit;
        var pos = blockHit.getBlockPos();

        FluidState fluidState = world.getFluidState(pos);
        if (!fluidState.isIn(FluidTags.WATER) || !fluidState.isStill()) {
            return ActionResult.PASS;
        }

        // Simple pickup: remove the source block and swap the item
        if (!world.isClient()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            world.playSound(
                    null, pos,
                    SoundEvents.ITEM_BUCKET_FILL,
                    SoundCategory.PLAYERS,
                    1.0F, 1.0F
            );
        }

        ItemStack filled = new ItemStack(ModItems.COPPER_WATER_BUCKET);
        ItemStack newHandStack = ItemUsage.exchangeStack(stackInHand, user, filled);

        return ActionResult.SUCCESS.withNewHandStack(newHandStack);
    }
}

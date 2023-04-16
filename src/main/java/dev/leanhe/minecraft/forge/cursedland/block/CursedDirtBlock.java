
package dev.leanhe.minecraft.forge.cursedland.block;

import dev.leanhe.minecraft.forge.cursedland.tick.InfectNearbyTick;
import dev.leanhe.minecraft.forge.cursedland.types.BlockNearby;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import java.util.Random;
import java.util.List;
import java.util.Collections;

import dev.leanhe.minecraft.forge.cursedland.procedures.CursedDirtUpdateTickProcedure;
import org.jetbrains.annotations.NotNull;


public class CursedDirtBlock extends Block {

    public CursedDirtBlock() {
        super(BlockBehaviour.Properties.of(Material.DIRT).sound(SoundType.GRAVEL).strength(1f, 10f));
    }

    @Override
    public int getLightBlock(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        return 15;
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState state, LootContext.@NotNull Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void onPlace(@NotNull BlockState blockstate, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        if (world instanceof ServerLevel level) {
            InfectNearbyTick.checkAndAdd(level, pos);
        }
        world.scheduleTick(pos, this, 8);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        var neighborBlock = level.getBlockState(neighbor).getBlock();
        if (neighborBlock == Blocks.DIRT || neighborBlock == Blocks.GRASS_BLOCK) {
            InfectNearbyTick.push(BlockNearby.create(serverLevel, pos, neighbor));
        } else {
            InfectNearbyTick.remove(BlockNearby.create(serverLevel, pos, neighbor));
        }
    }


    @Override
    public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player entity, boolean willHarvest, FluidState fluid) {
        boolean retval = super.onDestroyedByPlayer(blockstate, world, pos, entity, willHarvest, fluid);
        //MinerMachineNeighbourBlockChangesProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
        InfectNearbyTick.remove(pos);
        return retval;
    }

    @Override
    public void wasExploded(@NotNull Level world, @NotNull BlockPos pos, @NotNull Explosion e) {
        super.wasExploded(world, pos, e);
        InfectNearbyTick.remove(pos);
        //MinerMachineNeighbourBlockChangesProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ());
    }


    @Override
    public void tick(@NotNull BlockState blockstate, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull Random random) {
        super.tick(blockstate, world, pos, random);
        CursedDirtUpdateTickProcedure.execute(world, pos, random);
        InfectNearbyTick.checkAndAdd(world, pos);
        world.scheduleTick(pos, this, 8);
    }
}

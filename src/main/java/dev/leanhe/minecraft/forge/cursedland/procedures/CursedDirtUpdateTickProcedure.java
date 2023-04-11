package dev.leanhe.minecraft.forge.cursedland.procedures;

import com.mojang.blaze3d.shaders.Effect;
import dev.leanhe.minecraft.forge.cursedland.CursedLandMod;
import dev.leanhe.minecraft.forge.cursedland.block.CursedDirtBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class RealSpawnTick {

    static void execute(ServerLevel level, BlockPos blockPos, Random random) {
        ArrayList<Entity> entities = new ArrayList<>(
                Arrays.asList(
                        new EnderMan(EntityType.ENDERMAN, level),
                        new Drowned(EntityType.DROWNED, level),
                        new Zombie(EntityType.ZOMBIE, level),
                        new Creeper(EntityType.CREEPER, level),
                        new ZombieVillager(EntityType.ZOMBIE_VILLAGER, level),
                        new Evoker(EntityType.EVOKER, level),
                        new Spider(EntityType.SPIDER, level),
                        new Skeleton(EntityType.SKELETON, level),
                        new Witch(EntityType.WITCH, level),
                        new WitherSkeleton(EntityType.WITHER_SKELETON, level)

                )
        );
        Entity entity = entities.get(random.nextInt(entities.size()));
        entity.moveTo(blockPos, random.nextFloat() * 360F, 0);
        Mob mob = (Mob) entity;
        mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 180, 3));
        mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 180, 3));
        mob.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 180, 3));
        mob.finalizeSpawn(level, level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
        level.addFreshEntity(entity);
    }

}

class ReplaceNearBlock {
    static void tick(ServerLevel world, BlockPos pos, Random random) {
        var state = world.getBlockState(pos);
        ArrayList<BlockPos> dirs = new ArrayList<>(Arrays.asList(pos.north(), pos.south(), pos.east(), pos.west()));

        for (var dir : dirs) {
            if (random.nextInt(10) >= 4) {
                continue;
            }
            var block = world.getBlockState(dir).getBlock();
            if (block.equals(Blocks.DIRT) || block.equals(Blocks.GRASS_BLOCK)) {
                world.setBlock(dir, state, 3);
            }

        }
    }
}


public class CursedDirtUpdateTickProcedure {
    public static void execute(LevelAccessor world, BlockPos pos, Random random) {
        if (!(world instanceof ServerLevel level)) {
            return;
        }

        if (random.nextInt(10) < 6) {
            return;
        }

        var block_above = pos.above();

        var light_level = world.getRawBrightness(block_above, 0);

        if (world.getBlockState(block_above).getBlock() != Blocks.AIR ||
                world.getBlockState(block_above.above()).getBlock() != Blocks.AIR) {
            return;
        }

        if (light_level == 0) {

            try {
                RealSpawnTick.execute(level, pos.above(), random);
                ReplaceNearBlock.tick(level, pos, random);

            } catch (Exception e) {
                CursedLandMod.LOGGER.fatal(e.toString());
            }
        }

    }
}

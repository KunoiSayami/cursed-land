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

    static void execute(LevelAccessor world, double x, double y, double z, Random random) {
        if (world instanceof ServerLevel level) {
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
            entity.moveTo(x, y, z, world.getRandom().nextFloat() * 360F, 0);
            Mob mob = (Mob) entity;
            mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 180, 3));
            mob.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 180, 3));
            mob.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 180, 3));
            mob.finalizeSpawn(level, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            world.addFreshEntity(entity);
        }

    }
}


public class CursedDirtUpdateTickProcedure {
    public static void execute(LevelAccessor world, BlockPos pos, double x, double y, double z, Random random) {
        var block_above = pos.above();

        var light_level = world.getRawBrightness(block_above, 0);


        CursedLandMod.LOGGER.debug("X => " + x + ", Y => " + y + ", Z => " + z + ", light => " + light_level);
        if (world.getBlockState(block_above).getBlock() != Blocks.AIR ||
                world.getBlockState(block_above.above()).getBlock() != Blocks.AIR) {
            return;
        }

        if (light_level == 0) {
            if (random.nextInt(10) < 6) {
                return;
            }
            try {
                RealSpawnTick.execute(world, x, y + 1, z, random);
            } catch (Exception e) {
                CursedLandMod.LOGGER.fatal(e.toString());
            }
        }

    }
}

package dev.leanhe.minecraft.forge.cursedland.procedures;

import com.mojang.brigadier.context.CommandContext;
import dev.leanhe.minecraft.forge.cursedland.CursedLandMod;
import dev.leanhe.minecraft.forge.cursedland.command.CursedDirtCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

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

    static boolean enabled = false;

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

class CanSeeSkyCheck {

    static boolean enabled = true;

    static boolean tick(ServerLevel world, BlockPos pos, Random random) {
        //CursedLandMod.LOGGER.info(world.canSeeSky(pos.above()) + " " + world.getDayTime() + " " + world.getGameTime() % 20000);
        if (world.canSeeSky(pos.above()) && random.nextInt(100) < 40 && world.getDayTime() < 12000) {

            LightningBolt entityToSpawn = EntityType.LIGHTNING_BOLT.create(world);
            if (entityToSpawn == null) {
                CursedLandMod.LOGGER.warn("Can't spawn lighting");
                return false;
            }
            entityToSpawn.moveTo(Vec3.atBottomCenterOf(pos));
            entityToSpawn.setVisualOnly(false);
            world.addFreshEntity(entityToSpawn);
            world.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
            return true;
        }
        return false;
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

        if (CanSeeSkyCheck.enabled && CanSeeSkyCheck.tick(level, pos, random)) {
            return;
        }

        var block_above = pos.above();

        var light_level = world.getRawBrightness(block_above, 0);

        if (world.getBlockState(block_above).getBlock() != Blocks.AIR ||
                world.getBlockState(block_above.above()).getBlock() != Blocks.AIR) {
            return;
        }

        if (light_level == 0) {

            RealSpawnTick.execute(level, pos.above(), random);
            if (ReplaceNearBlock.enabled) {
                ReplaceNearBlock.tick(level, pos, random);
            }
        }
    }

    public static int toggleSkyCheck(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        CanSeeSkyCheck.enabled = !CanSeeSkyCheck.enabled;
        CursedDirtCommand.sendMessageToPlayer(commandSourceStackCommandContext, "Set SkyCheck to " + CanSeeSkyCheck.enabled);
        return 0;
    }


    public static int toggleReplaceBlock(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        ReplaceNearBlock.enabled = !ReplaceNearBlock.enabled;
        CursedDirtCommand.sendMessageToPlayer(commandSourceStackCommandContext, "Set ReplaceNearbyBlock to" + ReplaceNearBlock.enabled);
        return 0;
    }

    public static int getCommandStatus(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        CursedDirtCommand.sendMessageToPlayer(commandSourceStackCommandContext, "CanSeeSkyCheck => " + CanSeeSkyCheck.enabled +
                ", ReplaceBlock => " + ReplaceNearBlock.enabled);
        return 0;
    }

}

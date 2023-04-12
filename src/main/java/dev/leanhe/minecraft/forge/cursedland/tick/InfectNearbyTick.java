package dev.leanhe.minecraft.forge.cursedland.tick;

import com.mojang.brigadier.context.CommandContext;
import dev.leanhe.minecraft.forge.cursedland.CursedLandMod;
import dev.leanhe.minecraft.forge.cursedland.command.CursedDirtCommand;
import dev.leanhe.minecraft.forge.cursedland.types.BlockNearby;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber
public class InfectNearbyTick {
    private static int server_tick = 0;
    private static final Set<BlockNearby> blockList = new LinkedHashSet<>();

    private static boolean enabled = false;

    public static void push(BlockNearby b) {
        synchronized (blockList) {
            blockList.add(b);
        }
    }

    public static void remove(BlockNearby b) {
        synchronized (blockList) {
            blockList.remove(b);
        }
    }

    public static void remove(BlockPos b) {
        ArrayList<BlockNearby> nearbies = new ArrayList<>();
        synchronized (blockList) {
            for (var blocknearby : blockList) {
                if (b.equals(blocknearby.getSelf())) {
                    nearbies.add(blocknearby);
                }
            }
            for (var nearby : nearbies) {
                blockList.remove(nearby);
            }
        }
    }

    public static int toggle(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        enabled = !enabled;
        CursedDirtCommand.sendMessageToPlayer(commandSourceStackCommandContext, "Set InfectNearbyTick to " + enabled);
        return 0;
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        //CursedLandMod.LOGGER.info("tick! size => " + blockList.size());
        if (!InfectNearbyTick.enabled || blockList.size() == 0) {
            return;
        }
        server_tick++;
        if (server_tick % 7 != 0) {
            return;
        }
        var random = new Random();
        ArrayList<BlockNearby> nearbies = new ArrayList<>();
        synchronized (blockList) {
            for (var blocknearby : InfectNearbyTick.blockList) {
                var world = blocknearby.getWorld();
                var nearbyCanSeeSky = world.canSeeSky(blocknearby.getNearby());
                if (nearbyCanSeeSky) {
                    continue;
                }
                if (random.nextInt(10) < 4) {
                    world.setBlock(blocknearby.getNearby(), world.getBlockState(blocknearby.getNearby()), 3);
                    nearbies.add(blocknearby);
                }
            }
            for (var nearby : nearbies) {
                blockList.remove(nearby);
            }
        }
    }

    public static void checkAndAdd(ServerLevel world, BlockPos blockPos) {
        for (var pos : Arrays.asList(blockPos.north(), blockPos.south(), blockPos.west(), blockPos.east())) {
            var block = world.getBlockState(pos).getBlock();
            if (block.equals(Blocks.GRASS_BLOCK) || block.equals(Blocks.DIRT)) {
                push(BlockNearby.create(world, blockPos, pos));
            }
        }
    }

}

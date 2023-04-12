package dev.leanhe.minecraft.forge.cursedland.types;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class BlockNearby {

    private final ServerLevel world;
    private final BlockPos self, nearby;

    BlockNearby(ServerLevel world, BlockPos self, BlockPos nearby) {
        this.world = world;
        this.self = self;
        this.nearby = nearby;
    }

    public BlockPos getSelf() {
        return self;
    }

    public BlockPos getNearby() {
        return nearby;
    }

    public ServerLevel getWorld() {
        return world;
    }

    public static BlockNearby create(ServerLevel world, BlockPos self, BlockPos nearby) {
        return new BlockNearby(world, self, nearby);
    }
}

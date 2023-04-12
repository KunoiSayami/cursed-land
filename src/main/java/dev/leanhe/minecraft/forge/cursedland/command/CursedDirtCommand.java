
package dev.leanhe.minecraft.forge.cursedland.command;

import com.mojang.brigadier.context.CommandContext;
import dev.leanhe.minecraft.forge.cursedland.procedures.CursedDirtUpdateTickProcedure;
import dev.leanhe.minecraft.forge.cursedland.tick.InfectNearbyTick;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class CursedDirtCommand {

    public static void sendMessageToPlayer(CommandContext<CommandSourceStack> commandSourceStackCommandContext, String s) {

        var source = commandSourceStackCommandContext.getSource();
        ///source.sendSuccess(new TextComponent("114514"), false);
        var entity = source.getEntity();
        if (entity == null) {
            entity = FakePlayerFactory.getMinecraft(source.getLevel());
        }
        if (entity instanceof Player player && !player.level.isClientSide())
            player.displayClientMessage(new TextComponent(s), false);
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("curseddirt")
                .then(Commands.literal("toggle")
                        .then(Commands.literal("sky").executes(CursedDirtUpdateTickProcedure::toggleSkyCheck))
                        .then(Commands.literal("nearby").executes(CursedDirtUpdateTickProcedure::toggleReplaceBlock))
                        .then(Commands.literal("nearby_ng").executes(InfectNearbyTick::toggle))
                        .executes(CursedDirtUpdateTickProcedure::getCommandStatus)
                ));

    }
}

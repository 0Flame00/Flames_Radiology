package net.flame.flamesradiology.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.flame.flamesradiology.common.init.ModEffects;
import net.flame.flamesradiology.radiation.ExposureManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class RadiationCommand {
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("radiation")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("clear")
                        .executes(context -> clearRadiation(context, context.getSource().getPlayerOrException()))
                        .then(Commands.argument("targets", EntityArgument.players())
                                .executes(context -> clearRadiation(context, EntityArgument.getPlayers(context, "targets")))))
                .then(Commands.literal("set")
                        .then(Commands.argument("exposure", DoubleArgumentType.doubleArg(0.0))
                                .executes(context -> setRadiation(context, context.getSource().getPlayerOrException(), DoubleArgumentType.getDouble(context, "exposure")))
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .executes(context -> setRadiation(context, EntityArgument.getPlayers(context, "targets"), DoubleArgumentType.getDouble(context, "exposure"))))))
                .then(Commands.literal("get")
                        .executes(context -> getRadiation(context, context.getSource().getPlayerOrException()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> getRadiation(context, EntityArgument.getPlayer(context, "target"))))));
    }
    
    private static int clearRadiation(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        return clearRadiation(context, java.util.List.of(player));
    }
    
    private static int clearRadiation(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        for (ServerPlayer player : players) {
            // Remove all radiation effects
            player.removeEffect(ModEffects.RADIATION_SICKNESS);
            player.removeEffect(ModEffects.RADIATION_POISONING);
            player.removeEffect(ModEffects.ACUTE_RADIATION_SYNDROME);
            
            // Clear radiation exposure
            ExposureManager.clearPlayerExposure(player);
        }
        
        if (players.size() == 1) {
            context.getSource().sendSuccess(() -> Component.literal("Cleared radiation for " + players.iterator().next().getName().getString()), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Cleared radiation for " + players.size() + " players"), true);
        }
        
        return players.size();
    }
    
    private static int setRadiation(CommandContext<CommandSourceStack> context, ServerPlayer player, double exposure) {
        return setRadiation(context, java.util.List.of(player), exposure);
    }
    
    private static int setRadiation(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, double exposure) {
        for (ServerPlayer player : players) {
            ExposureManager.setPlayerExposure(player, exposure);
        }
        
        if (players.size() == 1) {
            context.getSource().sendSuccess(() -> Component.literal("Set radiation exposure to " + exposure + " mSv for " + players.iterator().next().getName().getString()), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Set radiation exposure to " + exposure + " mSv for " + players.size() + " players"), true);
        }
        
        return players.size();
    }
    
    private static int getRadiation(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        double exposure = ExposureManager.getPlayerExposure(player);
        context.getSource().sendSuccess(() -> Component.literal(player.getName().getString() + " has " + String.format("%.2f", exposure) + " mSv radiation exposure"), false);
        return 1;
    }
}

package net.deadlydiamond98.archipelago.networking.s2c;

import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemsAccessor;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class MissingAPItemForCreateS2CPacket {
    public static final Identifier ID = APMod.id("missing_ap_item_for_create");

    /// Send the packet.
    /// @param players The players to send this packet to
    /// @param positions The block positions these missing items apply to.
    ///                  For example, the entire wall of mechanical crafters.
    /// @param missingItems The list of missing items.
    ///                     A single recipe might be missing multiple unlocks for all of its outputs.
    public static void send(List<ServerPlayerEntity> players, BlockPos[] positions, APMissingItem[] missingItems) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(positions.length);
        for (BlockPos pos : positions) {
            buf.writeBlockPos(pos);
        }

        buf.writeInt(missingItems.length);
        for (APMissingItem missingItem : missingItems) {
            missingItem.serialize(buf);
        }

        for (ServerPlayerEntity player : players) {
            ServerPlayNetworking.send(player, ID, buf);
        }
    }

    public static void checkAndChangeMultiple(ServerWorld world, BlockPos[] positions, APMissingItem[] missingItems) {
        for (BlockPos position : positions) {
            // If any are different, just commit and refresh all of them
            if (world.getBlockEntity(position) instanceof IMissingAPItemsAccessor accessor
                    && !Arrays.equals(missingItems, accessor.archipelago$getMissingAPItems())) {
                send(world.getPlayers(), positions, missingItems);
                return;
            }
        }
    }

    public static class Handler {
        public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int positionCount = buf.readInt();
            BlockPos[] positions = new BlockPos[positionCount];
            for (int i = 0; i < positionCount; i++) {
                positions[i] = buf.readBlockPos();
            }

            int missingItemCount = buf.readInt();
            APMissingItem[] missingItems = new APMissingItem[missingItemCount];
            for (int i = 0; i < missingItemCount; i++) {
                missingItems[i] = APMissingItem.deserialize(buf);
            }

            client.execute(() -> {
                if (client.world != null) {
                    for (BlockPos position : positions) {
                        if (client.world.getBlockEntity(position) instanceof IMissingAPItemsAccessor accessor) {
                            accessor.archipelago$setMissingAPItems(missingItems);
                        }
                    }
                }
            });
        }
    }
}

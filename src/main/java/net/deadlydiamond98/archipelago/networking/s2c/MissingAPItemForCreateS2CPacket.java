package net.deadlydiamond98.archipelago.networking.s2c;

import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemAccessor;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Optional;

public class MissingAPItemForCreateS2CPacket {
    public static final Identifier ID = APMod.id("missing_ap_item_for_create");

    public static void send(ServerPlayerEntity player, BlockPos pos, @Nullable APMissingItem missingItem) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        if (missingItem != null) {
            missingItem.serialize(buf);
        } else {
            APMissingItem.serializeNone(buf);
        }
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static class Handler {
        public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            BlockPos position = buf.readBlockPos();
            Optional<APMissingItem> missingItem = APMissingItem.deserialize(buf);
            client.execute(() -> {
                if (client.world != null && client.world.getBlockEntity(position) instanceof IMissingAPItemAccessor accessor) {
                    accessor.archipelago$setMissingAPItem(missingItem.orElse(null));
                }
            });
        }
    }
}

package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.archipelago.items.SavedArchipelagoItems;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemAccessor;
import net.deadlydiamond98.archipelago.networking.s2c.MissingAPItemForCreateS2CPacket;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin extends SmartBlockEntity implements IMissingAPItemAccessor {
    @Unique
    @Nullable
    private APMissingItem archipelago$missingAPItem;

    public KineticBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void archipelago$setMissingAPItem(@Nullable APMissingItem missingItem) {
        archipelago$missingAPItem = missingItem;
    }

    public Optional<APMissingItem> archipelago$getMissingAPItem() {
        return Optional.ofNullable(archipelago$missingAPItem);
    }

    public void archipelago$changeMissingAPItem(@Nullable APMissingItem item) {
        if (Objects.equals(item, archipelago$missingAPItem)) return;
        archipelago$missingAPItem = item;
        if (getWorld() instanceof ServerWorld serverWorld) {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                MissingAPItemForCreateS2CPacket.send(player, getPos(), item);
            }
        }
    }

    @Inject(method = "addToTooltip", at = @At("RETURN"), cancellable = true, remap = false)
    void addMissingAPItemTooltip(List<Text> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        Optional<APMissingItem> potentialMissingItem = archipelago$getMissingAPItem();
        if (potentialMissingItem.isPresent()) {
            APMissingItem missingItem = potentialMissingItem.get();
            String itemName = SavedArchipelagoItems.ID_TO_NAME_MAP.get(missingItem.name);
            new LangBuilder(APMod.MOD_ID)
                    .translate("check.requires_check", itemName)
                    .style(Formatting.GOLD)
                    .forGoggles(tooltip);
            String translationKey = missingItem.amount == 1
                            ? "archipelago.compat.create.crafting.requires_check"
                            : "archipelago.compat.create.crafting.requires_check.multiple";
            MutableText description = Text.translatable(translationKey, missingItem.attemptedCraft, itemName, missingItem.amount);
            List<Text> cutDescription = TooltipHelper.cutTextComponent(description, FontHelper.Palette.GRAY_AND_WHITE);
            for (Text text : cutDescription) {
                new LangBuilder(APMod.MOD_ID)
                        .add(text)
                        .forGoggles(tooltip);
            }
            cir.setReturnValue(true);
        }
    }
}

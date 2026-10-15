package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.catnip.lang.LangBuilder;
import net.deadlydiamond98.archipelago.APMod;
import net.deadlydiamond98.archipelago.archipelago.items.SavedArchipelagoItems;
import net.deadlydiamond98.archipelago.common.world.APPersistentState;
import net.deadlydiamond98.archipelago.networking.s2c.MissingAPItemForCreateS2CPacket;
import net.deadlydiamond98.archipelago.util.APMissingItem;
import net.deadlydiamond98.archipelago.util.mixinterfaces.IMissingAPItemsAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(KineticBlockEntity.class)
public abstract class KineticBlockEntityMixin extends SmartBlockEntity implements IMissingAPItemsAccessor {
    @Unique
    @NotNull
    private APMissingItem[] archipelago$missingAPItems = new APMissingItem[0];

    public KineticBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void archipelago$setMissingAPItems(@NotNull APMissingItem[] missingItems) {
        archipelago$missingAPItems = missingItems;
    }

    public APMissingItem[] archipelago$getMissingAPItems() {
        return archipelago$missingAPItems;
    }

    public void archipelago$changeMissingAPItem(@Nullable APMissingItem item) {
        if (item == null) {
            archipelago$changeMissingAPItems(new APMissingItem[0]);
        } else {
            archipelago$changeMissingAPItems(new APMissingItem[] { item });
        }
    }

    public void archipelago$changeMissingAPItems(@NotNull APMissingItem[] items) {
        if (Arrays.equals(items, archipelago$missingAPItems)) return;
        archipelago$setMissingAPItems(items);
        if (getWorld() instanceof ServerWorld serverWorld) {
            MissingAPItemForCreateS2CPacket.send(serverWorld.getPlayers(), new BlockPos[] {getPos()}, items);
        }
    }

    @Unique
    private static LangBuilder langBuilder() {
        return new LangBuilder(APMod.MOD_ID);
    }

    @Unique
    private void cutAndAddTextComponent(Text text, List<Text> tooltip) {
        List<Text> cutTexts = TooltipHelper.cutTextComponent(text, FontHelper.Palette.GRAY_AND_WHITE);
        for (Text cutText : cutTexts) {
            langBuilder()
                    .add(cutText)
                    .forGoggles(tooltip);
        }
    }

    @Inject(method = "addToTooltip", at = @At("RETURN"), cancellable = true, remap = false)
    void addMissingAPItemTooltip(List<Text> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
        APMissingItem[] missingItems = archipelago$getMissingAPItems();
        if (missingItems.length == 0) return;
        else if (missingItems.length == 1) {
            APMissingItem missingItem = missingItems[0];
            String itemName = SavedArchipelagoItems.ID_TO_NAME_MAP.get(missingItem.name);

            langBuilder()
                    .translate("check.requires_check", itemName)
                    .style(Formatting.GOLD)
                    .forGoggles(tooltip);

            String translationKey = String.format(
                    "archipelago.compat.create.%s.requires_check%s",
                    ((Object) this instanceof BlockBreakingKineticBlockEntity) ? "breaking" : "crafting",
                    missingItem.amount == 0 ? "" : ".progressive"
            );
            MutableText description = Text.translatable(translationKey, missingItem.attemptedCraft, itemName, missingItem.amount);
            cutAndAddTextComponent(description, tooltip);

            cir.setReturnValue(true);
        } else {
            long unlockedItems = Arrays.stream(missingItems).filter(i -> !i.isStillMissing()).count();
            if (unlockedItems == missingItems.length) return; // State has changed in the meantime

            langBuilder()
                    .translate("compat.create.crafting.some_missing.heading")
                    .style(Formatting.GOLD)
                    .forGoggles(tooltip);

            MutableText description = Text.translatable(
                    "archipelago.compat.create.crafting.some_missing.description",
                    unlockedItems,
                    missingItems.length
            );
            cutAndAddTextComponent(description, tooltip);

            for (APMissingItem missingItem : missingItems) {
                String translationKey = String.format(
                        "compat.create.crafting.some_missing.item.%s",
                        missingItem.isStillMissing()
                                ? "locked"
                                : "unlocked"
                );
                String reqTranslationKey = String.format(
                        "compat.create.crafting.some_missing.requirement%s",
                        missingItem.amount == 0 ? "" : ".progressive"
                );
                String itemName = SavedArchipelagoItems.ID_TO_NAME_MAP.get(missingItem.name);
                Formatting formatting = missingItem.isStillMissing() ? Formatting.RED : Formatting.GREEN;

                tooltip.add(Text.empty());
                langBuilder()
                        .translate(translationKey, missingItem.attemptedCraft)
                        .style(formatting)
                        .forGoggles(tooltip, 2);
                langBuilder()
                        .translate(reqTranslationKey, itemName, missingItem.amount)
                        .style(formatting)
                        .forGoggles(tooltip, 4);
            }
            cir.setReturnValue(true);
        }
    }
}

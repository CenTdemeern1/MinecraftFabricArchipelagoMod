package net.deadlydiamond98.archipelago.mixin.compat.create;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import net.deadlydiamond98.archipelago.compat.create.IMissingAPItemAccessor;
import net.deadlydiamond98.archipelago.util.APItemAccessUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(BasinOperatingBlockEntity.class)
public class BasinOperatingBlockEntityMixin extends KineticBlockEntity {
    public BasinOperatingBlockEntityMixin(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Inject(method = "getMatchingRecipes", at = @At("RETURN"), cancellable = true, remap = false)
    void filterMatchingRecipes(CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> recipes = cir.getReturnValue();
        World world = getWorld();
        if (world == null) {
            return;
        }
        DynamicRegistryManager manager = world.getRegistryManager();
        for (Recipe<?> recipe : recipes) {
            ItemStack itemStack = recipe.getOutput(manager);
            Optional<APItemAccessUtil.MissingItem> potentialMissingItem = APItemAccessUtil.getMissingItemFor(itemStack.getItem());
            if (potentialMissingItem.isPresent()) {
                APItemAccessUtil.MissingItem missingItem = potentialMissingItem.get();
                ((IMissingAPItemAccessor)this).archipelago$changeMissingAPItem(missingItem);
                cir.setReturnValue(new ArrayList<>());
                return;
            }
        }
        ((IMissingAPItemAccessor)this).archipelago$changeMissingAPItem(null);
    }
}

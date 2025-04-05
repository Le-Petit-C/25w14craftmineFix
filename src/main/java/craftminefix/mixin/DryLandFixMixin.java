package craftminefix.mixin;

import net.minecraft.aprilfools.UnlockCondition;
import net.minecraft.aprilfools.WorldEffect;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
/**
 * 查看: {@link net.minecraft.aprilfools.WorldEffects#DRY_LAND}
 * Mojang对于是否满足获得该物品的判断是
 	(serverWorld, serverPlayerEntity, itemStack) -> serverWorld.getBiome(serverPlayerEntity.getBlockPos()).isIn(BiomeTags.IS_BADLANDS)
 	&& itemStack.isOf(Items.LAVA_BUCKET)
 * 但是奇怪的是这个.isIn(BiomeTags.IS_BADLANDS)始终无法返回true
 * 所以我把它改成了.getIdAsString().contains("badlands")
 */

@Mixin(WorldEffect.Builder.class)
public abstract class DryLandFixMixin {
	@Shadow @Final @Mutable private String id;
	@Shadow @Final private List<UnlockCondition> unlockedBy;
	@Shadow protected abstract WorldEffect build();
	@Shadow public abstract WorldEffect.Builder unlockedByCondition(UnlockCondition... args);
	@Inject(method = "buildAndRegister", at = @At("HEAD"), cancellable = true)
    private void method_69963_injection(CallbackInfoReturnable<WorldEffect> cir){
		if(id.equals("dry_land")){
			unlockedBy.clear();
			unlockedByCondition(
					UnlockCondition.method_69662(
							(serverWorld, serverPlayerEntity, itemStack) -> serverWorld.getBiome(serverPlayerEntity.getBlockPos()).getIdAsString().contains("badlands")
                            && itemStack.isOf(Items.LAVA_BUCKET)
					)
			);
		}
		cir.setReturnValue(Registry.register(Registries.WORLD_EFFECT, Identifier.ofVanilla(this.id), build()));
		cir.cancel();
	}
}
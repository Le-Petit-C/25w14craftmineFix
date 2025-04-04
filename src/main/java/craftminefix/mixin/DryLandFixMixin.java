package craftminefix.mixin;

import net.minecraft.class_11057;
import net.minecraft.class_11109;
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
 * 查看: {@link net.minecraft.class_11113#field_59163}
 * Mojang对于是否满足获得该物品的判断是
 	(serverWorld, serverPlayerEntity, itemStack) -> serverWorld.getBiome(serverPlayerEntity.getBlockPos()).isIn(BiomeTags.IS_BADLANDS)
 	&& itemStack.isOf(Items.LAVA_BUCKET)
 * 但是奇怪的是这个.isIn(BiomeTags.IS_BADLANDS)始终无法返回true
 * 所以我把它改成了.getIdAsString().contains("badlands")
 */

@Mixin(class_11109.class_11110.class)
public abstract class DryLandFixMixin {
	@Shadow @Final @Mutable private String field_59139;
	@Shadow @Final private List<class_11057> field_59143;
	@Shadow protected abstract class_11109 method_69964();
	@Shadow public abstract class_11109.class_11110 method_69943(class_11057... args);
	@Inject(method = "method_69963", at = @At("HEAD"), cancellable = true)
    private void method_69963_injection(CallbackInfoReturnable<class_11109> cir){
		if(field_59139.equals("dry_land")){
			field_59143.clear();
			method_69943(
					class_11057.method_69662(
							(serverWorld, serverPlayerEntity, itemStack) -> serverWorld.getBiome(serverPlayerEntity.getBlockPos()).getIdAsString().contains("badlands")
                            && itemStack.isOf(Items.LAVA_BUCKET)
					)
			);
		}
		cir.setReturnValue(Registry.register(Registries.field_59575, Identifier.ofVanilla(this.field_59139), this.method_69964()));
		cir.cancel();
	}
}
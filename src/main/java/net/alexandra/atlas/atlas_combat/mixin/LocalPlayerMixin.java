package net.alexandra.atlas.atlas_combat.mixin;

import com.mojang.authlib.GameProfile;
import net.alexandra.atlas.atlas_combat.extensions.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.*;
import net.rizecookey.cookeymod.CookeyMod;
import net.rizecookey.cookeymod.config.category.MiscCategory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements PlayerExtensions, LivingEntityExtensions {
	public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
		super(clientLevel, gameProfile);
	}

	@Shadow
	public abstract void startUsingItem(InteractionHand interactionHand);

	@Shadow
	private boolean startedUsingItem;

	@Final
	public Minecraft minecraft = Minecraft.getInstance();
	LocalPlayer thisPlayer = (LocalPlayer)(Object)this;
	@Environment(EnvType.CLIENT)
	@Inject(method = "tick", at = @At("HEAD"))
	public void injectSneakShield(CallbackInfo ci) {
		if(thisPlayer.isOnGround()) {
			if(thisPlayer.isOnGround() && this.hasEnabledShieldOnCrouch()) {
				for (InteractionHand interactionHand : InteractionHand.values()) {
					if (thisPlayer.isCrouching() && !thisPlayer.isUsingItem()) {
						ItemStack itemStack = ((LivingEntityExtensions) this.thisPlayer).getBlockingItem();
						if (!itemStack.isEmpty() && itemStack.getItem() instanceof IShieldItem shieldItem && shieldItem.getBlockingType().canCrouchBlock() && thisPlayer.isCrouching() && thisPlayer.getItemInHand(interactionHand) == itemStack) {
							if (!thisPlayer.getCooldowns().isOnCooldown(itemStack.getItem())) {
								((IMinecraft) minecraft).startUseItem(interactionHand);
								minecraft.gameRenderer.itemInHandRenderer.itemUsed(interactionHand);
							}
						}
					} else if ((thisPlayer.isUsingItem() && minecraft.options.keyShift.consumeClick() && !minecraft.options.keyShift.isDown()) && !minecraft.options.keyUse.isDown()) {
						ItemStack itemStack = this.thisPlayer.getItemInHand(interactionHand);
						if (!itemStack.isEmpty() && (itemStack.getItem() instanceof IShieldItem shieldItem && shieldItem.getBlockingType().canCrouchBlock())) {
							minecraft.gameMode.releaseUsingItem(thisPlayer);
							startedUsingItem = false;
						}
					}
				}
			}
		}
	}
	@Override
	public boolean isAttackAvailable(float baseTime) {
		MiscCategory miscCategory = CookeyMod.getInstance().getConfig().getCategory(MiscCategory.class);
		if (!(getAttackStrengthScale(baseTime) < 1.0F)) {
			return true;
		} else {
			return this.getMissedAttackRecovery() && this.getAttackStrengthStartValue() - this.attackStrengthTicker - baseTime > 4.0F && !((IMiscCategory)miscCategory).getForce100PercentRecharge().get();
		}
	}
    @Redirect(method="hurtTo", at = @At(value = "FIELD", target = "Lnet/minecraft/client/player/LocalPlayer;invulnerableTime:I", opcode = Opcodes.PUTFIELD, ordinal=0))
    private void syncInvulnerability(LocalPlayer player, int x) {
        player.invulnerableTime = x / 2;
    }

	@Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(Z)V"))
	private void isShieldCrouching(Input instance, boolean b) {
		Item item = ((LivingEntityExtensions) thisPlayer).getBlockingItem().getItem();
		if(thisPlayer.getCooldowns().isOnCooldown(item)) {
			instance.tick(b);
		} else if(item instanceof IShieldItem shieldItem && shieldItem.getBlockingType().canCrouchBlock() && !thisPlayer.getCooldowns().isOnCooldown(item)) {
			instance.tick(false);
		} else {
			instance.tick(b);
		}
	}
	@Override
	public float getAttackAnim(float tickDelta) {
		if(((IOptions)Minecraft.getInstance().options).rhythmicAttacks()) {
			float var2 = this.attackAnim - this.oAttackAnim;
			if (var2 < 0.0F) {
				++var2;
			}

			float var3 = this.oAttackAnim + var2 * tickDelta;
			return var3 > 0.4F && this.getAttackStrengthScale(tickDelta) < 1.95F ? 0.4F + 0.6F * (float)Math.pow((var3 - 0.4F) / 0.6F, 4.0) : var3;
		}
		float f = this.attackAnim - this.oAttackAnim;
		if (f < 0.0F) {
			++f;
		}

		return this.oAttackAnim + f * tickDelta;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean hasEnabledShieldOnCrouch() {
		return ((IOptions)minecraft.options).shieldCrouch();
	}
}

package net.atlas.combatify.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.atlas.combatify.Combatify;
import net.atlas.combatify.config.ConfigurableItemData;
import net.atlas.combatify.config.ConfigurableWeaponData;
import net.atlas.combatify.extensions.DefaultedItemExtensions;
import net.atlas.combatify.extensions.ItemExtensions;
import net.atlas.combatify.extensions.PiercingItem;
import net.atlas.combatify.extensions.WeaponWithType;
import net.atlas.combatify.util.BlockingType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LongSwordItem extends TieredItem implements Vanishable, ItemExtensions, PiercingItem, WeaponWithType, DefaultedItemExtensions {
	private Multimap<Attribute, AttributeModifier> defaultModifiers;
	public LongSwordItem(Tier tier, Properties properties) {
		super(tier, properties);
		ImmutableMultimap.Builder<Attribute, AttributeModifier> var3 = ImmutableMultimap.builder();
		getWeaponType().addCombatAttributes(getTier(), var3);
		defaultModifiers = var3.build();
	}
	@Override
	public void modifyAttributeModifiers() {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> var3 = ImmutableMultimap.builder();
		getWeaponType().addCombatAttributes(getTier(), var3);
		ImmutableMultimap<Attribute, AttributeModifier> output = var3.build();
		this.setDefaultModifiers(output);
	}

	@Override
	public boolean canAttackBlock(BlockState state, Level world, BlockPos pos, Player miner) {
		return !miner.isCreative();
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (state.is(Blocks.COBWEB)) {
			return 15.0F;
		} else {
			return state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(1, attacker, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
		if (state.getDestroySpeed(world, pos) != 0.0F) {
			stack.hurtAndBreak(2, miner, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}

		return true;
	}

	@Override
	public boolean isCorrectToolForDrops(BlockState state) {
		return state.is(Blocks.COBWEB);
	}

	@Override
	public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
	}

	@Override
	public void setStackSize(int stackSize) {
		this.maxStackSize = stackSize;
	}

	@Override
	public double getPiercingLevel() {
		return getTier() == Tiers.NETHERITE || getTier().getLevel() >= 4 ? 0.2 : getTier() == Tiers.GOLD || getTier() == Tiers.WOOD || getTier() == Tiers.STONE || getTier().getAttackDamageBonus() <= 1 ? 0.0 : (0.1 * (getTier().getLevel() - 1));
	}

	@Override
	public WeaponType getWeaponType() {
		if(Combatify.ITEMS != null && Combatify.ITEMS.configuredItems.containsKey(this)) {
			WeaponType type = Combatify.ITEMS.configuredItems.get(this).type;
			if (type != null)
				return type;
		}
		return WeaponType.LONGSWORD;
	}
	@Override
	public double getChargedAttackBonus() {
		Item item = this;
		double chargedBonus = getWeaponType().getChargedReach();
		if(Combatify.ITEMS.configuredItems.containsKey(item)) {
			ConfigurableItemData configurableItemData = Combatify.ITEMS.configuredItems.get(item);
			if (configurableItemData.chargedReach != null)
				chargedBonus = configurableItemData.chargedReach;
		}
		return chargedBonus;
	}

	@Override
	public BlockingType getBlockingType() {
		if(Combatify.ITEMS != null && Combatify.ITEMS.configuredItems.containsKey(this)) {
			ConfigurableItemData configurableItemData = Combatify.ITEMS.configuredItems.get(this);
			if (configurableItemData.blockingType != null) {
				return configurableItemData.blockingType;
			}
		}
		if (Combatify.ITEMS != null && Combatify.ITEMS.configuredWeapons.containsKey(getWeaponType())) {
			ConfigurableWeaponData configurableWeaponData = Combatify.ITEMS.configuredWeapons.get(getWeaponType());
			if (configurableWeaponData.blockingType != null) {
				return configurableWeaponData.blockingType;
			}
		}
		return Combatify.EMPTY;
	}

	@Override
	public void setDefaultModifiers(ImmutableMultimap<Attribute, AttributeModifier> modifiers) {
		defaultModifiers = modifiers;
	}
}

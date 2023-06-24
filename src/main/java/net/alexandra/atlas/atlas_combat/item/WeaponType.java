package net.alexandra.atlas.atlas_combat.item;

import com.google.common.collect.ImmutableMultimap;
import net.alexandra.atlas.atlas_combat.config.AtlasConfig;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

import java.util.UUID;

public enum WeaponType {
    SWORD,
	LONGSWORD,
    AXE,
    PICKAXE,
    HOE,
    SHOVEL,
	KNIFE,
    TRIDENT;

    public static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    public static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    public static final UUID BASE_ATTACK_REACH_UUID = UUID.fromString("26cb07a3-209d-4110-8e10-1010243614c8");
	public static final UUID BASE_BLOCK_REACH_UUID = UUID.fromString("7f6fa63f-0fbd-4fa8-9acc-69c45c8f68ed");

    WeaponType() {
    }

    public void addCombatAttributes(Tier var1, ImmutableMultimap.Builder<Attribute, AttributeModifier> var2) {
        float var3 = this.getSpeed(var1);
        float var4 = this.getDamage(var1);
        float var5 = this.getReach();
		float var6 = this.getBlockReach();
        var2.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", var4, AttributeModifier.Operation.ADDITION));
		var2.put(NewAttributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", var3, AttributeModifier.Operation.ADDITION));
        if (var5 != 0.0F && AtlasConfig.attackReach) {
            var2.put(NewAttributes.ATTACK_REACH, new AttributeModifier(BASE_ATTACK_REACH_UUID, "Weapon modifier", var5, AttributeModifier.Operation.ADDITION));
        }
		if (var6 != 0.0F && AtlasConfig.blockReach) {
			var2.put(NewAttributes.BLOCK_REACH, new AttributeModifier(BASE_BLOCK_REACH_UUID, "Weapon modifier", var5, AttributeModifier.Operation.ADDITION));
		}


    }

	public float getDamage(Tier var1) {
		int modifier = AtlasConfig.fistDamage ? 1 : 0;
		float var2 = var1.getAttackDamageBonus() + modifier;
		boolean isNotTier1 = var1 != Tiers.WOOD && var1 != Tiers.GOLD && var2 != 0;
		boolean isCTSNotT1 = isNotTier1 && AtlasConfig.ctsAttackBalancing;
		switch (this) {
			case KNIFE -> {
				if (isCTSNotT1) {
					return var2 + min(AtlasConfig.knifeAttackDamage, 0);
				} else {
					return var2 + min(AtlasConfig.knifeAttackDamage, 0) + 1.0F;
				}
			}
			case PICKAXE -> {
				if (isCTSNotT1) {
					return var2;
				} else {
					return var2 + 1.0F;
				}
			}
			case SWORD -> {
				if (isCTSNotT1) {
					return var2 + min(AtlasConfig.swordAttackDamage, 0);
				} else {
					return var2 + min(AtlasConfig.swordAttackDamage, 0) + 1.0F;
				}
			}
			case AXE -> {
				if(!AtlasConfig.ctsAttackBalancing) {
					return !isNotTier1 ? var1 == Tiers.NETHERITE ? 10 : 9 : 7;
				} else if (isCTSNotT1) {
					return var2 + min(AtlasConfig.axeAttackDamage, 0);
				} else {
					return var2 + min(AtlasConfig.axeAttackDamage, 0) + 1.0F;
				}
			}
			case LONGSWORD, HOE -> {
				if (var1 != Tiers.IRON && var1 != Tiers.DIAMOND) {
					if (var1 == Tiers.NETHERITE || var1.getLevel() >= 4) {
						return var1 == Tiers.NETHERITE ? min(AtlasConfig.netheriteHoeAttackDamage, 0) + modifier : min(AtlasConfig.netheriteHoeAttackDamage, 0) + var2 - 4 + modifier;
					}

					return min(AtlasConfig.baseHoeAttackDamage, 0) + modifier;
				}
				return min(AtlasConfig.ironDiaHoeAttackDamage, 0) + modifier;
			}
			case SHOVEL -> {
				return var2;
			}
			case TRIDENT -> {
				return min(AtlasConfig.tridentAttackDamage, 0) + modifier + (AtlasConfig.ctsAttackBalancing ? 0 : 1);
			}
			default -> {
				return 0.0F + modifier;
			}
		}
    }

    public float getSpeed(Tier var1) {
		switch (this) {
			case KNIFE -> {
				return AtlasConfig.knifeAttackSpeed;
			}
			case LONGSWORD, SWORD -> {
				return AtlasConfig.swordAttackSpeed;
			}
			case AXE, SHOVEL -> {
				return AtlasConfig.axeAttackSpeed;
			}
			case TRIDENT -> {
				return AtlasConfig.tridentAttackSpeed;
			}
			case HOE -> {
				if (var1 == Tiers.WOOD) {
					return AtlasConfig.woodenHoeAttackSpeed;
				} else if (var1 == Tiers.IRON) {
					return AtlasConfig.ironHoeAttackSpeed;
				} else if (var1 == Tiers.DIAMOND) {
					return AtlasConfig.goldDiaNethHoeAttackSpeed;
				} else if (var1 == Tiers.GOLD) {
					return AtlasConfig.goldDiaNethHoeAttackSpeed;
				} else {
					if (var1 == Tiers.NETHERITE || var1.getLevel() >= 4) {
						return AtlasConfig.goldDiaNethHoeAttackSpeed;
					}

					return AtlasConfig.stoneHoeAttackSpeed;
				}
			}
			default -> {
				return AtlasConfig.defaultAttackSpeed;
			}
		}
    }

    public float getReach() {
		return switch (this) {
			case KNIFE -> -0.5F;
			case SWORD -> 0.5F;
			case LONGSWORD, HOE, TRIDENT -> 1.0F;
			case AXE -> !AtlasConfig.axeReachBuff ? 0.0F : 0.5F;
			default -> 0.0F;
		};
    }

	public float getBlockReach() {
		return switch (this) {
			case PICKAXE, SWORD, AXE -> 1.5F;
			case SHOVEL -> 1.0F;
			case LONGSWORD, HOE, TRIDENT -> 2.0F;
			default -> 0.0F;
		};
	}
	public static float min(float f, float j) {
		return Math.max(f, j);
	}
}

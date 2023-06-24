package net.alexandra.atlas.atlas_combat.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class AtlasConfig extends MidnightConfig {
	@Entry public static boolean toolsAreWeapons = false;
	@Entry public static boolean bedrockBlockReach = false;
	@Entry public static boolean refinedCoyoteTime = false;
	@Entry public static boolean midairKB = false;
	@Entry public static boolean fishingHookKB = false;
	@Entry public static boolean fistDamage = false;
	@Entry public static boolean swordBlocking = false;
	@Entry public static boolean sprintCritsEnabled = true;
	@Entry public static boolean saturationHealing = false;
	@Entry public static boolean configOnlyWeapons = false;
	@Entry public static boolean autoAttackAllowed = true;
	@Entry public static boolean axeReachBuff = false;
	@Entry public static boolean blockReach = true;
	@Entry public static boolean attackReach = true;
	@Entry public static boolean attackSpeed = true;
	@Entry public static boolean ctsAttackBalancing = true;
	@Entry public static boolean eatingInterruption = true;
	@Entry(min=-3, max =4) public static int swordProtectionEfficacy = 0;
	@Entry(min=1,max=1000) public static int potionUseDuration = 20;
	@Entry(min=1,max=1000) public static int honeyBottleUseDuration = 20;
	@Entry(min=1,max=1000) public static int milkBucketUseDuration = 20;
	@Entry(min=1,max=1000) public static int stewUseDuration = 20;
	@Entry(min=1,max=1000) public static int instantHealthBonus = 6;
	@Entry(min=1,max=1000) public static int eggItemCooldown = 4;
	@Entry(min=1,max=1000) public static int snowballItemCooldown = 4;
	@Entry(min=0,max=40) public static float snowballDamage = 0.0F;
	@Entry(min=0,max=40) public static float eggDamage = 0.0F;
	@Entry(min=0,max=4) public static float bowUncertainty = 0.25F;
	@Entry(min = 0, max = 1000) public static float swordAttackDamage = 1;
	@Entry(min = 0, max = 1000) public static float axeAttackDamage = 2;
	@Entry(min = 0, max = 1000) public static float knifeAttackDamage = 0;
	@Entry(min = 0, max = 1000) public static float baseHoeAttackDamage = 0;
	@Entry(min = 0, max = 1000) public static float ironDiaHoeAttackDamage = 1;
	@Entry(min = 0, max = 1000) public static float netheriteHoeAttackDamage = 2;
	@Entry(min = 0, max = 1000) public static float tridentAttackDamage = 5;

	@Entry(min = -1, max = 7.5)
	public static float swordAttackSpeed = 0.5F;

	@Entry(min = -1, max = 7.5)
	public static float axeAttackSpeed = -0.5F;

	@Entry(min = -1, max = 7.5)
	public static float woodenHoeAttackSpeed = -0.5F;

	@Entry(min = -1, max = 7.5)
	public static float stoneHoeAttackSpeed = 0;

	@Entry(min = -1, max = 7.5)
	public static float ironHoeAttackSpeed = 0.5F;

	@Entry(min = -1, max = 7.5)
	public static float goldDiaNethHoeAttackSpeed = 1;

	@Entry(min = -1, max = 7.5)
	public static float knifeAttackSpeed = 1;

	@Entry(min = -1, max = 7.5)
	public static float tridentAttackSpeed = -0.5F;

	@Entry(min = -1, max = 7.5)
	public static float defaultAttackSpeed = 0F;

	@Entry(min = -1, max = 7.5)
	public static float slowestToolAttackSpeed = -1F;

	@Entry(min = -1, max = 7.5)
	public static float slowToolAttackSpeed = -0.5F;

	@Entry(min = -1, max = 7.5)
	public static float fastToolAttackSpeed = 0.5F;

	@Entry(min = -1, max = 7.5)
	public static float fastestToolAttackSpeed = 1F;
}

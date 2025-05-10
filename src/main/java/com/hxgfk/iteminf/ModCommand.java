package com.hxgfk.iteminf;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.EnumArgument;

import java.util.Map;
import java.util.function.Function;

public class ModCommand {
    enum DataType {
        nbt, food, base, tool
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("iteminf").requires(CommandSourceStack::isPlayer).executes(ModCommand::run).then(Commands.argument("type", EnumArgument.enumArgument(DataType.class)).executes(ModCommand::runWithArg)));
    }

    private static int run(CommandContext<CommandSourceStack> css) {
        CommandSourceStack source = css.getSource();
        ServerPlayer executor = source.getPlayer();
        if (executor.getMainHandItem().isEmpty()) {
            source.sendFailure(Component.literal(I18n.get("msg.iteminf.noItem")).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            return 0;
        } else {
            ItemStack itemStack = executor.getMainHandItem();
            Item item = itemStack.getItem();
            String itemID = ForgeRegistries.ITEMS.getKey(item).toString();
            String itemName = item.getDescription().getString();
            String customName = itemStack.hasCustomHoverName() ? itemStack.getHoverName().getString() : null;
            CompoundTag nbt = itemStack.hasTag() ? itemStack.getTag() : null;
            String text = I18n.get("msg.iteminf.data", itemName, itemID, itemStack.getDamageValue());
            if (customName != null) {
                text += "\n" + ChatFormatting.BLUE + I18n.get("msg.iteminf.dataCustomName", customName) + ChatFormatting.RESET;
            }
            if (nbt != null) {
                text += "\nNBT: " + NbtUtils.toPrettyComponent(nbt).getString();
            }
            String foodData = getFoodData(item);
            if (foodData != null) text += "\n" + foodData;
            String toolData = getToolData(item);
            if (toolData != null) text += "\n" + toolData;
            String finalText = text;
            source.sendSuccess(() -> Component.literal(finalText), true);
        }
        return 0;
    }

    private static int runWithArg(CommandContext<CommandSourceStack> css) {
        CommandSourceStack source = css.getSource();
        ServerPlayer executor = source.getPlayer();
        if (executor.getMainHandItem().isEmpty()) {
            source.sendFailure(Component.literal(I18n.get("msg.iteminf.noItem")).withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
            return 0;
        } else {
            ItemStack itemStack = executor.getMainHandItem();
            Item item = itemStack.getItem();
            String itemID = ForgeRegistries.ITEMS.getKey(item).toString();
            String itemName = item.getDescription().getString();
            String customName = itemStack.hasCustomHoverName() ? itemStack.getHoverName().getString() : null;
            String text = "";
            switch (css.getArgument("type", DataType.class)) {
                case base:
                    text = I18n.get("msg.iteminf.data", itemName, itemID, itemStack.getDamageValue());
                    if (customName != null) {
                        text += ChatFormatting.BLUE + I18n.get("msg.iteminf.dataCustomName", customName) + ChatFormatting.RESET;
                    }
                    break;
                case nbt:
                    CompoundTag nbt = itemStack.hasTag() ? itemStack.getTag() : null;
                    if (nbt != null) {
                        text = NbtUtils.toPrettyComponent(nbt).getString();
                    } else {
                        source.sendFailure(Component.translatable("msg.iteminf.targetNoNbt").withStyle(ChatFormatting.RED));
                        return 0;
                    }
                    break;
                case food:
                    String foodData = getFoodData(item);
                    if (foodData != null) {
                        text = foodData;
                    } else {
                        source.sendFailure(Component.translatable("msg.iteminf.targetNoFood").withStyle(ChatFormatting.RED));
                        return 0;
                    }
                    break;
                case tool:
                    String toolData = getToolData(item);
                    if (toolData != null) {
                        text = toolData;
                    } else {
                        source.sendFailure(Component.translatable("msg.iteminf.targetNoTool").withStyle(ChatFormatting.RED));
                        return 0;
                    }
                    break;
            }
            String finalText = text;
            source.sendSuccess(() -> Component.literal(finalText), true);
        }
        return 0;
    }

    private static String getToolData(Item item) {
        String text = null;
        if (item instanceof TieredItem tieredItem) {
            text = "";
            String toolTypeTransKey = "Sword";
            if (item instanceof AxeItem) toolTypeTransKey = "Axe";
            if (item instanceof PickaxeItem) toolTypeTransKey = "Pickaxe";
            if (item instanceof ShovelItem) toolTypeTransKey = "Shovel";
            if (item instanceof HoeItem) toolTypeTransKey = "Hoe";
            text += I18n.get("msg.iteminf.dataToolType", I18n.get("msg.iteminf.dataToolType" + toolTypeTransKey));
            String toolTier = "Unknown";
            Tier tier = tieredItem.getTier();
            if (tier instanceof Tiers tiers) toolTier = tiers.name();
            text += "\n" + I18n.get("msg.iteminf.dataToolTier", toolTier);
            text += "\n" + ChatFormatting.RED + I18n.get("msg.iteminf.dataToolTierDamageBonus", tier.getAttackDamageBonus());
            text += "\n" + ChatFormatting.LIGHT_PURPLE + I18n.get("msg.iteminf.dataToolTierEnchantment", tier.getEnchantmentValue());
            text += "\n" + ChatFormatting.WHITE + I18n.get("msg.iteminf.dataToolTierUses", tier.getUses());
            text += "\n" + ChatFormatting.BLUE + I18n.get("msg.iteminf.dataToolTierSpeed", tier.getSpeed());
            text += "\n" + ChatFormatting.YELLOW + I18n.get("msg.iteminf.dataToolTierRepairs", customArrayToString(tier.getRepairIngredient().getItems(), (is) -> is.getDisplayName().getString()));
            if (item instanceof SwordItem sword)
                text += "\n" + ChatFormatting.GOLD + I18n.get("msg.iteminf.dataToolSwordAttackDamage", sword.getDamage());
            if (item instanceof DiggerItem digger) {
                text += "\n" + I18n.get("msg.iteminf.dataToolDiggerBaseAttackDamage", digger.getAttackDamage());
                try {
                    TagKey<Block> diggerBlocks = (TagKey<Block>) DiggerItem.class.getDeclaredField("blocks").get(digger);
                    text += "\n" + I18n.get("msg.iteminf.dataToolDiggerDestoryBlocks", diggerBlocks.toString());
                } catch (Exception ignored) {
                }
                text += "\n" + I18n.get("msg.iteminf.dataToolDiggerAttributes");
                try {
                    Map<Attribute, AttributeModifier> toolAttributeMap = (Map<Attribute, AttributeModifier>) DiggerItem.class.getDeclaredField("defaultModifiers").get(digger);
                    Attribute[] keys = (Attribute[]) toolAttributeMap.keySet().toArray();
                    for (Attribute attr : keys) {
                        AttributeModifier modifier = toolAttributeMap.get(attr);
                        double value = calculateAttributeValue(attr, modifier);
                        text += "\n  " + attr.getDescriptionId() + " = " + value;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return text;
    }

    private static String getFoodData(Item item) {
        String text = null;
        FoodProperties fp = item.getFoodProperties();
        if (fp != null) {
            text = "";
            text += I18n.get("msg.iteminf.dataFood");
            text += "\n   " + ChatFormatting.GOLD + I18n.get("msg.iteminf.dataFoodNutrition", fp.getNutrition());
            text += "\n   " + ChatFormatting.YELLOW + I18n.get("msg.iteminf.dataFoodSaturation", fp.getNutrition() * fp.getSaturationModifier() * 20);
            text += "\n   " + ChatFormatting.WHITE + I18n.get("msg.iteminf.dataFoodMeat", fp.isMeat());
            text += "\n   " + ChatFormatting.WHITE + I18n.get("msg.iteminf.dataFoodAlwaysEat", fp.canAlwaysEat());
            text += "\n   " + I18n.get("msg.iteminf.dataFoodFastEat", fp.isFastFood());
            if (!fp.getEffects().isEmpty()) {
                text += "\n   " + ChatFormatting.GREEN + I18n.get("msg.iteminf.dataFoodEffects");
                for (Pair<MobEffectInstance, Float> effect : fp.getEffects()) {
                    MobEffectInstance instance = effect.getFirst();
                    text += "\n      [" + instance.getEffect().getDisplayName().getString() + "]";
                    text += "\n         " + I18n.get("msg.iteminf.dataFoodEffectDuration", instance.getDuration());
                    text += "\n         " + I18n.get("msg.iteminf.dataFoodEffectAmplifier", instance.getAmplifier());
                    text += "\n         " + I18n.get("msg.iteminf.dataFoodEffectProbability", (effect.getSecond() * 100) + "%");
                }
            }
        }
        return text;
    }

    private static <T> String customArrayToString(T[] array, Function<T, String> func) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < array.length; i++) {
            T element = array[i];
            if (element == null) {
                sb.append("null");
            } else {
                sb.append(func.apply(element));
            }
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }

    private static double calculateAttributeValue(Attribute attribute, AttributeModifier modifier) {
        double baseValue = attribute.getDefaultValue();
        double addSum = 0.0;
        double multiplyBaseSum = 0.0;
        double multiplyTotalProduct = 1.0;
        switch (modifier.getOperation()) {
            case ADDITION:
                addSum += modifier.getAmount();
                break;
            case MULTIPLY_BASE:
                multiplyBaseSum += modifier.getAmount();
                break;
            case MULTIPLY_TOTAL:
                multiplyTotalProduct *= (1.0 + modifier.getAmount());
                break;
        }
        double result = baseValue + addSum;
        result *= (1.0 + multiplyBaseSum);
        result *= multiplyTotalProduct;

        return result;
    }
}

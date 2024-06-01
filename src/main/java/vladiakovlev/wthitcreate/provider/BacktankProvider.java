package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.component.BarComponent;
import mcp.mobius.waila.api.component.PairComponent;
import mcp.mobius.waila.api.component.WrappedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import vladiakovlev.wthitcreate.WTHITCreate;

public enum BacktankProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final ResourceLocation CONFIG_BACKTANK_LEVEL = new ResourceLocation(WTHITCreate.MOD_ID,
			"backtank-level");

	private static final ResourceLocation CAPACITY_ENCHANTMENT_ID = EnchantmentHelper
			.getEnchantmentId(AllEnchantments.CAPACITY.get());

	public void register(IRegistrar registrar) {
		registrar.addFeatureConfig(CONFIG_BACKTANK_LEVEL, true);
		registrar.addComponent(this, TooltipPosition.BODY, BacktankBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(CONFIG_BACKTANK_LEVEL)) {
			return;
		}

		var backtank = (BacktankBlockEntity) accessor.getBlockEntity();
		var stored = backtank.getAirLevel();
		var capacity = BacktankUtil.maxAir(getCapacityEnchantmentLevel(backtank));

		var ratio = (float) stored / (float) capacity;
		var text = StringUtil.formatTickDuration(stored * 20) + "/"
				+ StringUtil.formatTickDuration(capacity * 20);

		tooltip.addLine(new PairComponent(
				new WrappedComponent(Component.translatable("wthit-create.backtank.air")),
				new BarComponent(ratio, 0xFFFFFFFF, text)));
	}

	private static int getCapacityEnchantmentLevel(BacktankBlockEntity backtank) {
		for (var tag : backtank.getEnchantmentTag()) {
			var compoundTag = (CompoundTag) tag;
			ResourceLocation enchantmentId = EnchantmentHelper.getEnchantmentId(compoundTag);

			if (enchantmentId != null && enchantmentId.equals(CAPACITY_ENCHANTMENT_ID)) {
				return EnchantmentHelper.getEnchantmentLevel(compoundTag);
			}
		}

		return 0;
	}

}

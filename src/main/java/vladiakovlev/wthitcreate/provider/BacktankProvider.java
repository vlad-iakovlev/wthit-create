package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.component.BarComponent;
import mcp.mobius.waila.api.component.PairComponent;
import mcp.mobius.waila.api.component.WrappedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import vladiakovlev.wthitcreate.WTHITCreate;

public enum BacktankProvider implements IBlockComponentProvider, IDataProvider<BacktankBlockEntity> {
	INSTANCE;

	private static final String PROVIDER_ID = WTHITCreate.MOD_ID + ".backtank";

	private static final String STORED_KEY = PROVIDER_ID + ".stored";
	private static final String CAPACITY_KEY = PROVIDER_ID + ".capacity";

	private static final ResourceLocation CAPACITY_ENCHANTMENT_ID = EnchantmentHelper
			.getEnchantmentId(AllEnchantments.CAPACITY.get());

	private static String formatSeconds(int seconds) {
		return "%02d:%02d".formatted(seconds / 60, seconds % 60);
	}

	public void register(IRegistrar registrar) {
		registrar.addComponent(this, TooltipPosition.BODY, BacktankBlockEntity.class);
		registrar.addBlockData(this, BacktankBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		var data = accessor.getData();
		var stored = data.raw().getInt(STORED_KEY);
		var capacity = data.raw().getInt(CAPACITY_KEY);

		var ratio = 0f;
		var text = formatSeconds(stored);
		if (capacity > 0) {
			ratio = (float) (stored / capacity);
			text += "/" + formatSeconds(capacity);
		}

		tooltip.addLine(new PairComponent(
				new WrappedComponent(Component.translatable("wthit-create.backtank.air")),
				new BarComponent(ratio, 0xFFFFFFFF, text)));
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<BacktankBlockEntity> accessor, IPluginConfig config) {
		var backtank = accessor.getTarget();

		data.raw().putInt(STORED_KEY, backtank.getAirLevel());
		data.raw().putInt(CAPACITY_KEY, BacktankUtil.maxAir(getEnchantmentLevel(backtank)));
	}

	private int getEnchantmentLevel(BacktankBlockEntity backtank) {
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

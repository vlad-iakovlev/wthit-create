package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankUtil;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IData;
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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import vladiakovlev.wthitcreate.WTHITCreate;

public enum BacktankProvider implements IBlockComponentProvider, IDataProvider<BacktankBlockEntity> {
	INSTANCE;

	public static final ResourceLocation ID = new ResourceLocation(WTHITCreate.MOD_ID, "backtank");

	private static final ResourceLocation CAPACITY_ENCHANTMENT_ID = EnchantmentHelper
			.getEnchantmentId(AllEnchantments.CAPACITY.get());

	private static String formatSeconds(int seconds) {
		return "%02d:%02d".formatted(seconds / 60, seconds % 60);
	}

	public void register(IRegistrar registrar) {
		registrar.addDataType(ID, Data.class, Data::new);
		registrar.addComponent(this, TooltipPosition.BODY, BacktankBlockEntity.class);
		registrar.addBlockData(this, BacktankBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		var data = accessor.getData().get(Data.class);
		if (data == null) {
			return;
		}

		var ratio = (float) (data.stored / data.capacity);
		var text = formatSeconds(data.stored) + "/" + formatSeconds(data.capacity);

		tooltip.addLine(new PairComponent(
				new WrappedComponent(Component.translatable("wthit-create.backtank.air")),
				new BarComponent(ratio, 0xFFFFFFFF, text)));
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<BacktankBlockEntity> accessor, IPluginConfig config) {
		var backtank = accessor.getTarget();
		var stored = backtank.getAirLevel();
		var capacity = BacktankUtil.maxAir(getEnchantmentLevel(backtank));

		data.add(Data.class, res -> res.add(new Data(stored, capacity)));
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

	public record Data(int stored, int capacity) implements IData {

		public Data(FriendlyByteBuf buf) {
			this(buf.readInt(), buf.readInt());
		}

		@Override
		public void write(FriendlyByteBuf buf) {
			buf.writeInt(stored);
			buf.writeInt(capacity);
		}

	}

}

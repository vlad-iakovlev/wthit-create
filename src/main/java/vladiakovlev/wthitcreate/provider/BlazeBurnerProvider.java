package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;

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
import mcp.mobius.waila.api.component.PairComponent;
import mcp.mobius.waila.api.component.WrappedComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import vladiakovlev.wthitcreate.WTHITCreate;

public enum BlazeBurnerProvider implements IBlockComponentProvider, IDataProvider<BlazeBurnerBlockEntity> {
	INSTANCE;

	public static final ResourceLocation ID = new ResourceLocation(WTHITCreate.MOD_ID, "blaze-burner");

	public void register(IRegistrar registrar) {
		registrar.addDataType(ID, Data.class, Data::read);
		registrar.addComponent(this, TooltipPosition.BODY, BlazeBurnerBlockEntity.class);
		registrar.addBlockData(this, BlazeBurnerBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		var data = accessor.getData().get(Data.class);
		if (data == null) {
			return;
		}

		var state = switch (data.activeFuel) {
			case NONE -> Component.translatable("wthit-create.blaze-burner.state.smouldering");
			case NORMAL -> Component.translatable("wthit-create.blaze-burner.state.kindled").withStyle(ChatFormatting.GOLD);
			case SPECIAL ->
				Component.translatable("wthit-create.blaze-burner.state.seething").withStyle(ChatFormatting.BLUE);
		};

		tooltip.addLine(new PairComponent(
				new WrappedComponent(Component.translatable("wthit-create.blaze-burner.state")),
				new WrappedComponent(state)));

		if (data.isCreative) {
			tooltip.addLine(new PairComponent(
					new WrappedComponent(Component.translatable("wthit-create.blaze-burner.remaining-time")),
					new WrappedComponent("∞")));
		}

		if (data.remainingBurnTime > 0) {
			tooltip.addLine(new PairComponent(
					new WrappedComponent(Component.translatable("wthit-create.blaze-burner.remaining-time")),
					new WrappedComponent(StringUtil.formatTickDuration(data.remainingBurnTime))));
		}
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<BlazeBurnerBlockEntity> accessor, IPluginConfig config) {
		var burner = accessor.getTarget();
		var isCreative = burner.isCreative();
		var activeFuel = isCreative ? getCreativeFuel(burner) : burner.getActiveFuel();
		var remainingBurnTime = burner.getRemainingBurnTime();

		data.add(Data.class, res -> res.add(new Data(isCreative, activeFuel, remainingBurnTime)));

	}

	private static FuelType getCreativeFuel(BlazeBurnerBlockEntity burner) {
		return switch (BasinBlockEntity.getHeatLevelOf(burner.getBlockState())) {
			case SEETHING -> FuelType.SPECIAL;
			case NONE -> FuelType.NONE;
			default -> FuelType.NORMAL;
		};
	}

	public record Data(boolean isCreative, FuelType activeFuel, int remainingBurnTime) implements IData {

		private static final int VERSION = 1;

		public static Data read(FriendlyByteBuf buf) {
			if (buf.readInt() != VERSION) {
				return null;
			}

			return new Data(buf.readBoolean(), FuelType.values()[buf.readInt()], buf.readInt());
		}

		@Override
		public void write(FriendlyByteBuf buf) {
			buf.writeInt(VERSION);
			buf.writeBoolean(isCreative);
			buf.writeInt(activeFuel.ordinal());
			buf.writeInt(remainingBurnTime);
		}

	}

}

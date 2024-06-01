package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.component.PairComponent;
import mcp.mobius.waila.api.component.WrappedComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringUtil;
import vladiakovlev.wthitcreate.WTHITCreate;

public enum BlazeBurnerProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final ResourceLocation CONFIG_BLAZE_BURNER_STATE = new ResourceLocation(WTHITCreate.MOD_ID,
			"blaze-burner-state");

	public void register(IRegistrar registrar) {
		registrar.addFeatureConfig(CONFIG_BLAZE_BURNER_STATE, true);
		registrar.addComponent(this, TooltipPosition.BODY, BlazeBurnerBlockEntity.class);
	}

	@Override
	public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
		if (!config.getBoolean(CONFIG_BLAZE_BURNER_STATE)) {
			return;
		}

		var blazeBurner = (BlazeBurnerBlockEntity) accessor.getBlockEntity();
		var isCreative = blazeBurner.isCreative();
		var activeFuel = isCreative ? getCreativeFuel(blazeBurner) : blazeBurner.getActiveFuel();
		var remainingBurnTime = blazeBurner.getRemainingBurnTime();

		var state = switch (activeFuel) {
			case NONE -> Component.translatable("wthit-create.blaze-burner.state.smouldering");
			case NORMAL -> Component.translatable("wthit-create.blaze-burner.state.kindled").withStyle(ChatFormatting.GOLD);
			case SPECIAL ->
				Component.translatable("wthit-create.blaze-burner.state.seething").withStyle(ChatFormatting.BLUE);
		};

		tooltip.addLine(new PairComponent(
				new WrappedComponent(Component.translatable("wthit-create.blaze-burner.state")),
				new WrappedComponent(state)));

		if (isCreative) {
			tooltip.addLine(new PairComponent(
					new WrappedComponent(Component.translatable("wthit-create.blaze-burner.remaining-time")),
					new WrappedComponent("∞")));
		}

		if (remainingBurnTime > 0) {
			tooltip.addLine(new PairComponent(
					new WrappedComponent(Component.translatable("wthit-create.blaze-burner.remaining-time")),
					new WrappedComponent(StringUtil.formatTickDuration(remainingBurnTime))));
		}
	}

	private static FuelType getCreativeFuel(BlazeBurnerBlockEntity burner) {
		return switch (BasinBlockEntity.getHeatLevelOf(burner.getBlockState())) {
			case SEETHING -> FuelType.SPECIAL;
			case NONE -> FuelType.NONE;
			default -> FuelType.NORMAL;
		};
	}

}

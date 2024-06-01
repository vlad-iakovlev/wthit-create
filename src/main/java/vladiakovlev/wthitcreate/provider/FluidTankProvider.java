package vladiakovlev.wthitcreate.provider;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.data.FluidData;

public enum FluidTankProvider implements IDataProvider<FluidTankBlockEntity> {
	INSTANCE;

	public void register(IRegistrar registrar) {
		registrar.addBlockData(this, FluidTankBlockEntity.class, 500);
	}

	@Override
	public void appendData(IDataWriter data, IServerAccessor<FluidTankBlockEntity> accessor, IPluginConfig config) {
		data.add(FluidData.class, res -> {
			var target = accessor.getTarget();
			var controller = target.getControllerBE();

			if (controller != null && controller.boiler.isActive()) {
				res.block();
			}
		});
	}

}

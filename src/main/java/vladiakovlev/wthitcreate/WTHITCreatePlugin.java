package vladiakovlev.wthitcreate;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import vladiakovlev.wthitcreate.provider.BacktankProvider;
import vladiakovlev.wthitcreate.provider.BlazeBurnerProvider;
import vladiakovlev.wthitcreate.provider.CopycatProvider;
import vladiakovlev.wthitcreate.provider.PlacardProvider;

public class WTHITCreatePlugin implements IWailaPlugin {

	@Override
	public void register(IRegistrar registrar) {
		BacktankProvider.INSTANCE.register(registrar);
		BlazeBurnerProvider.INSTANCE.register(registrar);
		CopycatProvider.INSTANCE.register(registrar);
		PlacardProvider.INSTANCE.register(registrar);
	}

}

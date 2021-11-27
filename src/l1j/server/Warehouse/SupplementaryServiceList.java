package l1j.server.Warehouse;

public class SupplementaryServiceList extends WarehouseList {
	@Override
	protected SupplementaryService createWarehouse(String name) {
		return new SupplementaryService(name);
	}
}

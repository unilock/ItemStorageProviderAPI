package cc.unilock.item_storage_provider.mixin;

import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleItemAccessor {
	@Invoker
	int callGetBundleOccupancy(ItemStack stack);

	@Invoker
	int callGetItemOccupancy(ItemStack stack);
}

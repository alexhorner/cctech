package github.shrekshellraiser.cctech.common.peripheral.tape.reel;

import github.shrekshellraiser.cctech.CCTech;
import github.shrekshellraiser.cctech.common.ModProperties;
import github.shrekshellraiser.cctech.common.ModBlockEntities;
import github.shrekshellraiser.cctech.common.peripheral.tape.TapeBlockEntity;
import github.shrekshellraiser.cctech.common.item.tape.ReelItem;
import github.shrekshellraiser.cctech.client.screen.tape.ReelToReelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dan200.computercraft.shared.Capabilities.CAPABILITY_PERIPHERAL;

public class ReelToReelBlockEntity extends TapeBlockEntity {


    @Override
    protected void itemRemoved(ItemStack item) {
        pointer = 0;
        super.itemRemoved(item);
    }

    @Override
    protected void itemInserted(ItemStack item) {
        super.itemInserted(item);
        pointer = 0;
    }

    public ReelToReelBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.REEL_TO_REEL.get(), pWorldPosition, pBlockState);
        deviceDir = ReelItem.getDeviceDir();
        peripheral = new ReelToReelPeripheral(this);
    }


    @Override
    public @NotNull Component getDisplayName() {
        return new TranslatableComponent("block.cctech.reel_to_reel");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new ReelToReelMenu(pContainerId, pPlayerInventory, this);
    }



    @Override
    public void drops() {
        pointer = 0;
        super.drops();
    }

    public int getPointer() {
        return pointer;
    }
}

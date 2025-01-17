package github.shrekshellraiser.cctech.common.peripheral.tape.cassette;

import github.shrekshellraiser.cctech.common.ModProperties;
import github.shrekshellraiser.cctech.common.peripheral.StorageBlock;
import github.shrekshellraiser.cctech.common.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CassetteDeckBlock extends StorageBlock {

    public CassetteDeckBlock() {
        super(Properties.of(Material.METAL).strength(2f));
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return ModBlockEntities.CASSETTE_DECK.get().create(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof CassetteDeckBlockEntity) {
                ((CassetteDeckBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if (entity instanceof CassetteDeckBlockEntity cassetteDeckBlockEntity) {
//                NetworkHooks.openGui(((ServerPlayer) pPlayer), (CassetteDeckBlockEntity) entity, pPos);
                // TODO add functionality
                if (pPlayer.isShiftKeyDown()) {
                    boolean isOpen = pState.getValue(ModProperties.OPEN);
                    pState = pState.setValue(ModProperties.OPEN, !isOpen);
                    pLevel.setBlock(pPos, pState, 3);
                    pLevel.blockEntityChanged(pPos);
                    pLevel.updateNeighbourForOutputSignal(pPos, pState.getBlock());
                } else if (pState.getValue(ModProperties.OPEN)) {
                    cassetteDeckBlockEntity.onRightClick(pLevel, pPlayer, pHand);
                }
            } else {
                throw new IllegalStateException("Clicked on a block that isn't the cassette deck but also is???");
            }
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.CASSETTE_DECK.get(), CassetteDeckBlockEntity::tick);
    }
}

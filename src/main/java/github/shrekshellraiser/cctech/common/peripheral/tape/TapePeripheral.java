package github.shrekshellraiser.cctech.common.peripheral.tape;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import github.shrekshellraiser.cctech.common.item.tape.CassetteItem;
import github.shrekshellraiser.cctech.common.item.tape.TapeItem;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class TapePeripheral implements IPeripheral {
    private final List<IComputerAccess> connectedComputers = new ArrayList<>();

    protected final TapeBlockEntity tileEntity;

    public TapePeripheral(TapeBlockEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    private Timer eventTimer;

    protected void startEventTimer(Object val, long delay) {
        eventTimer = new Timer();
        eventTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (IComputerAccess computer : connectedComputers) {
                    computer.queueEvent("cassette_finished", val);
                }
            }
        }, 0L);
    }

    @Override
    public boolean equals(IPeripheral other) {
        return this == other;
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        connectedComputers.remove(computer);
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        connectedComputers.add(computer);
    }

    @LuaFunction
    public boolean isReady() {
        return tileEntity.getItem() instanceof TapeItem;
    }

    @LuaFunction
    public final MethodResult read(IComputerAccess computerAccess, Optional<Integer> targetChars, Optional<Boolean> async) throws LuaException {
        int characters = targetChars.orElse(1);
        if (characters < 1) {
            throw new LuaException("Cannot read <1 bytes.");
        }
        return tileEntity.readData(computerAccess, characters, async.orElse(false));
    }

    @LuaFunction
    public final MethodResult write(IComputerAccess computerAccess, String ch, Optional<Boolean> async) throws LuaException {
        return tileEntity.write(computerAccess, ch, async.orElse(false));
    }

    @LuaFunction
    public final MethodResult seek(IComputerAccess computerAccess, int offset, Optional<Boolean> async) throws LuaException {
        return tileEntity.seekRel(computerAccess, offset, async.orElse(false));
    }

    @LuaFunction
    public final void setLabel(String label) throws LuaException {
        tileEntity.setLabel(label);
    }

    @LuaFunction
    public final void clearLabel() throws LuaException {
        tileEntity.clearLabel();
    }

    @LuaFunction
    public final int getSize() throws LuaException {
        return tileEntity.getSize();
    }
//
//    private final class TapeFinished implements ILuaCallback {
//        MethodResult methodResult = MethodResult.pullEvent(null, this);
//        /**
//         * Resume this coroutine.
//         *
//         * @param args The result of resuming this coroutine. These will have the same form as described in
//         *             {@link LuaFunction}.
//         * @return The result of this continuation. Either the result to return to the callee, or another yield.
//         * @throws LuaException On an error.
//         */
//        @NotNull
//        @Override
//        public MethodResult resume(Object[] args) throws LuaException {
//            boolean cassetteFinished = "cassette_finished".equals(args[0]);
//            if (cassetteFinished) {
//                return MethodResult.of(args[1]);
//            }
//            tileEntity.assertReady();
//            return MethodResult.pullEvent(null, this);
//        }
//    }
}

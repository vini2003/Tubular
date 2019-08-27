package tubular.fluid;

import net.minecraft.fluid.BaseFluid;

public class FluidHolder {
    BaseFluid fluid;
    Integer amount;

    public void setFluid(BaseFluid temporaryFluid) {
        this.fluid = temporaryFluid;
    }

    public BaseFluid getFluid() {
        return this.fluid;
    }

    public void setAmount(int temporaryAmount) {
        this.amount = temporaryAmount;
    }

    public int getAmount() {
        return this.amount;
    }
}
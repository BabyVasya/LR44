package org.example.Consumer;

import lombok.Getter;
import lombok.Setter;

public class SendTaskDto {
    @Getter
    @Setter
    private double requiredPowerPerHour;
    @Getter
    @Setter
    private double myMaxPrice;

    public SendTaskDto(double requiredPowerPerHour, double myMaxPrice) {
        this.requiredPowerPerHour = requiredPowerPerHour;
        this.myMaxPrice = myMaxPrice;
    }
}

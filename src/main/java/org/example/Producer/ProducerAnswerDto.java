package org.example.Producer;

import lombok.Getter;

public class ProducerAnswerDto {
    @Getter
    private double myPrice;
    @Getter
    private double taskPrice;

    public ProducerAnswerDto(double myPrice, double taskPrice) {
        this.myPrice = myPrice;
        this.taskPrice = taskPrice;
    }
}

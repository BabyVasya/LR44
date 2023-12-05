package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class VirtualTime {
    public static double currentHour = 1;
//    @SneakyThrows
//    public static void startModeling() {
//        new Thread(()-> {
//            currentHour++;
//            log.info(""+currentHour);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//    }
    public double currentModelHour() {
        return currentHour;
    }

    //TODO: метод, который возвращает время в миллисекундах до начала следующего часа
}

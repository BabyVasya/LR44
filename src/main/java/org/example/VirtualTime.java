package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class VirtualTime {
    public static int currentHour = 1;
}

    //TODO: метод, который возвращает время в миллисекундах до начала следующего часа


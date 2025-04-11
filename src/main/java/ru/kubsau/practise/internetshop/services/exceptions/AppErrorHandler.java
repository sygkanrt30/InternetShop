package ru.kubsau.practise.internetshop.services.exceptions;

import java.time.LocalTime;

public record AppErrorHandler(int statusCode, String message, LocalTime time) {}

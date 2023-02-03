package com.example.fooddeliverysystem.exceptions.purchase;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.*;
public class IncorrectDateFormatException extends DateTimeParseException {

    public IncorrectDateFormatException(String msg, String parsedObj, Integer errorInd) {
        super(msg, parsedObj, errorInd);
    }
}

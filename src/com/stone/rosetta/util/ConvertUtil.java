package com.stone.rosetta.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ConvertUtil {

    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
    
    public static LocalDateTime toLocalDateTime(Timestamp timestamp){
        return timestamp.toLocalDateTime();
    }
}

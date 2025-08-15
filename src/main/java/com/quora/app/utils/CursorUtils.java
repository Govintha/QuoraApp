package com.quora.app.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class CursorUtils {

    public static boolean isValidCursor(String cursor){

         if(cursor==null||cursor.isEmpty()) return false;

         try{
             Instant localDateTime= OffsetDateTime.parse(cursor).toInstant();
             return true;
         }catch (Exception e){
              return false;
         }
    }

    public static Instant parseCursor(String cursor){

         if(!isValidCursor(cursor)) throw new IllegalArgumentException("Cursor not valid");

         try{
             return OffsetDateTime.parse(cursor).toInstant();
         }catch (Exception e){
             throw new IllegalArgumentException(e.getMessage());
         }
    }


}

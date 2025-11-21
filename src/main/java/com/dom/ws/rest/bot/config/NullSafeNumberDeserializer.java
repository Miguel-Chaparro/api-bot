package com.dom.ws.rest.bot.config;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Deserializador personalizado para manejar números vacíos o inválidos en JSON.
 * Convierte strings vacíos a null en lugar de lanzar excepción.
 */
public class NullSafeNumberDeserializer implements JsonDeserializer<Number> {
    
    @Override
    public Number deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonSyntaxException {
        
        if (json.isJsonNull()) {
            return null;
        }
        
        if (json.isJsonPrimitive()) {
            JsonPrimitive primitive = json.getAsJsonPrimitive();
            
            // Si es un string vacío, retornar null
            if (primitive.isString()) {
                String str = primitive.getAsString();
                if (str == null || str.trim().isEmpty()) {
                    return null;
                }
                
                // Intentar parsear el string
                try {
                    if (typeOfT == Integer.class) {
                        return Integer.parseInt(str);
                    } else if (typeOfT == Long.class) {
                        return Long.parseLong(str);
                    } else if (typeOfT == Double.class) {
                        return Double.parseDouble(str);
                    } else if (typeOfT == Float.class) {
                        return Float.parseFloat(str);
                    } else if (typeOfT == BigDecimal.class) {
                        return new BigDecimal(str);
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            
            // Si es un número directo, deserializar normalmente
            if (primitive.isNumber()) {
                try {
                    if (typeOfT == Integer.class) {
                        return primitive.getAsInt();
                    } else if (typeOfT == Long.class) {
                        return primitive.getAsLong();
                    } else if (typeOfT == Double.class) {
                        return primitive.getAsDouble();
                    } else if (typeOfT == Float.class) {
                        return primitive.getAsFloat();
                    } else if (typeOfT == BigDecimal.class) {
                        return primitive.getAsBigDecimal();
                    }
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        
        return null;
    }
}

package com.github.natezhengbne.toolbox.kafka.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.natezhengbne.toolbox.exceptions.KafkaDeserializationException;
import com.github.natezhengbne.toolbox.exceptions.KafkaSerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class ObjectEncoder {

    @Autowired
    private ObjectMapper objectMapper;

    public Object decode(byte[] encodedObject, Type type) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            byte[] b64object = Base64.getDecoder().decode(encodedObject);
            return objectMapper.readValue(b64object, javaType);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            throw new KafkaDeserializationException("Error decoding object parameter/response : " + encodedObject);
        }
    }


    public byte[] encode(Object o) {
        try {
            return Base64.getEncoder().encode(objectMapper.writeValueAsBytes(o));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new KafkaSerializationException("Error encoding object parameter/response : " + o.toString());
        }
    }
    
    
    public Map<String, byte[]> encodeMap(Map<String,Object> params) {
        if (params == null) return null;
        Map<String, byte[]> encodedParams = new HashMap<>();
        params.forEach((key ,value) -> encodedParams.put(key, encode(value)));
        return encodedParams;
    }



}

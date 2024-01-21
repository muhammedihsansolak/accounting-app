package com.cydeo.converter;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@ConfigurationPropertiesBinding
public class BigIntegerConverter implements Converter<String, BigInteger> {

    @Override
    public BigInteger convert(String source) {
        return BigInteger.valueOf( Integer.parseInt(source) );
    }
}

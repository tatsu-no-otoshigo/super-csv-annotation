/*
 * Min.java
 * created in 2012/09/22
 *
 * (C) Copyright 2003-2012 GreenDay Project. All rights reserved.
 */
package org.supercsv.ext.cellprocessor.constraint;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DoubleCellProcessor;
import org.supercsv.cellprocessor.ift.LongCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.exception.SuperCsvConstraintViolationException;
import org.supercsv.ext.cellprocessor.NumberFormatWrapper;
import org.supercsv.ext.cellprocessor.ift.ValidationCellProcessor;
import org.supercsv.util.CsvContext;


/**
 * @author T.TSUCHIE
 *
 */
public class Min<T extends Number & Comparable<T>> extends CellProcessorAdaptor
        implements LongCellProcessor, DoubleCellProcessor, ValidationCellProcessor {
    
    protected final T min;
    
    protected NumberFormat formatter;
    
    public Min(final T min) {
        super();
        checkPreconditions(min);
        this.min = min;
    }
    
    public Min(final T min, final CellProcessor next) {
        super(next);
        checkPreconditions(min);
        this.min = min;
    }
    
    protected static <T extends Number & Comparable<T>> void checkPreconditions(final T min) {
        if(min == null) {
            throw new NullPointerException("min should not be null");
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Object execute(final Object value, final CsvContext context) {
        
        validateInputNotNull(value, context);
        
        final Class<?> exepectedClass = getMin().getClass();
        if(!exepectedClass.isAssignableFrom(value.getClass())) {
            throw new SuperCsvCellProcessorException(exepectedClass, value, context, this);
        }
        
        final T result = ((T) value);
        
        if(result.compareTo(min) < 0) {
            throw new SuperCsvConstraintViolationException(
                    String.format("%s does not lie the min (%s) values (inclusive)", result, min),
                    context, this);
        }
            
        return next.execute(result, context);
    }
    
    @Override
    public String getMessageCode() {
        return Min.class.getCanonicalName() + ".violated";
    }
    
    @Override
    public Map<String, ?> getMessageVariable() {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("min", getMin());
        return vars;
    }
    
    @Override
    public String formatValue(final Object value) {
        if(value == null) {
            return "";
        }
        
        if(value instanceof Number) {
            final Number number = (Number) value;
            if(getFormatter() != null) {
                return new NumberFormatWrapper(getFormatter()).format(number);
            }
            
        }
        
        return value.toString();
    }
    
    public T getMin() {
        return min;
    }
    
    public NumberFormat getFormatter() {
        return formatter;
    }
    
    public Min<T> setFormatter(NumberFormat formatter) {
        this.formatter = formatter;
        return this;
    }
    
}

/*
 * CsvColumn.java
 * created in 2013/03/05
 *
 * (C) Copyright 2003-2013 GreenDay Project. All rights reserved.
 */
package org.supercsv.ext.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.supercsv.ext.builder.AbstractCellProcessorBuilder;
import org.supercsv.ext.builder.NullCellProcessorBuilder;


/**
 * Annotation for CSV "Column".
 * 
 * @version 01-00
 * @since 01-00
 * @author T.TSUCHIE
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CsvColumn {
    
    /**
     * index of column.
     * <p>start with zero(0).
     * @return
     */
    int position();
    
    /**
     * Header column label.
     * <p>if label omiited, then using field name.
     */
    String label() default "";
    
    /**
     * optional colums.
     * <p>if set the true, set CellProcessor for 'Optional'
     * <p>if set the false, set CellProcessor for 'NotNull'
     * @return
     */
    boolean optional() default false;
    
    /**
     * trimming on read/write
     * <p>set CellProcessor for 'Trim'
     * @return
     */
    boolean trim() default false;
    
    /**
     * default value.
     * <p>set CellProcessor for 'ConvertNullTo'
     * <p>When type is 'String', set the magic value '@empty' as empty ''.
     * @return
     */
    String inputDefaultValue() default "";
    
    /**
     * default value.
     * <p>set CellProcessor for 'ConvertNullTo'
     * <p>When type is 'String', set the magic value '@empty' as empty ''.
     * @return
     */
    String outputDefaultValue() default "";
    
    
    /**
     * unique column
     * <p>set CellProcessor for 'Unique'
     * @return
     */
    boolean unique() default false;
    
    /**
     * eqauals value
     * <p>set CellProcessor for 'Equals'
     * @return
     */
    String equalsValue() default "";
    
    /**
     * custom builder class
     * @return
     */
    @SuppressWarnings("rawtypes")
    Class<? extends AbstractCellProcessorBuilder> builderClass() default NullCellProcessorBuilder.class;
}
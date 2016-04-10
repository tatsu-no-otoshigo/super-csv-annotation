package org.supercsv.ext.builder.time;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;
import java.util.Optional;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.time.FmtLocalDateTime;
import org.supercsv.cellprocessor.time.ParseLocalDateTime;
import org.supercsv.ext.annotation.CsvDateConverter;
import org.supercsv.ext.exception.SuperCsvInvalidAnnotationException;

/**
 * The cell processor builder for {@link LocalDateTime}.
 *
 * @since 1.2
 * @author T.TSUCHIE
 *
 */
public class LocalDateTimeCellProcessorBuilder extends AbstractTemporalAccessorCellProcessorBuilder<LocalDateTime> {
    
    @Override
    protected String getDefaultPattern() {
        return "yyyy/MM/dd HH:mm:ss";
    }
    
    @Override
    protected LocalDateTime parseTemporal(final String value, final DateTimeFormatter formatter) {
        return LocalDateTime.parse(value, formatter);
    }
    
    @Override
    public LocalDateTime getParseValue(final Class<LocalDateTime> type, final Annotation[] annos, final String strValue) {
        
        final Optional<CsvDateConverter> converterAnno = getAnnotation(annos);
        
        final String pattern = getPattern(converterAnno);
        final ResolverStyle style = getResolverStyle(converterAnno);
        final Locale locale = getLocale(converterAnno);
        final ZoneId zone = getZoneId(converterAnno);
        final DateTimeFormatter formatter = createDateTimeFormatter(pattern, style, locale, zone);
        
        try {
            return LocalDateTime.parse(strValue, formatter);
            
        } catch(DateTimeParseException e) {
            throw new SuperCsvInvalidAnnotationException(
                    String.format("default '%s' value cannot parse to Date with pattern '%s'",
                            strValue, pattern), e);
            
        }
    }
    
    @Override
    public CellProcessor buildOutputCellProcessor(final Class<LocalDateTime> type, final Annotation[] annos,
            final CellProcessor processor, final boolean ignoreValidationProcessor) {
        
        final Optional<CsvDateConverter> converterAnno = getAnnotation(annos);
        final String pattern = getPattern(converterAnno);
        final ResolverStyle style = getResolverStyle(converterAnno);
        final Locale locale = getLocale(converterAnno);
        final ZoneId zone = getZoneId(converterAnno);
        
        final DateTimeFormatter formatter = createDateTimeFormatter(pattern, style, locale, zone);
        
        final Optional<LocalDateTime> min = getMin(converterAnno).map(s -> parseTemporal(s, formatter));
        final Optional<LocalDateTime> max = getMax(converterAnno).map(s -> parseTemporal(s, formatter));
        
        CellProcessor cp = processor;
        cp = (cp == null ? new FmtLocalDateTime(formatter) : new FmtLocalDateTime(formatter, cp));
        
        if(!ignoreValidationProcessor) {
            cp = prependRangeProcessor(min, max, formatter, cp);
        }
        
        return cp;
    }
    
    @Override
    public CellProcessor buildInputCellProcessor(final Class<LocalDateTime> type, final Annotation[] annos,
                final CellProcessor processor) {
        
        final Optional<CsvDateConverter> converterAnno = getAnnotation(annos);
        final String pattern = getPattern(converterAnno);
        final ResolverStyle style = getResolverStyle(converterAnno);
        final Locale locale = getLocale(converterAnno);
        final ZoneId zone = getZoneId(converterAnno);
        
        final DateTimeFormatter formatter = createDateTimeFormatter(pattern, style, locale, zone);
        
        CellProcessor cp = processor;
        cp = (cp == null ? new ParseLocalDateTime(formatter) : new ParseLocalDateTime(formatter, cp));
        
        return cp;
        
    }
    
}
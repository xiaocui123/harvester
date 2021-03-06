//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.31 at 09:28:31 AM CST 
//


package com.harvester.station.config;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.harvester package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.harvester
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Timeserial }
     * 
     */
    public Timeserial createTimeserial() {
        return new Timeserial();
    }

    /**
     * Create an instance of {@link VariableType }
     * 
     */
    public VariableType createVariableType() {
        return new VariableType();
    }

    /**
     * Create an instance of {@link Timeserial.Stations }
     * 
     */
    public Timeserial.Stations createTimeserialStations() {
        return new Timeserial.Stations();
    }

    /**
     * Create an instance of {@link TimeRangeType }
     * 
     */
    public TimeRangeType createTimeRangeType() {
        return new TimeRangeType();
    }

    /**
     * Create an instance of {@link Timeserial.Variables }
     * 
     */
    public Timeserial.Variables createTimeserialVariables() {
        return new Timeserial.Variables();
    }

    /**
     * Create an instance of {@link ColumnType }
     * 
     */
    public ColumnType createColumnType() {
        return new ColumnType();
    }

    /**
     * Create an instance of {@link AttributionType }
     * 
     */
    public AttributionType createAttributionType() {
        return new AttributionType();
    }

    /**
     * Create an instance of {@link StationType }
     * 
     */
    public StationType createStationType() {
        return new StationType();
    }

    /**
     * Create an instance of {@link VariableType.Attributions }
     * 
     */
    public VariableType.Attributions createVariableTypeAttributions() {
        return new VariableType.Attributions();
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.11.22 at 04:20:49 PM CST 
//


package com.harvester.v2.config;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.harvester.config package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.harvester.config
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataSet }
     * 
     */
    public DataSet createDataSet() {
        return new DataSet();
    }

    /**
     * Create an instance of {@link DataSet.Source }
     * 
     */
    public DataSet.Source createDataSetSource() {
        return new DataSet.Source();
    }

    /**
     * Create an instance of {@link DataSet.Station }
     * 
     */
    public DataSet.Station createDataSetStation() {
        return new DataSet.Station();
    }

    /**
     * Create an instance of {@link Variable }
     * 
     */
    public Variable createVariable() {
        return new Variable();
    }

    /**
     * Create an instance of {@link DataSet.MeaturedVariables }
     * 
     */
    public DataSet.MeaturedVariables createDataSetMeaturedVariables() {
        return new DataSet.MeaturedVariables();
    }

    /**
     * Create an instance of {@link DataSet.GlobalAttributes }
     * 
     */
    public DataSet.GlobalAttributes createDataSetGlobalAttributes() {
        return new DataSet.GlobalAttributes();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link DataSet.Source.JdbcType }
     * 
     */
    public DataSet.Source.JdbcType createDataSetSourceJdbcType() {
        return new DataSet.Source.JdbcType();
    }

    /**
     * Create an instance of {@link DataSet.Source.Exel }
     * 
     */
    public DataSet.Source.Exel createDataSetSourceExel() {
        return new DataSet.Source.Exel();
    }

}
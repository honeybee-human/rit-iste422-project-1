import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class EdgeConvertCreateDDL {
   static String[] products = { "MySQL" };
   protected EdgeTable[] tables; // master copy of EdgeTable objects
   protected EdgeField[] fields; // master copy of EdgeField objects
   protected int[] numBoundTables;
   protected int maxBound;
   protected StringBuffer sb;
   protected int selected;
   public static Logger logger = LogManager.getLogger(EdgeConvertCreateDDL.class.getName());

   public EdgeConvertCreateDDL(EdgeTable[] tables, EdgeField[] fields) {
      this.tables = tables;
      this.fields = fields;
      logger.info("Creating EdgeConvertDDL with tables " + Arrays.toString(tables) + " and fields "
            + Arrays.toString(fields));
      initialize();
   } // EdgeConvertCreateDDL(EdgeTable[], EdgeField[])

   public EdgeConvertCreateDDL() { // default constructor with empty arg list for to allow output dir to be set
                                   // before there are table and field objects

   } // EdgeConvertCreateDDL()

   public void initialize() {
      numBoundTables = new int[tables.length];
      maxBound = 0;
      sb = new StringBuffer();

      for (int i = 0; i < tables.length; i++) { // step through list of tables
         int numBound = 0; // initialize counter for number of bound tables
         int[] relatedFields = tables[i].getRelatedFieldsArray();
         for (int j = 0; j < relatedFields.length; j++) { // step through related fields list
            if (relatedFields[j] != 0) {
               numBound++; // count the number of non-zero related fields
            }
         }
         numBoundTables[i] = numBound;
         if (numBound > maxBound) {
            logger.warn("Setting maximum bound " + maxBound + " to number of bound tables " + numBound);
            maxBound = numBound;
         }
      }
   }

   protected EdgeTable getTable(int numFigure) {
      for (int tIndex = 0; tIndex < tables.length; tIndex++) {
         if (numFigure == tables[tIndex].getNumFigure()) {
            logger.debug("Retrieved table " + tables[tIndex]);
            return tables[tIndex];
         }
      }
      logger.warn("Table of " + numFigure + " not found.");
      return null;
   }

   protected EdgeField getField(int numFigure) {
      for (int fIndex = 0; fIndex < fields.length; fIndex++) {
         if (numFigure == fields[fIndex].getNumFigure()) {
            logger.debug("Retrieved field " + fields[fIndex]);
            return fields[fIndex];
         }
      }
      logger.warn("Field of " + numFigure + " not found.");
      return null;
   }

   public abstract String getDatabaseName();

   public abstract String getProductName();

   public abstract String getSQLString();

   public abstract void createDDL();

}// EdgeConvertCreateDDL

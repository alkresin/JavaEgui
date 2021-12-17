/*
 */

package alkresin.egui;

import java.util.ArrayList;

public class Efont {

   String sFamily;
   String sName;
   int Height;
   boolean Bold;
   boolean Italic;
   boolean Underline;
   boolean Strikeout;
   int Charset;

   static ArrayList<Efont> aFonts = new ArrayList<Efont>();

   public void Create () {

      aFonts.add( this );
   }
}

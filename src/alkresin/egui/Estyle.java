/*
 */

package alkresin.egui;

import java.util.ArrayList;

public class Estyle {

   String sName;
   int Orient;
   int[] Colors = null;
   int[] Corners = null;
   int BorderW;
   int BorderClr;
   String Bitmap;

   static ArrayList<Estyle> aStyles = new ArrayList<Estyle>();

   public void Create () {

      aStyles.add( this );
   }

}

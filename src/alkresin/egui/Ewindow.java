/*
 */

package alkresin.egui;

import java.util.ArrayList;

public class Ewindow extends Ewidget {

   public static Ewindow oWndMain = null;
   private static int iDialogCount = 0;
   static ArrayList<Ewindow> aDialogs = null;

   public static Ewindow InitMain( int x, int y, int w, int h, String title, String[] arr ) {

      Ewindow oWnd = new Ewindow();
      String sProps = "";

      oWnd.sType = "window";
      oWnd.sName = "main";

      String s = "[\"crmainwnd\",[" + x + "," + y + "," + w + "," + h +
         ",\"" + title + "\"]" + sProps + "]";

      Egui.WriteOut( s );

      oWndMain = oWnd;

      return oWnd;
   }

   public static Ewindow InitDialog( int x, int y, int w, int h, String title, String[] arr ) {

      Ewindow oWnd = new Ewindow();
      String sProps = "";

      oWnd.sType = "dialog";

      iDialogCount ++;
      oWnd.sName = "d" + iDialogCount;
      oWnd.aProps = null;
      oWnd.AddProps( arr );

      /*
      String s = "[\"crmainwnd\",[" + x + "," + y + "," + w + "," + h +
         ",\"" + title + "\"]" + sProps + "]";

      Egui.WriteOut( s );
      */
      aDialogs.add( oWnd );

      return oWnd;

   }

   public static Ewindow Get( String name ) {

      name = name.toLowerCase();
      if ( name == "main" )
         return oWndMain;

      return null;
   }

   public void Activate() {

      String s = "[\"actmainwnd\",[\"f\"]]";
      Egui.WriteOut( s );
      Egui.Wait();
   }
}

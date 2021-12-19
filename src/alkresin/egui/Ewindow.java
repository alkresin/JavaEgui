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

      oWnd.sType = "dialog";

      iDialogCount ++;
      if ( aDialogs == null )
         aDialogs = new ArrayList<Ewindow>();
      aDialogs.add( oWnd );
      oWnd.sName = "d" + iDialogCount;
      oWnd.aProps = null;
      oWnd.AddProps( arr );

      String sProps = "null";
      String s = "[\"crdialog\",\"" + oWnd.sName + "\",[" + x + "," + y + "," + w + "," + h +
         ",\"" + title + "\"]," + sProps + "]";

      Egui.WriteOut( s );

      return oWnd;

   }

   public static Ewindow Wnd( String name ) {

      name = name.toLowerCase();
      if ( name.equals( "main" ) )
         return oWndMain;
      else {
         for (int i = 0; i < aDialogs.size(); i++) {
            Ewindow o = aDialogs.get(i);
            if ( o.sName.equals( name ) )
               return o;
         }
      }

      return null;
   }

   public void Close() {

      String s = "[\"close\",\"" + sName + "\"]";
      Egui.WriteOut( s );
      this.delete();
   }

   public void delete() {

      if ( !sName.equals( "main" ) )
         for (int i = 0; i < aDialogs.size(); i++) {
            Ewindow o = aDialogs.get(i);
            if ( o.sName.equals( sName ) )
               aDialogs.remove(i);
         }
   }

   public void Activate() {

      String s;

      if ( sType.equals( "window" ) )
         s = "[\"actmainwnd\",[\"f\"]]";
      else
         s = "[\"actdialog\",\"" + sName + "\",\"f\",[\"f\"]]";

      Egui.WriteOut( s );
      Egui.Wait();
   }
}

/*
 */

package alkresin.egui;

import java.util.ArrayList;

public class Ewidget {

   String sType;
   String sName;
   ArrayList<String[]> aProps = null;

   Ewidget oParent;
   ArrayList<Ewidget> aWidgets = null;

   public Ewidget Add( String type, int x, int y, int w, int h, String title, String[] arr ) {

      Ewidget oWidg = new Ewidget();
      if ( aWidgets == null )
         aWidgets = new ArrayList<Ewidget>();
      aWidgets.add( oWidg );
      oWidg.oParent = this;
      oWidg.sType = type;

      oWidg.sName = "w" + aWidgets.size();
      oWidg.aProps = null;
      oWidg.AddProps( arr );

      oWidg.sName = sName + "." + oWidg.sName;

      String sProps = "";
      String s = "[\"addwidg\",\"" + oWidg.sType + "\",\"" + oWidg.sName + "\",[" +
         x + "," + y + "," + w + "," + h + ",\"" + title + "\"]" + sProps + "]";

      Egui.WriteOut( s );

      return oWidg;
   }

   public void AddProps( String[] arr ) {

      if ( !(arr == null) ) {
         if( aProps == null )
            aProps = new ArrayList<String[]>();
         for (int i = 0; i < arr.length; i++ ) {
            int iPos = arr[i].indexOf( ':' );
            if (iPos > 0) {
               String s1 = arr[i].substring( 0,iPos ).toLowerCase();
               if (s1.equals( "name" ))
                  sName = arr[i].substring( iPos+1 );
               else
                  aProps.add( new String[] { s1, arr[i].substring( iPos+1 ) } );
            }
            else
               aProps.add( new String[] { arr[i] } );
         }
      }
   }

   public void SetText( String sText ) {

      String s = "[\"set\",\"" + sName + "\",\"text\"," + Egui.String2Json(sText) + "]";
      Egui.WriteLog( s );
      Egui.WriteOut( s );
   }

   public String GetText() {

      String s = "[\"get\",\"" + sName + "\",\"text\"]";

      Egui.WriteOut( s );
      return Egui.GetStringOut().substring( 1 );
   }

   public void SetColor( int tColor, int bColor ) {

      String s = "[\"set\",\"" + sName + "\",\"color\",{" + tColor + "," + bColor + "}]";
      Egui.WriteOut( s );
   }

   public void SetImage( String sText ) {

      String s = "[\"set\",\"" + sName + "\",\"image\"," + Egui.String2Json(sText) + "]";
      Egui.WriteOut( s );
   }

   public void SetCallbackProc( String cbName, String sCode ) {

      String s = "[\"set\",\"" + sName + "\",\"cb." + cbName + "\",\"" + sCode + "\"]";
      Egui.WriteOut( s );
   }

   public void SetCallbackProc( String cbName, CBFunc func, String sProcName, String[] params ) {

      Egui.pFuncs.put( sProcName, func );

      String s = "[\"set\",\"" + sName + "\",\"cb." + cbName + "\"," +
         Egui.getscode( func, sProcName, sName, params ) + "]";

      Egui.WriteOut( s );
   }
}

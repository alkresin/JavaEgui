/*
 */

import alkresin.egui.*;

class Test1 {

   static Ewidget wLbl1;

   public static void main(String data[]) {

      System.out.println( "Init: " + Egui.Init( "log=1" ) );

      Ewindow oWnd = Ewindow.InitMain( 100, 100, 600, 300, "Egui test", null );

      Egui.Menu( "" );
         Egui.Menu( "File" );
            Egui.AddMenuItem( "Test1", 0, "hwg_MsgInfo(\"Test\")" );
            Egui.AddMenuItem( "Test2", 0, (s) -> fu0(s), "fu0", new String[] { "FuTest" } );
            Egui.AddMenuItem( "Info", 0, (s) -> fu2(s), "fu2", null );
            Egui.AddMenuSeparator();
            Egui.AddMenuItem( "Exit", 0, "hwg_EndWindow()" );
         Egui.EndMenu();
         Egui.Menu( "Help" );
            Egui.AddMenuItem( "About", 0, "hwg_MsgInfo(hb_version()+chr(10)+chr(13)+hwg_version(),'About')" );
         Egui.EndMenu();
      Egui.EndMenu();

      wLbl1 = oWnd.Add( "label", 220, 20, 160, 24, "This is a test", new String[] {"name:lbl1"} );

      Ewidget wBtn1 = oWnd.Add( "button", 250, 180, 100, 32, "Version", null );
      wBtn1.SetCallbackProc( "onclick", "hwg_msginfo( hwg_version() )" );

      Ewidget wBtn2 = oWnd.Add( "button", 250, 220, 100, 32, "Ok", null );
      //CBFunc func = (s) -> fu1(s);
      wBtn2.SetCallbackProc( "onclick", (s) -> fu1(s), "fu1", null );

      oWnd.Activate();
   }

   public static String[] fu0( String[] params ) {

      System.out.println( "fu0!" );
      System.out.println( params[0] + "/" + params[1] );
      return null;
   }

   public static String[] fu1( String[] params ) {

      wLbl1.SetText( "Новый текст." );
      System.out.println( "fu1!" );
      System.out.print( " " + params[0] );
      return null;
   }

   public static String[] fu2( String[] params ) {

      Egui.MsgInfo( Egui.GetVersion(1), "Version" );
      System.out.println( "fu2!" );
      return null;
   }

}

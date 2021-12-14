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
            Egui.AddMenuItem( "Info", 0, (s) -> fu2(s), "fu2", new String[] { "FuTest" } );
            Egui.AddMenuItem( "MsgYesNo", 0, (s) -> fu3(s), "fu3", null );
            Egui.AddMenuItem( "Dialog", 0, (s) -> CreateDialog(s), "CreateDialog", null );
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

   public static String[] fu1( String[] params ) {

      wLbl1.SetText( "Новый текст." );
      wLbl1.SetColor( 14939901, 3549952 );
      //System.out.println( "fu1!" );
      //System.out.print( " " + params[0] );
      return null;
   }

   public static String[] fu2( String[] params ) {

      Egui.MsgInfo( Egui.GetVersion(2), "Version" );
      //System.out.println( "fu2: " + params[0] + "/" + params[1] );
      return null;
   }

   public static String[] fu3( String[] params ) {

      if ( params[0].equals( "menu" ) )
         Egui.MsgYesNo( "My question?", "Box", (s) -> fu3(s), "fu3", "mm1" );
      else if ( params[0].equals( "mm1" ) )
         Egui.MsgInfo( (params[1].equals("t")? "Yes" : "No"), "Answer" );
      return null;
   }

   public static String[] CreateDialog( String[] params ) {

      Ewindow oDlg = Ewindow.InitDialog( 50, 50, 400, 300, "Egui dialog test", null );

      Ewidget wBtn1 = oDlg.Add( "button", 150, 220, 100, 32, "Ok", null );

      oDlg.Activate();

      return null;
   }

}

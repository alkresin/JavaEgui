/*
 */

package alkresin.egui;

import java.io.*;
import java.net.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class Egui {

   static int iPort = 3101;
   static String sServer = "guiserver";
   static String sIp = "127.0.0.1";
   static String sLog = "";

   static boolean bEndProg = false;

   private static Socket socketOut, socketIn;
   private static InputStreamReader sout_inStreamReader;
   private static OutputStreamWriter sout_outStreamWriter;
   private static InputStreamReader sin_inStreamReader;
   private static OutputStreamWriter sin_outStreamWriter;
   private static int iLastReadIn, iLastReadOut;
   private static byte[] arrIn = new byte[1024];
   private static byte[] arrOut = new byte[1024];

   public static HashMap<String,CBFunc> pFuncs = new HashMap<String,CBFunc>();

   private static String sMenu = "";
   private static int iStackLen = 0;

   private static boolean bPacket = false;
   private static String sPacketBuf = "";

   public static int Init( String sOpt ) {

      if ( !sOpt.isEmpty() ) {
         String sep = "\r\n";
         if ( !sOpt.contains( sep ) ) {
            sep = "\n";
         }
         String[] aOpt = sOpt.split( sep );
         String s;

         for( int i = 0; i<aOpt.length; i++ ) {
            s = aOpt[i].toLowerCase();
            if( s.startsWith( "guiserver" ) )
               sServer = s.substring(10).trim();
            else if( s.startsWith( "address" ) )
               sIp = s.substring(8).trim();
            else if( s.startsWith( "port" ) )
               try{
                 iPort = Integer.parseInt( s.substring(5).trim() );
               }
               catch (NumberFormatException ex) {
                  ex.printStackTrace();
               }
            else if( s.startsWith( "log" ) ) {
               if ( s.substring(4).trim().equals( "1" ) )
                  sLog = "-log1";
               else if ( s.substring(4).trim().equals( "2" ) )
                  sLog = "-log2";
            }
         }
      }

      if ( !sServer.isEmpty() ) {
         try {
            Runtime.getRuntime().exec( sServer + " -p" + iPort + " " + sLog );
         } catch( IOException e ) {
            System.out.println( "Error running the guiserver" );
            System.out.println( e );
            WriteLog( "Error running the guiserver" );
            return 1;
         }
         try { Thread.sleep( 100 ); } catch (InterruptedException e) {}
         System.out.println( "Guiserver has been started" );
      }

      boolean lErr = false;
      try { socketOut = new Socket( sIp, iPort ); } catch( Exception e ) { lErr = true; }
      if ( lErr ) {
         lErr = false;
         try { Thread.sleep( 1000 ); } catch (InterruptedException e) {}
         try { socketOut = new Socket( sIp, iPort ); }  catch( Exception e ) { lErr = true; }
         if ( lErr ) {
            lErr = false;
            try { Thread.sleep( 3000 ); } catch (InterruptedException e) {}
            try { socketOut = new Socket( sIp, iPort ); }  catch( Exception e ) { lErr = true; }
            if ( lErr ) {
               WriteLog( "Cannot connect to " + sIp + ":" + iPort );
               return 1;
            }
         }
      }
      System.out.println( "Connected to guiserver" );
      try {
         //sout_outStream = socketOut.getOutputStream();
         sout_outStreamWriter = new OutputStreamWriter( socketOut.getOutputStream() );
         //sout_inStream = socketOut.getInputStream();
         sout_inStreamReader = new InputStreamReader( socketOut.getInputStream() );

         ReadOut( false );
      } catch( Exception e ) { lErr = true; }

      WriteLog( Arrays.toString( Arrays.copyOf( arrOut,iLastReadOut ) ) );

      String sVer = new String( Arrays.copyOf( arrOut,iLastReadOut ) );
      System.out.println( sVer );

      lErr = false;
      try { socketIn = new Socket( sIp, iPort+1 ); } catch( Exception e ) { lErr = true; }
      if ( lErr ) {
         lErr = false;
         try { Thread.sleep( 1000 ); } catch (InterruptedException e) {}
         try { socketIn = new Socket( sIp, iPort+1 ); }  catch( Exception e ) { lErr = true; }
         if ( lErr ) {
            WriteLog( "Cannot connect to " + sIp + ":" + (iPort+1) );
            return 1;
         }
      }
      System.out.println( "Connected to guiserver, port " + (iPort+1) );
      try {
         //sin_outStream = socketIn.getOutputStream();
         sin_outStreamWriter = new OutputStreamWriter( socketIn.getOutputStream() );
         //sin_inStream = socketIn.getInputStream();
         sin_inStreamReader = new InputStreamReader( socketIn.getInputStream() );

         ReadIn( false );
      } catch( Exception e ) { lErr = true; }

      WriteLog( Arrays.toString( Arrays.copyOf( arrIn,iLastReadIn ) ) );

      return 0;
   }

   public static void Exit() {

      try {
         socketIn.close();
         socketOut.close();
      }
      catch( Exception exception ) {
         System.out.println( exception );
      }
   }

   public static void WriteLog( String cText ) {

      PrintWriter writer;

      try {
         writer = new PrintWriter( (new FileWriter( "egui.log", true )) );
      }
      catch (IOException e) {
         System.out.println(e);
         return;
      }

      writer.println( cText );
      writer.close();
   }

   public static boolean ReadOut( boolean bNonBlock ) {

      int i = 0, x, iLen = arrOut.length;

      try {
         if ( !bNonBlock || sout_inStreamReader.ready() ) {
            while( true ) {
               if ( sout_inStreamReader.ready() ) {
                  x = sout_inStreamReader.read();
                  if( x == -1 || x == 10 )
                     break;
                  if ( i == iLen ) {
                     byte[] arr = new byte[iLen + 1024];
                     System.arraycopy( arrOut, 0, arr, 0, iLen );
                     iLen += 1024;
                     arrOut = arr;
                  }
                  arrOut[i] = (byte) x;
                  i ++;
               }
            }
         }
      }
      catch( Exception exception ) {
         bEndProg = true;
         System.out.println( exception );
         return false;
      }
      iLastReadOut = i;

      return true;
   }

   public static boolean ReadIn( boolean bNonBlock ) {

      int i = 0, x, iLen = arrIn.length;

      try {
         if ( !bNonBlock || sin_inStreamReader.ready() ) {
            while( true ) {
               if ( sin_inStreamReader.ready() ) {
                  x = sin_inStreamReader.read();
                  if( x == -1 || x == 10 )
                     break;
                  if ( i == iLen ) {
                     byte[] arr = new byte[iLen + 1024];
                     System.arraycopy( arrIn, 0, arr, 0, iLen );
                     iLen += 1024;
                     arrIn = arr;
                  }
                  arrIn[i] = (byte) x;
                  i ++;
               }
            }
         }
      }
      catch( Exception exception ) {
         bEndProg = true;
         System.out.println( exception );
         return false;
      }
      iLastReadIn = i;

      if( iLastReadIn > 0 ) {
         Handler( Arrays.copyOf( arrIn,iLastReadIn ) );
      }

      return true;
   }

   public static boolean WriteOut( String text ) {

      if( bPacket ) {
         sPacketBuf += "," + text;
      } else {
         try {
            sout_outStreamWriter.write( 43 ); // '+'
            sout_outStreamWriter.write( text );
            sout_outStreamWriter.write( 10 );
            sout_outStreamWriter.flush();
         }
         catch( Exception exception ) {
            bEndProg = true;
            System.out.println( exception );
            return false;
         }

         iLastReadOut = 0;
         do {
            if ( !ReadIn( true ) )
               break;
            if ( !ReadOut( true ) )
               break;
         } while (iLastReadOut==0);
      }
      return true;
   }

   public static boolean WriteIn( String text ) {

      try {
         sin_outStreamWriter.write( 43 ); // '+'
         sin_outStreamWriter.write( text );
         sin_outStreamWriter.write( 10 );
         sin_outStreamWriter.flush();
      }
      catch( Exception exception ) {
         bEndProg = true;
         System.out.println( exception );
         return false;
      }
      return true;
   }

   public static String GetStringOut() {

      String sBuff;
      try {
         sBuff = new String( arrOut, 0, iLastReadOut, "UTF-8" );
      }
      catch (UnsupportedEncodingException e) {
         System.out.println( e );
         return "";
      }
      return sBuff;
   }

   public static boolean Handler( byte[] buff ) {

      String sBuff;
      String cmd;
      boolean bSend = false;

      try {
         sBuff = new String( buff, "UTF-8" );
      }
      catch (UnsupportedEncodingException e) {
         System.out.println( e );
         return false;
      }

      WriteLog( sBuff );
      if ( !sBuff.startsWith( "+[\"" ) )
         return false;

      int nPos, nPos2 = sBuff.indexOf( '"', 3 );
      if ( nPos2 == -1 )
         return false;

      cmd = sBuff.substring( 3, nPos2 );
      //System.out.println( cmd );
      if( cmd.equals( "runproc" ) || cmd.equals( "runfunc" ) ) {
         if( cmd.equals( "runproc" ) )
            WriteIn( "Ok" );
         bSend = true;
         nPos = sBuff.indexOf( '"', nPos2+1 ) + 1;
         nPos2 = sBuff.indexOf( '"', nPos );
         if (nPos > 0 && nPos2 > nPos ) {
            CBFunc func = pFuncs.get( sBuff.substring( nPos, nPos2 ) );
            if ( !(func == null) ) {
               //System.out.println( "func found!" );
               String[] aParams = null;
               nPos = sBuff.indexOf( '[', nPos2 ) + 1;
               nPos2 = sBuff.indexOf( ']', nPos );
               if (nPos > 0 && nPos2 > nPos ) {
                  aParams = Split( sBuff.substring( nPos, nPos2 ).replace( "\\\"", "" ), ',' );
               }
               if( cmd.equals( "runfunc" ) ) {
                  aParams = func.run( aParams );
                  WriteIn( Arr2Json(aParams) );
               }
               else
                  func.run( aParams );
            }
         }
      }
      else if( cmd.equals( "endapp" ) ) {
         WriteIn( "Goodbye" );
         try { Thread.sleep( 100 ); } catch (InterruptedException e) {}
         bEndProg = true;
         Exit();
      }
      else if( cmd.equals( "exit" ) ) {
         WriteIn( "Ok" );
         bSend = true;

         nPos = sBuff.indexOf( '"', nPos2+1 ) + 1;
         nPos2 = sBuff.indexOf( '"', nPos );
         if (nPos > 0 && nPos2 > nPos ) {
            String sName = sBuff.substring( nPos, nPos2 );
            //System.out.println( sName );
            if ( sName.equals( "main" ) ) {
            } else {
               Ewindow o = Ewindow.Wnd( sName );
               if ( o != null )
                  o.Delete();
            }
         }
      }

      if (!bSend )
         WriteIn( "Ok" );

      return true;
   }

   public static void Wait() {

      while( !bEndProg ) {
         try { Thread.sleep( 1 ); } catch (InterruptedException e) {}
         ReadIn( true );
      }
   }

   public static void Menu( String title ) {

      if ( sMenu.isEmpty() )
         sMenu = "[\"menu\",[";
      else if ( !title.isEmpty() ) {
         if ( !sMenu.endsWith( "[" ) )
            sMenu += ",";
         sMenu += "[\"" + title + "\",[";
         iStackLen ++;
      }
   }

   public static void EndMenu() {

      sMenu += "]]";
      if ( iStackLen == 0 ) {
         WriteOut( sMenu );
         sMenu = "";
      }
      else
         iStackLen --;
   }

   public static void AddMenuItem( String title, int id, String sCode ) {

      if ( !sMenu.endsWith( "[" ) )
         sMenu += ",";
      sMenu += "[\"" + title + "\"," + String2Json(sCode) + "," + ((id==0)? "null" : id) + "]";
   }

   public static void AddMenuItem( String title, int id, CBFunc func, String sProcName, String[] params ) {

      pFuncs.put( sProcName, func );

      if ( !sMenu.endsWith( "[" ) )
         sMenu += ",";
      sMenu += "[\"" + title + "\"," +
         getscode( func, sProcName, "menu", params ) +
         "," + ((id==0)? "null" : id) + "]";
   }

   public static void AddMenuSeparator() {

      if ( !sMenu.endsWith( "[" ) )
         sMenu += ",";
      sMenu += "[\"-\"]";
   }

   public static void MsgInfo( String sMessage, String sTitle ) {

      String s = "[\"common\",\"minfo\",\"\",\"\"," +
         String2Json(sMessage) + ",\"" + sTitle + "\"]";
      Egui.WriteOut( s );
   }

   public static void MsgInfo( String sMessage, String sTitle, CBFunc func, String sProcName, String sName ) {

      Egui.pFuncs.put( sProcName, func );
      String s = "[\"common\",\"minfo\",\"" + sProcName + "\",\"" + sName + "\"," +
         String2Json(sMessage) + ",\"" + sTitle + "\"]";
      Egui.WriteOut( s );
   }

   public static void MsgStop( String sMessage, String sTitle ) {

      String s = "[\"common\",\"mstop\",\"\",\"\"," +
         String2Json(sMessage) + ",\"" + sTitle + "\"]";
      Egui.WriteOut( s );
   }

   public static void MsgStop( String sMessage, String sTitle, CBFunc func, String sProcName, String sName ) {

      Egui.pFuncs.put( sProcName, func );
      String s = "[\"common\",\"mstop\",\"" + sProcName + "\",\"" + sName + "\"," +
         String2Json(sMessage) + ",\"" + sTitle + "\"]";
      Egui.WriteOut( s );
   }

   public static void MsgYesNo( String sMessage, String sTitle, CBFunc func, String sProcName, String sName ) {

      Egui.pFuncs.put( sProcName, func );
      String s = "[\"common\",\"myesno\",\"" + sProcName + "\",\"" + sName + "\"," +
         String2Json(sMessage) + ",\"" + sTitle + "\"]";
      Egui.WriteOut( s );
   }

   public static void MsgGet( String sMessage, String sTitle, CBFunc func, String sProcName, String sName ) {

      Egui.pFuncs.put( sProcName, func );
      String s = "[\"common\",\"mget\",\"" + sProcName + "\",\"" + sName + "\"," +
         String2Json(sMessage) + ",\"" + sTitle + "\",null]";
      Egui.WriteOut( s );
   }

   public static void Choice( String[] arr, String sTitle, CBFunc func, String sProcName, String sName ) {

      Egui.pFuncs.put( sProcName, func );
      String s = "[\"common\",\"mget\",\"" + sProcName + "\",\"" + sName + "\"," +
         Arr2Json(arr) + ",\"" + sTitle + "\",null]";
      Egui.WriteOut( s );
   }

   public static void EvalProc( String sCode ) {

      String s = "[\"evalcode\"," + String2Json(sCode) + "]";

      Egui.WriteOut( s );
   }

   public static String EvalFunc( String sCode ) {

      String s = "[\"evalcode\"," + String2Json(sCode) + ",\"t\"]";

      Egui.WriteOut( s );
      return Egui.GetStringOut().substring( 1 );
   }

   public static String GetVersion( int n ) {

      String s = "[\"getver\"," + n + "]";

      Egui.WriteOut( s );
      return Egui.GetStringOut().substring( 1 );
   }

   public static void BeginPacket() {

      bPacket = true;
      sPacketBuf = "[\"packet\"";
   }

   public static void EndPacket() {

      bPacket = false;
      WriteOut( sPacketBuf + "]" );
      sPacketBuf = "";
   }

   public static String[] Split( String input, char cSep ) {

      List<String> tokens = new ArrayList<String>();
      int iStartPos = 0;
      char c, cQuo = ' ';

      for ( int iCurrPos = 0; iCurrPos < input.length(); iCurrPos++ ) {
          c = input.charAt( iCurrPos );
          if ( c == '\"' || c == '\'') {
              cQuo = (cQuo==' ')? c : ' ';
          }
          else if ( c == cSep && cQuo == ' ' ) {
              tokens.add(input.substring( iStartPos, iCurrPos ));
              iStartPos = iCurrPos + 1;
          }
      }

      String lastToken = input.substring( iStartPos );
      if (lastToken.equals( ""+cSep )) {
          tokens.add( "" );
      } else {
          tokens.add( lastToken );
      }
      return tokens.toArray( new String[0] );
   }

   public static String String2Json( String s ) {

      if ( s.contains( "\"" ) )
         s = s.replace( "\"", "\\\"" );
      return "\"" + s + "\"";
   }

   public static String Arr2Json( String[] arr ) {

      String s = "[";

      if ( arr != null && arr.length > 0 ) {
         s += String2Json( arr[0] );
         for ( int i = 1; i < arr.length; i++ )
            s += "," + String2Json( arr[i] );
      }
      return s + "]";
   }

   public static String getscode( CBFunc func, String sProcName, String sTarget, String[] params ) {

      String sCode = "\"pgo(\\\"" + sProcName + "\\\",{\\\"" + sTarget + "\\\"";
      if ( params != null )
         for ( int i = 0; i < params.length; i++ )
            sCode += ",\\\"" + params[i] + "\\\"";
      sCode += "})\"";
      return sCode;
   }

}

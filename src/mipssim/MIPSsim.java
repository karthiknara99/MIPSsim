package mipssim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 *
 * @author pooja
 */
public class MIPSsim
{
    private static ArrayList<String> INM = new ArrayList<>();
    private static ArrayList<String> INB = new ArrayList<>();
    private static ArrayList<String> AIB = new ArrayList<>();
    private static ArrayList<String> LIB = new ArrayList<>();
    private static ArrayList<String> ADB = new ArrayList<>();
    private static ArrayList<String> REB = new ArrayList<>();
    private static int[] registers = new int[8];
    private static int[] datamemory = new int[8];

    public static void myPrint( int j, int z ) throws IOException
    {
        try( PrintWriter out = new PrintWriter( new FileWriter("simulation.txt", true) ) )
        {
            int i;
            String pr = "";
            
            //Step Number
            out.println("STEP " + j + ":" );
            
            //INM
            pr = "";
            out.print("INM:");
            for( i = 0; i < INM.size(); i++ )
                pr += INM.get(i) + ",";
            if( !pr.equals("") )
                pr = pr.substring( 0, pr.length()-1 );
            out.println(pr);
            
            //INB
            pr = "";
            out.print( "INB:");
            for( i = 0; i < INB.size(); i++ )
                pr += INB.get(i) + ",";
            if( !pr.equals("") )
                pr = pr.substring( 0, pr.length()-1 );
            out.println(pr);
            
            //AIB
            pr = "";
            out.print("AIB:");
            for( i = 0; i < AIB.size(); i++ )
                pr += AIB.get(i) + ",";
            if( !pr.equals("") )
                pr = pr.substring( 0, pr.length()-1 );
            out.println(pr);
            
            //LIB
            pr = "";
            out.print("LIB:");
            for( i = 0; i < LIB.size(); i++ )
                pr += LIB.get(i) + ",";
            if( !pr.equals("") )
                pr = pr.substring( 0, pr.length()-1 );
            out.println(pr);
            
            //ADB
            pr = "";
            out.print("ADB:");
            for( i = 0; i < ADB.size(); i++ )
                pr += ADB.get(i) + ",";
            if( !pr.equals("") )
                pr = pr.substring( 0, pr.length()-1 );
            out.println(pr);
            
            //REB
            pr = "";
            out.print("REB:");
            for( i = 0; i < REB.size(); i++ )
                pr += REB.get(i) + ",";
            if( !pr.equals("") )
                pr = pr.substring( 0, pr.length()-1 );
            out.println(pr);
            
            //RGF
            pr = "";
            out.print("RGF:");
            for( i = 0; i < 8; i++ )
                pr += "<R" + i + "," + registers[i] + ">,";
            pr = pr.substring( 0, pr.length()-1 );
            out.println(pr);
            
            //DAM
            pr = "";
            out.print("DAM:");
            for( i = 0; i < 8; i++ )
                pr += "<" + i + "," + datamemory[i] + ">,";
            pr = pr.substring( 0, pr.length()-1 );
            out.print(pr);
            if( z == 0 )
            {
                out.println("");
                out.println("");
            }
            out.close();
        }
    }
    
    public static void main(String[] args) throws IOException
    {   
        File f1 = new File("simulation.txt");
        if (f1.exists())
           f1.delete();
        
        //Read Instructions FIle
        try( BufferedReader br = new BufferedReader( new FileReader("instructions.txt") ) )
        {
            String line;
            while ( ( line = br.readLine() ) != null )
                INM.add(line);
        }
        
        //Read Registers File
        try( BufferedReader br = new BufferedReader( new FileReader("registers.txt") ) )
        {
            String line;
            while ( ( line = br.readLine() ) != null )
            {
                line = line.substring( 1, line.length()-1 );
                String[] input = line.split(",");
                int loc = Integer.parseInt( input[0].substring(1) );
                registers[loc] = Integer.parseInt( input[1] );
            }
        }
        
        //Read Data Memory File
        try( BufferedReader br = new BufferedReader( new FileReader("datamemory.txt") ) )
        {
            String line;
            while ( ( line = br.readLine() ) != null )
            {
                line = line.substring( 1, line.length()-1 );
                String[] input = line.split(",");
                int loc = Integer.parseInt( input[0] );
                datamemory[loc] = Integer.parseInt( input[1] );
            }
        }
        
        int j = 0;
        while( !INM.isEmpty() || !INB.isEmpty() || !AIB.isEmpty() || !LIB.isEmpty() || !ADB.isEmpty() || !REB.isEmpty() )
        {
            myPrint(j,0);
            if( !REB.isEmpty() )
            {
                String s = REB.get(0);
                REB.remove(0);
                s = s.substring( 1, s.length()-1 );
                String[] input = s.split(",");
                input[0] = input[0].substring(1);
                int loc = Integer.parseInt(input[0]);
                int v1 = Integer.parseInt(input[1]);
                registers[loc] = v1;
                //System.out.println( "Reg: " + loc + ": " + registers[loc] );
            }
            if( !ADB.isEmpty() )
            {
                String s = ADB.get(0);
                ADB.remove(0);
                s = s.substring( 1, s.length()-1 );
                String[] input = s.split(",");
                int loc = Integer.parseInt(input[1]);
                s = "<" + input[0] + "," + datamemory[loc] + ">";
                REB.add(s);
                //System.out.println( "REB: " + s );
            }
            if( !LIB.isEmpty() )
            {
                String s = LIB.get(0);
                LIB.remove(0);
                s = s.substring( 1, s.length()-1 );
                String[] input = s.split(",");
                int v1 = Integer.parseInt(input[2]) + Integer.parseInt(input[3]);
                s = "<" + input[1] + "," + v1 + ">";
                ADB.add(s);
                //System.out.println( "ADB: " + s );
            }
            if( !AIB.isEmpty() )
            {
                String s = AIB.get(0);
                AIB.remove(0);
                s = s.substring( 1, s.length()-1 );
                String[] input = s.split(",");
                int v1 = Integer.parseInt(input[2]);
                int v2 = Integer.parseInt(input[3]);
                int out = 0;
                switch(input[0])
                {
                    case "ADD": out = v1 + v2;  break;
                    case "SUB": out = v1 - v2;  break;
                    case "AND": out = v1 & v2;  break;
                    case "OR":  out = v1 | v2;  break;
                }
                s = "<" + input[1] + "," + out + ">";
                REB.add(s);
                //System.out.println( "REB: " + s );
            }
            if( !INB.isEmpty() )
            {
                String s = INB.get(0);
                INB.remove(0);
                if( s.substring( 1, s.indexOf(',') ).compareTo("LD") == 0 )
                {
                    LIB.add(s);
                    //System.out.println( "LIB: " + s );
                }
                else
                {
                    AIB.add(s);
                    //System.out.println( "AIB: " + s );
                }
            }
            if( !INM.isEmpty() )
            {
                String s = INM.get(0);
                s = s.substring( 1, s.length()-1 );
                INM.remove(0);
                String[] input = s.split(",");
                int v1 = Integer.parseInt( input[2].substring(1) );
                int v2 = Integer.parseInt( input[3].substring(1) );
                s = "<" + input[0] + "," + input[1] + "," + registers[v1] + "," + registers[v2] + ">";
                INB.add(s);
                //System.out.println( "INB: " + s );
            }
            j++;
        }
        myPrint(j,1);
    }
    
}
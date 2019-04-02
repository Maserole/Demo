package Project;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Project1_MokoeleMJ {

    @Test
    public void main() throws SQLException {

        String file = "ProjectDay1.txt";

        File myFile = new File(file);

        //Checking If The File Is Empty
        //assertThat("The File Is Empty",false,is(fileEmpty(myFile)));

        //Reading a file
        int count = 0;
        String line = "";

        try {

            Scanner reader = new Scanner(myFile);
            while(reader.hasNext()){

                line = reader.nextLine(); //Reading The Line
                dataBaseTest(line);
            }

        } catch (FileNotFoundException e) {

            assertThat("File Does Not Exist.",true,is(fileExists(myFile))); //File Does Not Exist Check
        }

    }
    public static boolean fileExists(File str){

        boolean exists;

        if(str.exists()){

            exists = true;
        }else{

            exists = false;
        }

        return exists;
    }
    public  static boolean fileEmpty(File str){

        boolean empty;

        if(str.length() == 0){

            empty = true;
        }else{

            empty = false;
        }

        return empty;
    }

    public void dataBaseTest(String myString) throws SQLException {

        String []myArr = myString.split(",");
        String DB_URL = "";
        Connection conn = null;
        String myQuery = "";
        String table = "";
        String column1 = "", column2 = "";
        PreparedStatement statemnt = null;
        ResultSet result;

        for (int i = 0; i < myArr.length ; i++) {

            if(i == 0){

                String path = "chinook.db";
                DB_URL = "jdbc:sqlite:"+path;

                File myDBFile = new File(path);

                if(myDBFile.exists()){
                    try {

                        conn = DriverManager.getConnection(DB_URL);
                        System.out.println("Connected Successfully.");

                    } catch (SQLException e) {

                        break;
                    }
                } else{

                    assertThat("Database Does Not Exist.",true, is(myDBFile.exists()));
                }
            }else if(i == 1){

                //Validating IF The Given/Extracted Table Name Exist In The Database.
                myQuery = "select * from " + myArr[i];
                statemnt = conn.prepareStatement(myQuery);

                result = statemnt.executeQuery();

                if(result.next()){

                    table = myArr[i];
                    System.out.println(table + " Table Exist.");

                }else{

                    assertThat("The Table Does Not Exist In The Database",true,is(result.next()));
                    break;
                }
            }else if(i ==2){

                //Validating IF The Given/Extracted Column Name Exist Or Not.
                myQuery = "select "+myArr[i] +" from " + table;
                statemnt = conn.prepareStatement(myQuery);
                result = statemnt.executeQuery();

                if(result.next()){

                    column1 = myArr[i];
                }else{

                    assertThat("The Column With Heading "+myArr[i]+" Does Not Exist.",false,is(result.next()));
                    break;
                }
            }else if(i == 3){

                //Validating IF The Given/Extracted Column Name With Value Exist
                myQuery = "select "+column1+" from "+table+" where "+column1+"="+myArr[i];
                statemnt = conn.prepareStatement(myQuery);
                result = statemnt.executeQuery();

                if(result.next()){

                    System.out.println("Data Exist.");
                }else{

                    assertThat("No Data From "+table+" With "+column1+"="+myArr[i],true,is(result.next()));
                    break;
                }
            }else if(i == 4){

                //Validating IF The Given/Extracted Column Name Exist In The Table
                myQuery = "select "+myArr[i]+" from "+table;
                statemnt = conn.prepareStatement(myQuery);
                result = statemnt.executeQuery();

                if(result.next()){

                    System.out.println("Column Exist.");
                    column2 = myArr[i];
                }else{

                    assertThat("The Column With Heading "+myArr[i]+" Does Not Exist In Table"+table,true,is(result.next()));
                    break;
                }
            }else if(i == 5){

                //Validating IF The Given/Extracted Column Names With Values Exist in The Table.
                myQuery = "select "+column1+","+column2+" from "+table+" where "+column1+"="+myArr[3]+" and "+column2+"="+"\""+ myArr[i]+ "\"" ;
                statemnt = conn.prepareStatement(myQuery);
                result = statemnt.executeQuery();

                if(result.next()){

                    System.out.println("OK. Query Executed Without Any Issues");
                }else{

                    assertThat("No Data In "+table+" table With "+column2+" = "+myArr[i],true,is(result.next()));
                    break;
                }
            }
        }


    }
}

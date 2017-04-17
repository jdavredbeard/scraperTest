package rc.cracking;
//import com.mysql.jdbc.*;
//import com.mysql.jdbc.Driver;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        String user = System.getenv("MYSQL_USER");
        String pw = System.getenv("MYSQL_PW");
        String host = System.getenv("MYSQL_HOST");
        String database = "scraperTest";
        try {

            Connection conn = getDBconnection(host,database,user,pw);
            System.out.println("Database connection established. " + conn.getCatalog());

            Document doc = Jsoup.connect("https://philadelphia.craigslist.org").get();
            String title = doc.title();

            //System.out.println("Main.main");
            //System.out.println("doc = " + doc);
            System.out.println("title = " + title);

            Element hhh = doc.getElementById("hhh");
            Elements links = hhh.getElementsByTag("a");

            for (Element link: links) {
                String url = link.attr("href");
                String text = link.text();
                Statement st = conn.createStatement();
                String updateString = "INSERT INTO links (href,text) VALUES (\"" + url +"\",\"" + text+"\");";
                System.out.println(updateString);
                st.executeUpdate(updateString);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }


    }

    public static Connection getDBconnection(String host, String db, String user, String pwd) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + host +"/" + db,
                    user, pwd);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return conn;
    }


}

import java.util.*;
import java.sql.*;
import oracle.jdbc.*;
import oracle.sql.*;
import java.io.*;

public class TemplateLoader {

    private String fileName;
    private String type;
    private String mech;
    private String group;
    private String url;
    private String userName;
    private String password;

    public TemplateLoader(String fileName, String type, String mech, String group, String dbProperties) {
        this.fileName = fileName;
        this.type = type;
        this.mech = mech;

        if (group.equals("PC") || group.equals("PNC") || group.equals("TEST")) {
            this.group = "PGM";
        } else if (group.equals("SNC") || group.equals("SC")) {
            this.group = "SPEC";
        }

        StringTokenizer st = new StringTokenizer(dbProperties, ",");
        url = (String) st.nextToken();
        userName = (String) st.nextToken();
        password = (String) st.nextToken();

        System.out.println("filename " + fileName + "  type " + type + "  mech " + mech + "  group " + this.group);

    }

    public void replaceTemplate() throws Exception {
        Connection conn = null;
        try {

            System.out.println("REPLACE TEMPLATE");

            conn = getConnection();

            conn.setAutoCommit(false);

            String template = readFile();

            String id = null;
            Statement stmt = conn.createStatement();

            // Get a new id

            ResultSet res = stmt.executeQuery("select frm_seq.nextval from dual");
            if (res.next()) {
                id = res.getString(1);

                System.out.println("ID " + id);
            }
            res.close();

            System.out.println("LOADING CLOB ......");

            conn.setAutoCommit(false);

            // Insert a new Template CLOB

            String insertSql = "INSERT INTO FORM_TEMPLATES_T (ID,TEMPLATE_HTML,REVISION_NUM) VALUES(?,EMPTY_CLOB(),?)";

            OraclePreparedStatement ops = (OraclePreparedStatement) conn.prepareStatement(insertSql);
            ops.setString(1, id);
            ops.setString(2, "1");
            ops.executeUpdate();

            String selectSql = "SELECT TEMPLATE_HTML FROM FORM_TEMPLATES_T  WHERE ID=" + id;
            ops = (OraclePreparedStatement) conn.prepareStatement(selectSql);
            res = ops.executeQuery();
            CLOB clob = null;
            if (res.next()) {
                clob = (oracle.sql.CLOB) res.getClob("TEMPLATE_HTML");
            }
            java.io.Writer writer = ((oracle.sql.CLOB) clob).getCharacterOutputStream();
            writer.write(template);
            writer.flush();
            writer.close();
            ops.close();
            res.close();

            // Change the FTM_ID on the Form Grant Matrix

            String sql =
                "UPDATE form_grant_matrix_t set ftm_id ="
                    + id
                    + "WHERE form_role_code='"
                    + group
                    + "' AND "
                    + "appl_type_code='"
                    + type
                    + "' AND activity_code='"
                    + mech
                    + "'";

            stmt.executeUpdate(sql);

            conn.commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

        checkClob();

    }


    public void loadNewTemplate() throws Exception {
        Connection conn = null;

        try {

            conn = getConnection();

            String template = readFile();

            String id = null;
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("select frm_seq.nextval from dual");
            if (res.next()) {
                id = res.getString(1);

                System.out.println("ID " + id);
            }
            stmt.close();
            res.close();

            System.out.println("LOADING CLOB ......");

            conn.setAutoCommit(false);

            String insertSql = "INSERT INTO FORM_TEMPLATES_T (ID,TEMPLATE_HTML,REVISION_NUM) VALUES(?,EMPTY_CLOB(),?)";

            OraclePreparedStatement ops = (OraclePreparedStatement) conn.prepareStatement(insertSql);
            ops.setString(1, id);
            ops.setString(2, "1");
            ops.executeUpdate();

            String selectSql = "SELECT TEMPLATE_HTML FROM FORM_TEMPLATES_T  WHERE ID=" + id;
            ops = (OraclePreparedStatement) conn.prepareStatement(selectSql);
            res = ops.executeQuery();
            CLOB clob = null;
            if (res.next()) {
                clob = (oracle.sql.CLOB) res.getClob("TEMPLATE_HTML");
            }
            java.io.Writer writer = ((oracle.sql.CLOB) clob).getCharacterOutputStream();
            writer.write(template);
            writer.flush();
            writer.close();
            ops.close();
            res.close();

            String insertSql2 =
                "INSERT INTO FORM_GRANT_MATRIX_T "
                    + "(FTM_ID,FORM_ROLE_CODE,APPL_TYPE_CODE,MAJOR_ACTIVITY_CODE,ACTIVITY_CODE)"
                    + "VALUES(?,?,?,?,?)";

            ops = (OraclePreparedStatement) conn.prepareStatement(insertSql2);
            ops.setString(1, id);
            ops.setString(2, group);
            ops.setString(3, type);
            ops.setString(4, mech.substring(0, 1));
            ops.setString(5, mech);
            ops.executeUpdate();

            conn.commit();

            System.out.println("CLOB LOADING COMPLETE");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }

    }

    private String readFile() throws Exception {

        File f = new File(fileName);
        long length = f.length();
        FileReader fr = new FileReader(f);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        int i = 0;

        while (i > -1) {
            i = fr.read();
            out.write(i);
        }

        fr.close();

        String template = out.toString();
        out.close();
        return template;

    }

    private Connection getConnection() throws Exception {
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        return DriverManager.getConnection(url, userName, password);
    }

    private void closeConnection(Connection conn) throws Exception {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void checkClob() throws Exception {

        Connection conn = null;

        try {

            conn = getConnection();
            Statement stmt = conn.createStatement();

            String sql =
                "SELECT ft.template_html FROM form_templates_t ft,form_grant_matrix_t fg "
                    + "WHERE ft.id=fg.ftm_id AND fg.form_role_code='"
                    + group
                    + "' AND "
                    + "fg.appl_type_code='"
                    + type
                    + "' AND fg.activity_code='"
                    + mech
                    + "'";
            ;

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                java.sql.Clob clob = (java.sql.Clob) rs.getObject(1);

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                Reader reader = clob.getCharacterStream();
                int i = 0;

                while (i > -1) {
                    i = reader.read();
                    out.write(i);
                }

                reader.close();

                String template = out.toString();
                out.close();
                File f = new File(System.getProperty("root") + "/checkfile.vm");

                FileOutputStream fos = new FileOutputStream(f);
                fos.write(template.getBytes());
                fos.flush();
                fos.close();
                System.out.println("CHECK FILE WRITTEN " + f.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }

}
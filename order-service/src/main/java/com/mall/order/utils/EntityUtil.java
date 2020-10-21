package com.mall.order.utils;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * 自动生成实体类
 */
public class EntityUtil {

//	private static final Logger logger = Logger.getLogger(EntityUtil.class);


    /**
     * 使用注意： moduleName 数据库实例名 package_path 代码目录结构
     */
    public static void main(String[] args) {
        try {
            // 创建目录
            Runtime.getRuntime().exec("mkdir /Users/hongfei/Documents/web-workspace/" + EntityUtil.moduleName);

            // 自动生成代码
            new EntityUtil().generate();

            // 自动打开生成文件的目录
            Runtime.getRuntime().exec("open /Users/hongfei/Documents/web-workspace/" + EntityUtil.moduleName);
        } catch (ClassNotFoundException e) {
//			logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (SQLException e) {
//			logger.error(e.getMessage(), e);
            e.printStackTrace();
        } catch (IOException e) {
//			logger.error(e.getMessage(), e);
        }
    }

    /**
     * ********************************* 使用前必读*******************
     * *
     * * 使用前请将moduleName更改为自己模块的名称即可（一般情况下与数据库名一致），其他无须改动。
     * *
     * **********************************************************
     */
    private String type_char = "char";

    private String type_date = "date";

    private String type_timestamp = "timestamp";

    private String type_int = "int";

    private String type_bigint = "bigint";

    private String type_text = "text";

    private String type_bit = "bit";

    private String type_decimal = "decimal";

    private String type_blob = "blob";

    private String type_float = "float";

    private String type_double = "double";

    /**
     * MYSQL数据库实例名 对应模块名称（根据自己模块做相应调整!!!务必修改^_^）
     */
    public static String moduleName = "com.mall.order";

    private String bean_path = "/Users/hongfei/Documents/web-workspace/" + moduleName + "/entity_bean";

    private String mapper_path = "/Users/hongfei/Documents/web-workspace/" + moduleName + "/entity_mapper";

    private String xml_path = "/Users/hongfei/Documents/web-workspace/" + moduleName + "/entity_mapper/xml";

    /**
     * 代码目录结构 例如 springmvc 生成结构为: springmvc.entity.User
     */
    private String package_path = "com." + moduleName;

    private String bean_package = package_path + ".entity";

    private String mapper_package = package_path + ".mapper";

    private String driverName = "com.mysql.jdbc.Driver";

    private String user = "root";

    private String password = "1q2w3e4r";

    private String url = "jdbc:mysql://127.0.0.1:3306/mall?useUnicode=true&characterEncoding=UTF-8";

    private String tableName = null;

    private String beanName = null;

    private String mapperName = null;

    private Connection conn = null;


    private List<String> ignoreFiled = Arrays.asList("id", "create_time", "create_user_id", "create_user_name", "update_time", "update_user_id", "update_user_name");


    private void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, user, password);
    }


    /**
     * 获取所有的表
     *
     * @return
     * @throws SQLException
     */
    private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
        PreparedStatement pstate = conn.prepareStatement("show tables");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString(1);
            // if ( tableName.toLowerCase().startsWith("yy_") ) {
            tables.add(tableName);
            // }
        }
        return tables;
    }


    private void processTable(String table) {
        StringBuffer sb = new StringBuffer(table.length());
        table = table.toLowerCase();
        String[] tables = table.split("_");
        String temp = null;
        for (int i = 0; i < tables.length; i++) {
            temp = tables[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        beanName = sb.toString();
        mapperName = beanName + "Mapper";
    }


    private String processType(String type) {
        if (type.indexOf(type_char) > -1) {
            return "String";
        } else if (type.indexOf(type_bigint) > -1) {
            return "Long";
        } else if (type.indexOf(type_int) > -1) {
            return "Integer";
        } else if (type.indexOf(type_date) > -1) {
            return "java.com.mrd.biz.util.Date";
        } else if (type.indexOf(type_text) > -1) {
            return "String";
        } else if (type.indexOf(type_timestamp) > -1) {
            return "java.com.mrd.biz.util.Date";
        } else if (type.indexOf(type_bit) > -1) {
            return "Boolean";
        } else if (type.indexOf(type_decimal) > -1) {
            return "java.math.BigDecimal";
        } else if (type.indexOf(type_blob) > -1) {
            return "byte[]";
        } else if (type.indexOf(type_float) > -1) {
            return "Float";
        } else if (type.indexOf(type_double) > -1) {
            return "Double";
        }
        return null;
    }


    private String processField(String field) {
        StringBuffer sb = new StringBuffer(field.length());
        // field = field.toLowerCase();
        String[] fields = field.split("_");
        String temp = null;
        sb.append(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase()).append(temp.substring(1));
        }
        return sb.toString();
    }


    /**
     * 将实体类名首字母改为小写
     *
     * @param beanName
     * @return String
     */
    private String processResultMapId(String beanName) {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }


    /**
     * 构建类上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException
     */
    private BufferedWriter buildClassComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" */");
        return bw;
    }


    /**
     * 构建方法上面的注释
     *
     * @param bw
     * @param text
     * @return
     * @throws IOException
     */
    private BufferedWriter buildMethodComment(BufferedWriter bw, String text) throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + text);
        bw.newLine();
        bw.write("\t */");
        return bw;
    }


    /**
     * 生成实体类
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
    private void buildEntityBean(List<String> columns, List<String> types, List<String> comments, String tableComment) throws IOException {
        File folder = new File(bean_path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File beanFile = new File(bean_path, beanName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + bean_package + ";");
        bw.newLine();
        bw.write("import com.mrd.common.base.BaseModel;");
        bw.newLine();
        bw.write("import io.swagger.annotations.ApiModel;");
        bw.newLine();
        bw.write("import io.swagger.annotations.ApiModelProperty;");
        bw.newLine();
        bw.write("import lombok.Getter;");
        bw.newLine();
        bw.write("import lombok.Setter;");
        bw = buildClassComment(bw, tableComment);
        bw.newLine();
        bw.write("@Setter");
        bw.newLine();
        bw.write("@Getter");
        bw.newLine();
        bw.write("@ApiModel(description = \"" + tableComment + "\")");
        bw.newLine();
        bw.write("public class " + beanName + " extends BaseModel {");
        bw.newLine();
        bw.newLine();
//		bw.write("\tprivate static final long serialVersionUID = 1L;");
//		bw.newLine();
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            if (!ignoreFiled.contains(columns.get(i))) {
                bw.newLine();
                bw.write("\t/** " + comments.get(i) + " */");
                bw.newLine();
                bw.write("\t@ApiModelProperty(value = \"" + comments.get(i) + "\")");
                bw.newLine();
                bw.write("\tprivate " + processType(types.get(i)) + " " + processField(columns.get(i)) + ";");
                bw.newLine();
            }

        }
        // 生成get 和 set方法
//		String tempField = null;
//		String _tempField = null;
//		String tempType = null;
//		for ( int i = 0 ; i < size ; i++ ) {
//			tempType = processType(types.get(i));
//			_tempField = processField(columns.get(i));
//			tempField = _tempField.substring(0, 1).toUpperCase() + _tempField.substring(1);
//			bw.newLine();
//			bw.newLine();
//			// bw.write("\tpublic void set" + tempField + "(" + tempType + " _"
//			// + _tempField + "){");
//			bw.write("\tpublic void set" + tempField + "(" + tempType + " " + _tempField + ") {");
//			bw.newLine();
//			// bw.write("\t\tthis." + _tempField + "=_" + _tempField + ";");
//			bw.write("\t\tthis." + _tempField + " = " + _tempField + ";");
//			bw.newLine();
//			bw.write("\t}");
//			bw.newLine();
//			bw.newLine();
//			bw.newLine();
//			bw.write("\tpublic " + tempType + " get" + tempField + "() {");
//			bw.newLine();
//			bw.write("\t\treturn this." + _tempField + ";");
//			bw.newLine();
//			bw.write("\t}");
//			bw.newLine();
//		}
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }


    /**
     * 构建Mapper文件
     *
     * @throws IOException
     */
    private void buildMapper() throws IOException {
        File folder = new File(mapper_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperFile = new File(mapper_path, mapperName + ".java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + mapper_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import org.springframework.stereotype.Repository;");
        bw.newLine();
        bw.newLine();
        bw.write("import com.mrd.common.base.BaseMapper;");
        bw = buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.newLine();
        bw.write("@Repository");
        bw.newLine();
        bw.write("public interface " + mapperName + "<T, Q> extends BaseMapper<T, Q> {");
        bw.newLine();
        bw.newLine();
        // ----------定义Mapper中的方法Begin----------
		/*bw = buildMethodComment(bw, "查询（根据主键ID查询）");
		bw.newLine();
		bw.write("\t" + beanName + " selectById( @Param(\"id\") Long id );");
		bw.newLine();
		bw.newLine();
		bw = buildMethodComment(bw, "删除（根据主键ID删除）");
		bw.newLine();
		bw.write("\t" + "int deleteById( @Param(\"id\") Long id );");
		bw.newLine();
		bw.newLine();
		bw = buildMethodComment(bw, "添加");
		bw.newLine();
		bw.write("\t" + "int insert( " + beanName + " record );");
		bw.newLine();
		bw.newLine();
		bw = buildMethodComment(bw, "添加 （匹配有值的字段）");
		bw.newLine();
		bw.write("\t" + "int insertSelective( " + beanName + " record );");
		bw.newLine();
		bw.newLine();
		bw = buildMethodComment(bw, "修改 （匹配有值的字段）");
		bw.newLine();
		bw.write("\t" + "int updateByIdSelective( " + beanName + " record );");
		bw.newLine();
		bw.newLine();
		bw = buildMethodComment(bw, "修改（根据主键ID修改）");
		bw.newLine();
		bw.write("\t" + "int updateById( " + beanName + " record );");
		bw.newLine();*/

        // ----------定义Mapper中的方法End----------
        bw.newLine();
        bw.write("}");
        bw.flush();
        bw.close();
    }


    /**
     * 构建实体类映射XML文件
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
    private void buildMapperXml(List<String> columns, List<String> types, List<String> comments) throws IOException {
        File folder = new File(xml_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperXmlFile = new File(xml_path, mapperName + ".xml");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        bw.newLine();
        bw.write("<mapper namespace=\"" + mapper_package + "." + mapperName + "\">");
        bw.newLine();
        bw.newLine();

        /*
         * bw.write("\t<!--实体映射-->"); bw.newLine(); bw.write(
         * "\t<resultMap id=\"" + this.processResultMapId(beanName) +
         * "ResultMap\" type=\"" + beanName + "\">"); bw.newLine();
         * bw.write("\t\t<!--" + comments.get(0) + "-->"); bw.newLine();
         * bw.write("\t\t<id property=\"" + this.processField(columns.get(0)) +
         * "\" column=\"" + columns.get(0) + "\" />"); bw.newLine(); int size =
         * columns.size(); for ( int i = 1 ; i < size ; i++ ) {
         * bw.write("\t\t<!--" + comments.get(i) + "-->"); bw.newLine();
         * bw.write("\t\t<result property=\"" +
         * this.processField(columns.get(i)) + "\" column=\"" + columns.get(i) +
         * "\" />"); bw.newLine(); } bw.write("\t</resultMap>");
         *
         * bw.newLine(); bw.newLine(); bw.newLine();
         */

        // 下面开始写SqlMapper中的方法
        // this.outputSqlMapperMethod(bw, columns, types);
        buildSQL(bw, columns, types);

        bw.write("</mapper>");
        bw.flush();
        bw.close();
    }


    private void buildSQL(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
        int size = columns.size();
        // 通用结果列
        bw.write("\t<!-- 通用查询结果列-->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Column_List\">");
        bw.newLine();

        bw.write("\t\t ");
        for (int i = 0; i < size; i++) {
            bw.write(" " + columns.get(i));
            if (i != size - 1) {
                bw.write(",");
            }
        }

        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();

        // 通用条件
        bw.write("\t<!-- 通用条件-->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Condition\">");
        bw.newLine();
        bw.write("\t\t<where>");
        bw.newLine();

        String _field = null;
        for (int i = 0; i < size; i++) {
            String _tempType = processType(types.get(i));
            // 判断如果字段类型是string则在生成xml语句中追加一个字符串非空判断
            _field = processField(columns.get(i));
            if (null != _tempType && _tempType.equalsIgnoreCase("String")) {
                bw.write("\t\t\t<if test=\"null != " + _field + " and '' != " + _field + "\">");
            } else {
                bw.write("\t\t\t<if test=\"null != " + _field + "\">");
            }

            bw.newLine();
            bw.write("\t\t\t\tAND " + columns.get(i) + " = #{" + processField(columns.get(i)) + "}");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.newLine();
        bw.write("\t\t</where>");
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();

        // 查询列表
        bw.write("\t<!-- 查询 -->");
        bw.newLine();
        bw.write("\t<select id=\"selectList\" resultType=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM " + tableName);
        bw.newLine();
        bw.write("\t\t\t <include refid=\"Base_Condition\" />");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询列表

		/*bw.write("\t<!-- 查询总数 -->");
		bw.newLine();
		bw.write("\t<select id=\"selectCount\" resultType=\"java.lang.Integer\">");
		bw.newLine();
		bw.write("\t\t SELECT");
		bw.newLine();
		bw.write("\t\t\t COUNT(1)");
		bw.newLine();
		bw.write("\t\t FROM " + tableName);
		bw.newLine();
		bw.write("\t\t\t <include refid=\"Base_Condition\" />");
		bw.newLine();
		bw.write("\t</select>");
		bw.newLine();
		bw.newLine();*/

        // 删除（根据主键ID删除）
        bw.write("\t<!--删除 -->");
        bw.newLine();
        bw.write("\t<delete id=\"delete\" parameterType=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t DELETE FROM " + tableName);
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完

        // 添加insert方法
		/*bw.write("\t<!-- 添加 -->");
		bw.newLine();
		bw.write("\t<insert id=\"insert\" parameterType=\"" + processResultMapId(beanName) + "\">");
		bw.newLine();
		bw.write("\t\t INSERT INTO " + tableName);
		bw.newLine();
		bw.write(" \t\t\t(");
		for (int i = 0; i < size; i++) {
			bw.write(columns.get(i));
			if (i != size - 1) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.newLine();
		bw.write("\t\t VALUES ");
		bw.newLine();
		bw.write(" \t\t\t(");
		for (int i = 0; i < size; i++) {
			bw.write("#{" + processField(columns.get(i)) + "}");
			if (i != size - 1) {
				bw.write(",");
			}
		}
		bw.write(") ");
		bw.newLine();
		bw.write("\t</insert>");
		bw.newLine();
		bw.newLine();*/
        // 添加insert完

        // --------------- insert方法（匹配有值的字段）
        bw.write("\t<!-- 添加 （匹配有值的字段）-->");
        bw.newLine();
        bw.write("\t<insert id=\"insert\" parameterType=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t INSERT INTO " + tableName);
        bw.newLine();
        bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        String tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + ",");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.newLine();
        bw.write("\t\t </trim>");
        bw.newLine();

        bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t #{" + tempField + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.write("\t\t </trim>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // --------------- 完毕

        // 修改update方法
        bw.write("\t<!-- 修 改（匹配有值的字段）-->");
        bw.newLine();
        bw.write("\t<update id=\"update\" parameterType=\"" + processResultMapId(beanName) + "\">");
        bw.newLine();
        bw.write("\t\t UPDATE " + tableName);
        bw.newLine();
        bw.write(" \t\t <set> ");
        bw.newLine();

        tempField = null;
        for (int i = 1; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + " = #{" + tempField + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.newLine();
        bw.write(" \t\t </set>");
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
        // update方法完毕

        // ----- 修改（匹配有值的字段）
		/*bw.write("\t<!-- 修 改-->");
		bw.newLine();
		bw.write("\t<update id=\"updateById\" parameterType=\"" + processResultMapId(beanName) + "\">");
		bw.newLine();
		bw.write("\t\t UPDATE " + tableName);
		bw.newLine();
		bw.write("\t\t SET ");

		bw.newLine();
		tempField = null;
		for (int i = 1; i < size; i++) {
			tempField = processField(columns.get(i));
			bw.write("\t\t\t " + columns.get(i) + " = #{" + tempField + "}");
			if (i != size - 1) {
				bw.write(",");
			}
			bw.newLine();
		}

		bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
		bw.newLine();
		bw.write("\t</update>");
		bw.newLine();*/
        bw.newLine();
    }


    /**
     * 获取所有的数据库表注释
     *
     * @return
     * @throws SQLException
     */
    private Map<String, String> getTableComment() throws SQLException {
        Map<String, String> maps = new HashMap<String, String>();
        PreparedStatement pstate = conn.prepareStatement("show table status");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }


    public void generate() throws ClassNotFoundException, SQLException {
        init();
        String prefix = "show full fields from ";
        List<String> columns = null;
        List<String> types = null;
        List<String> comments = null;
        PreparedStatement pstate = null;
        List<String> tables = getTables();
        Map<String, String> tableComments = getTableComment();
        for (String table : tables) {
            columns = new ArrayList<String>();
            types = new ArrayList<String>();
            comments = new ArrayList<String>();
            System.out.println(prefix + table);
            pstate = conn.prepareStatement(prefix + table);
            try {
                ResultSet results = pstate.executeQuery();
                while (results.next()) {
                    columns.add(results.getString("FIELD"));
                    types.add(results.getString("TYPE"));
                    comments.add(results.getString("COMMENT"));
                }
                tableName = table;
                processTable(table);
                // this.outputBaseBean();
                String tableComment = tableComments.get(tableName);
                buildEntityBean(columns, types, comments, tableComment);
                buildMapper();
                buildMapperXml(columns, types, comments);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        conn.close();
    }
}

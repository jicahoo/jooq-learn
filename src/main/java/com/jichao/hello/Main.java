package com.jichao.hello;

import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static generated.jooq.Tables.AUTHOR;


public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("ee");

        String userName = "postgres";
        String password = "871207";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by jOOQ, internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            DSLContext create = DSL.using(conn, SQLDialect.POSTGRES);
            Result<Record> result = create.select().from(AUTHOR).fetch();
            if (result.isEmpty()) {
                System.out.println("Empty!");
            }

            for (Record r : result) {
                Integer id = r.getValue(AUTHOR.ID);
                String firstName = r.getValue(AUTHOR.FIRST_NAME);
                String lastName = r.getValue(AUTHOR.LAST_NAME);

                System.out.println("ID: " + id + " first name: " + firstName + " last name: " + lastName);
            }

            //ORM
            List<Library> libraries = create.select().from(AUTHOR).fetch().into(Library.class);
            libraries.forEach(System.out::println);

            create.select().from(AUTHOR).where(AUTHOR.FIRST_NAME.startsWith("ji"));
            result.forEach(r -> System.out.println(r.getValue(AUTHOR.LAST_NAME)));
            create.select().from(AUTHOR).where(DSL.concat(AUTHOR.LAST_NAME, "_a").eq("jichao_a"));
            result.forEach(r -> System.out.println(r.getValue(AUTHOR.LAST_NAME)));

            //New property
            Result<Record2<String, String>> x = create.select(AUTHOR.LAST_NAME, DSL.concat(AUTHOR.LAST_NAME, "_xxx").as("new_prop"))
                    .from(AUTHOR).where(AUTHOR.FIRST_NAME.startsWith("ji")).fetch();
            x.forEach(r -> System.out.println(r.get("new_prop")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

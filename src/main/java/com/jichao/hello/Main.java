package com.jichao.hello;

import generated.jooq.tables.records.AuthorRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static generated.jooq.Tables.AUTHOR;


public class Main {

    public static void createAuthor(DSLContext dslContext) {
        AuthorRecord author = dslContext.newRecord(AUTHOR);
        author.from(new Library(1,"lebron", "james", 100, 20, new Date()));
        author.store();
    }

    public static void getAllAuhtors(DSLContext dslContext) {
        List<Library> libraries = dslContext.select().from(AUTHOR).fetch().into(Library.class);
        libraries.forEach(System.out::println);
    }

    public static void getAuthorById(DSLContext dslContext, Integer id) {
        dslContext.select().from(AUTHOR).where(AUTHOR.ID.eq(1)).fetch().into(Library.class).forEach(
                System.out::println
        );
    }

    public static void expressionInSelect(DSLContext dslContext) {

        Result<Record2<String, String>> x = dslContext.select(AUTHOR.LAST_NAME, DSL.concat(AUTHOR.LAST_NAME, "_xxx").as("new_prop"))
                .from(AUTHOR).fetch();
        x.forEach(r -> System.out.println(r.get("new_prop")));

        List<SelectField<String>> stringFields = new ArrayList<>();
        stringFields.add(AUTHOR.LAST_NAME);
        int layerConcat = 5;
        Field<String> lastName = DSL.concat(AUTHOR.LAST_NAME);
        for(int i = 0; i < layerConcat; i++) {
            lastName = DSL.concat(lastName, "_suffix");
        }

        stringFields.add(lastName.as("new_prop"));
        dslContext.select(stringFields).from(AUTHOR).fetch().forEach(r -> System.out.println("Superman: " + r.get("new_prop")));
    }


    public static void filterAuthors(DSLContext dslContext) {
        dslContext.select().from(AUTHOR).where(AUTHOR.FIRST_NAME.startsWith("ja")).forEach(
                r -> System.out.println("Filter startsWith ja ---- " + r.getValue(AUTHOR.FIRST_NAME))
        );

        dslContext.select().from(AUTHOR).where(DSL.concat(AUTHOR.LAST_NAME, "_a").eq("lebron_a")).forEach(
                r -> System.out.println("Filter concat(last_name, _a) = lebron_a ---- " + r.getValue(AUTHOR.LAST_NAME))
        );
    }

    public static void deleteAllAuthors(DSLContext dslContext) {
        dslContext.delete(AUTHOR).execute();
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Fire!!!");

        String userName = "postgres";
        String password = "871207";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        // Connection is the only JDBC resource that we need
        // PreparedStatement and ResultSet are handled by jOOQ, internally
        try (Connection conn = DriverManager.getConnection(url, userName, password)) {
            DSLContext dslCtx = DSL.using(conn, SQLDialect.POSTGRES);
            deleteAllAuthors(dslCtx);
            createAuthor(dslCtx);
            getAllAuhtors(dslCtx);
            getAuthorById(dslCtx, 1);
            filterAuthors(dslCtx);
            expressionInSelect(dslCtx);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

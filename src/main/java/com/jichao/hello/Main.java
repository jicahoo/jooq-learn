package com.jichao.hello;

import generated.jooq.tables.records.AuthorRecord;
import generated.jooq.tables.records.BookRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static generated.jooq.Tables.AUTHOR;
import static generated.jooq.Tables.BOOK;


public class Main {

    public static void createAuthor(DSLContext dslContext) {
        AuthorRecord author = dslContext.newRecord(AUTHOR);
        Author author1 = new Author(1, "lebron", "james", 100, 20, new Date());
        author.from(author1);
        author.store();
        author = dslContext.newRecord(AUTHOR);
        Author author2 = new Author(2, "bryant", "kobe", 100, 20, new Date());
        author.from(author2);
        author.store();
        BookRecord book1 = dslContext.newRecord(BOOK);
        book1.from(new Book(1, "hello", 100, new Date(), 1));
        book1.store();
        BookRecord book2 = dslContext.newRecord(BOOK);
        book2.from(new Book(2, "world", 100, new Date(), 1));
        book2.store();
    }

    public static void getAllAuhtors(DSLContext dslContext) {
        List<Author> libraries = dslContext.select().from(AUTHOR).fetch().into(Author.class);
        libraries.forEach(System.out::println);
    }

    public static void getAuthorById(DSLContext dslContext, Integer id) {
        dslContext.select().from(AUTHOR).where(AUTHOR.ID.eq(1)).fetch().into(Author.class).forEach(
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
        for (int i = 0; i < layerConcat; i++) {
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
        dslContext.delete(BOOK).execute();
    }

    public static Select<Record> returnJooqQuery(DSLContext dslContext, TableLike<?> rootTable, boolean hasWhere, String col, String operator, String operand) {
        if (hasWhere) {
            Condition condition = ((Field<String>) rootTable.field(col)).startsWith(operand);
            return dslContext.select().from(rootTable).where(condition);
        } else {
            return dslContext.select().from(rootTable);
        }
    }

    public static void callGenericJooq(DSLContext dslContext) {
        Select<Record> select = returnJooqQuery(dslContext, AUTHOR, true, "last_name", "startsWith", "b");
        select.fetch().forEach(x -> System.out.println("Got output from generice jooq:" + x));
    }

    public static void getJoin(DSLContext dslContext) {
        System.out.println("Enter getJoin -- ");

        dslContext.select().from(BOOK).fetch().forEach(
                r -> System.out.println(r)
        );
        SelectOnConditionStep<Record> selectSetp = dslContext.select().from(AUTHOR).leftJoin(BOOK).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID));
        System.out.println(selectSetp.getSQL());
        Result<Record> joinResult = selectSetp.fetch();
        System.out.println("Size of join results: " + joinResult.size());
        joinResult.forEach(
                r -> System.out.println(r)
        );

        System.out.println("Exit getJoib -- ");
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
            callGenericJooq(dslCtx);
            getJoin(dslCtx);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

package com.mohamedgamal.wuzzuf_ml;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.util.List;
import static org.apache.spark.sql.functions.col;

@Component
public class JobsDAO {
    public static Dataset<Row> rows;
    public static SparkSession ss;

    @Bean
    public Dataset<Row> createDataset() {
        ss = SparkSession.builder()
                .appName("SpringBootMachineLearning")
                .config("spark.eventLog.enabled", "false")
                .master("local[4]")
                .getOrCreate();
        ss.sparkContext().setLogLevel("ERROR");
        rows = ss.read().option("header", true).csv("Jobs.csv");
        rows = rows.select(
                col("Title").cast(DataTypes.StringType),
                col("Company").cast(DataTypes.StringType),
                col("Location").cast(DataTypes.StringType),
                col("Type").cast(DataTypes.StringType),
                col("Level").cast(DataTypes.StringType),
                col("YearsExp").cast(DataTypes.StringType),
                col("Country").cast(DataTypes.StringType),
                col("Skills").cast(DataTypes.StringType));
        rows.createOrReplaceTempView("wuzzuf_raw");
        return ss.sql("SELECT CAST(Title as STRING) Title, CAST(Company as string) Company, CAST(Location as string) Location, "
                + "CAST(Type as string) Type, CAST(Level as string) Light, CAST(YearsExp as string) YearsExp, "
                + "CAST(Country as string) Country, CAST(Skills as string) Skills FROM wuzzuf_raw");
    }

    public void printInfo(){
        // Print in Console Schema and Info:
        rows.printSchema();
        System.out.println("Total count: " + rows.count());
        Dataset<Row> describe = rows.describe();
        describe.show();
    }

    public static List<String> getFullSchema() {
        return JobsDAO.rows.toJSON().collectAsList();
    }

    public static long getCount(){
        return JobsDAO.rows.count();
    }

    public static List<String> getSchema(){
        return JobsDAO.rows.describe().toJSON().collectAsList();
    }
}
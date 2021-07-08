package com.mohamedgamal.wuzzuf_ml;

import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.evaluation.ClusteringEvaluator;
import org.apache.spark.ml.feature.OneHotEncoder;
import org.apache.spark.ml.feature.OneHotEncoderModel;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.Serializable;
import java.util.ArrayList;

public class SparkMachineLearning implements Serializable {
    int count;
    private static transient PieChart chart;
    public static ArrayList<Double> averageFactorization = new ArrayList<>();
    private Double distance;
    private Double distanceTitle;
    ClusteringEvaluator companyEvaluator = new ClusteringEvaluator();
    ClusteringEvaluator titleEvaluator = new ClusteringEvaluator();

    public PieChart getPieChart(Dataset<Row> machineLearning){
        machineLearning.createOrReplaceTempView("The_most_demanding_companies_for_jobs");
        Dataset<Row> machine = JobsDAO.ss.sql("SELECT Company ,COUNT(Title) x FROM The_most_demanding_companies_for_jobs group by Company " +
                "order by x Desc");
        chart = new PieChartBuilder().width(2000).height(600).title("The Most Demanding Job Title").build();
        machine.foreach((ForeachFunction<Row>)
                row -> {
                    if (row.getLong(1)>20) chart.addSeries(row.getString(0), row.getLong(1));
                    else   count ++;
                });
        chart.addSeries("others",count);
        // Display the Bar Chart in the editor:
//        new SwingWrapper<>(chart).displayChart();
        return chart;
    }

    public CategoryChart getPopularJobTitlesBarChart(Dataset<Row> machineLearning){
        machineLearning.createOrReplaceTempView("The_most_demanding_companies_for_jobs");
        Dataset<Row> machine = JobsDAO.ss.sql("SELECT Title ,COUNT(Title) x FROM The_most_demanding_companies_for_jobs group by Title order by x Desc limit 10");
        CategoryChart chart = new CategoryChartBuilder().width(1000).height(1000).title("The Most Popular Job Titles").xAxisTitle("Job Title").yAxisTitle("count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.addSeries("The Most Popular Job Titles", machine.select("Title").as(Encoders.STRING()).collectAsList(),machine.select("x").as(Encoders.LONG()).collectAsList());
        // Display the Bar Chart in the editor:
//        new SwingWrapper<>(chart).displayChart();
        return chart;
    }

    public CategoryChart getPopularAreasBarChart(Dataset<Row> machineLearning){
        machineLearning.createOrReplaceTempView("the_most_popular_areas");
        Dataset<Row> machine = JobsDAO.ss.sql("SELECT Location ,COUNT(Location) x FROM the_most_popular_areas group by Location order by x Desc limit 10");
        CategoryChart chart = new CategoryChartBuilder().width(1000).height(1000).title("The Most Popular Areas").xAxisTitle("Location").yAxisTitle("count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.addSeries("The Most Popular Areas", machine.select("Location").as(Encoders.STRING()).collectAsList(),machine.select("x").as(Encoders.LONG()).collectAsList());
        // Display the Bar Chart in the editor:
//        new SwingWrapper<>(chart).displayChart();
        return chart;
    }

    public CategoryChart getMostPopularSkillsBarChart(Dataset<Row> machineLearning){
        machineLearning.createOrReplaceTempView(" the_most_important_skills_required");
        Dataset<Row> machine = JobsDAO.ss.sql("SELECT explode(split(Skills,',')) as exploded_skills  FROM the_most_important_skills_required ");
        machine.show();
        machine.createOrReplaceTempView("exploded_skills");
        Dataset<Row> exploded_skills = JobsDAO.ss.sql("SELECT exploded_Skills ,COUNT(exploded_Skills) x FROM exploded_skills group by exploded_Skills order by x Desc limit 10");
        CategoryChart chart = new CategoryChartBuilder().width(1000).height(1000).title("The Most Popular Skills Required").xAxisTitle("Skills").yAxisTitle("count").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setHasAnnotations(true);
        chart.addSeries("The Most Popular Skills Required", exploded_skills.select("exploded_Skills").as(Encoders.STRING()).collectAsList(),exploded_skills.select("x").as(Encoders.LONG()).collectAsList());
        // Display the Bar Chart in the editor:
//        new SwingWrapper<>(chart).displayChart();
        return chart;
    }

    public KMeans returnKMeans(Dataset<Row> machineLearning){
        machineLearning.createOrReplaceTempView ("wuzzuf_raw");
        final Dataset<Row> title = JobsDAO.ss.sql ("SELECT CAST(Title as String)Title FROM wuzzuf_raw");
        final Dataset<Row> company = JobsDAO.ss.sql ("SELECT CAST(Company as String)Company  FROM wuzzuf_raw");
        StringIndexer indexer = new StringIndexer()
                .setInputCol("Company")
                .setOutputCol("CompanyIndex");
        // Indexer and string indexer used to convert nominal data to numerical data
        Dataset<Row> indexed_company = indexer.fit(company).transform(company);
        indexed_company.show();
        StringIndexer indexer1 = new StringIndexer()
                .setInputCol("Title")
                .setOutputCol("TitleIndex");
        // Indexer and string indexer used to convert nominal data to numerical data
        Dataset<Row> indexed_title = indexer1.fit(title).transform(title);
        indexed_title.show();
        OneHotEncoder encoder = new OneHotEncoder()
                .setInputCols(new String[] {"CompanyIndex"})
                .setOutputCols(new String[] {"companyOneHotE"});
        OneHotEncoderModel oneHotEncoderModel = encoder.fit(indexed_company);
        Dataset<Row> companyOnHot = oneHotEncoderModel.transform(indexed_company);
        companyOnHot.show();
        OneHotEncoder Title_encoder = new OneHotEncoder()
                .setInputCols(new String[] {"TitleIndex"})
                .setOutputCols(new String[] {"TitleOneHotE"});
        OneHotEncoderModel modelTitle = Title_encoder.fit(indexed_title);
        Dataset<Row> titleOnHot = modelTitle.transform(indexed_title);
        companyOnHot.show();
        final VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(new String[] { "companyOneHotE"})
                .setOutputCol("features");
        final Dataset<Row> companyFeatures = vectorAssembler.transform(companyOnHot.na ().drop ());
        companyFeatures.show();
        final VectorAssembler vectorAssembler1 = new VectorAssembler()
                .setInputCols(new String[] {"TitleOneHotE"})
                .setOutputCol("Title Features");
        final Dataset<Row> featureForTitle = vectorAssembler1.transform(titleOnHot.na ().drop ());
        Dataset<Row>[] splits = companyFeatures.randomSplit(new double[] { 0.8, 0.2 },42);
        Dataset<Row> CompanyTrainingData = splits[0];
        Dataset<Row> CompanyTestData = splits[1];
        // This is for job title clustering
        Dataset<Row>[] splits1 = featureForTitle.randomSplit(new double[] { 0.8, 0.2 },42);
        Dataset<Row> trainingDataTitle = splits[0];
        Dataset<Row> testDataTitle = splits[1];
        companyFeatures.printSchema ();
        /////////////////////////  K Means for A Company   //////////////////////////
        // Trains a k-means model
        KMeans kmeans = new KMeans().setK(800).setSeed(1L);
        KMeansModel CompanyModel = kmeans.fit(CompanyTrainingData);
        //// Make predictions
        Dataset<Row> predictions = CompanyModel.transform(CompanyTestData);
        //// Evaluate clustering by computing Silhouette score
        distance = companyEvaluator.evaluate(predictions);
        // Print Company K-Means
//        System.out.println("Distance Measure: " + companyEvaluator.getDistanceMeasure());
//        System.out.println(distance);
        /////////////////////////  k means for title   //////////////////////////
        KMeans kMeansTitle = new KMeans().setK(600).setSeed(1L);
        // Fit is used on the training data to learn the scaling parameters of that data. Here,
        // The model built by us will learn the mean and variance of the features of the training set
        KMeansModel mode_title = kMeansTitle.fit(trainingDataTitle);
        // Using the transform method we can use the same mean and variance as it is calculated
        // From our training data to transform our test data.
        // Thus, the parameters learned by our model using the training data will help us to transform our test data.
        Dataset<Row> predictions_title = mode_title.transform(testDataTitle);
        // ClusteringEvaluator evaluator_title = new ClusteringEvaluator();
        distanceTitle = titleEvaluator.evaluate(predictions_title);
        // Print K-Means
//        System.out.println("Distance Measure: " + kMeansTitle.getDistanceMeasure());
//        System.out.println(distanceTitle);
        return kMeansTitle;
    }

    public Dataset<Row> calculateFactorization(Dataset<Row> machineLearning){
        machineLearning.createOrReplaceTempView ("Factorize_YearsExp");
        Dataset<Row> yearsExp = JobsDAO.ss.sql ("SELECT YearsExp FROM Factorize_YearsExp");
        yearsExp.foreach((ForeachFunction<Row>)
                row -> {
                    String x = row.toString().replaceAll("\\[", "").replaceAll("\\]","");
                    if(x.contains("-")){
                        String[] words=x.split("-");
                        Double min = Double.valueOf(words[0]);
                        String temp = String.valueOf(words[1].charAt(0)) + String.valueOf(words[1].charAt(1));
                        temp = temp.trim();
                        Double max = Double.valueOf(temp);
                        Double number = (min+max)/2;
                        averageFactorization.add(number);
                    }else if(x.contains("+")){
                        String[] words=x.split("\\+");
                        Double min = Double.valueOf(words[0]);
                        Double max = min+5;
                        Double number = (min+max)/2;
                        averageFactorization.add(number);
                    }else{
                        averageFactorization.add(0.0);
                    }
                }
        );
        return yearsExp;
    }

    public ArrayList<Double> getAverageFactorization() {
        return averageFactorization;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getDistanceTitle() {
        return distanceTitle;
    }

    public ClusteringEvaluator getCompanyEvaluator() {
        return companyEvaluator;
    }
}
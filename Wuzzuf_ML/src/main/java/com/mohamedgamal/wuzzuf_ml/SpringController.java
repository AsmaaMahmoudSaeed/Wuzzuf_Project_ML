package com.mohamedgamal.wuzzuf_ml;

import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.knowm.xchart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class SpringController {

    @Autowired
    Dataset<Row> machineLearning;
    SparkMachineLearning sparkMachineLearning = new SparkMachineLearning();

    @RequestMapping(value="/")
    public String openStream(Model model) {
        model.addAttribute("imageName", "/images/spark_image.jpg");
        return "home";
    }

    @RequestMapping(value="/Summary")
    public String statistics(Model model) {
        model.addAttribute("fullSchema", JobsDAO.getFullSchema());
        model.addAttribute("count", JobsDAO.getCount());
        model.addAttribute("schemaTable", JobsDAO.getSchema());
        return "summary";
    }

    @RequestMapping("/pieChart")
    public String pieChart(Model model) {
        PieChart chart = sparkMachineLearning.getPieChart(machineLearning);
        String imageNameAndPath = "/images/pie_chart.png";
        model.addAttribute("imageName", imageNameAndPath);
        try{
            BitmapEncoder.saveBitmap(chart,"./src/main/resources/static"+imageNameAndPath,BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "pie_chart";
    }

    @RequestMapping ("/barChart1")
    public String barChart(Model model){
        CategoryChart chart = sparkMachineLearning.getPopularJobTitlesBarChart(machineLearning);
        String imageNameAndPath = "/images/most_popular_job_titles.png";
        model.addAttribute("imageName", imageNameAndPath);
        try{
            BitmapEncoder.saveBitmap(chart,"./src/main/resources/static"+imageNameAndPath,BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "charts";
    }

    @RequestMapping(value="/barChart2")
    public String barChartStream(Model model){
        CategoryChart chart = sparkMachineLearning.getPopularAreasBarChart(machineLearning);
        String imageNameAndPath = "/images/areas_BarChart.png";
        model.addAttribute("imageName", imageNameAndPath);
        try{
            BitmapEncoder.saveBitmap(chart,"./src/main/resources/static"+imageNameAndPath,BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "charts";
    }

    @RequestMapping("/barChart3")
    public String barchart2(Model model){
        CategoryChart chart = sparkMachineLearning.getMostPopularSkillsBarChart(machineLearning);
        String imageNameAndPath = "/images/the_most_popular_skills_required.png";
        model.addAttribute("imageName", imageNameAndPath);
        try{
            BitmapEncoder.saveBitmap(chart,"./src/main/resources/static"+imageNameAndPath,BitmapEncoder.BitmapFormat.PNG);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "charts";
    }

    @RequestMapping("/kMeans")
    public String kMeans(Model model){
        KMeans kMeans = sparkMachineLearning.returnKMeans(machineLearning);
        model.addAttribute("imageName", "/images/k-Means.jpg");
        model.addAttribute("companyKMeans", sparkMachineLearning.getCompanyEvaluator().getDistanceMeasure());
        model.addAttribute("companyKMeansAccuracy", sparkMachineLearning.getDistance());
        model.addAttribute("kMeansTitle", kMeans.getDistanceMeasure());
        model.addAttribute("kMeansAccuracyTitle", sparkMachineLearning.getDistanceTitle());
        return "kmeans";
    }

    @RequestMapping("/factorization")
    public String factorization(Model model){
        ArrayList<Double> averageFactorizationColumn = sparkMachineLearning.getAverageFactorization();
        Dataset<Row> yearsExp = sparkMachineLearning.calculateFactorization(machineLearning);
        model.addAttribute("indexed", averageFactorizationColumn);
        model.addAttribute("yearsExp", yearsExp.toJSON().collectAsList());
        return "factorization";
    }
}

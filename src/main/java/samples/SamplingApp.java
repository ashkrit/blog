package samples;

import java.io.BufferedReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.lang.System.out;


public class SamplingApp {

    public static final int HUNDRED_PERCENTAGE = 100;

    public static void main(String...args)  throws Exception {

        String fileName = "/graduates-from-university-first-degree-courses-by-type-of-course.csv";

        int noOfRecords = recordCount(readFile(fileName));
        float samplePercentage = 0.02f;
        int sampleRecordCount =(int) (noOfRecords * samplePercentage);

        out.println(String.format("Sample record count %s" , sampleRecordCount));

        List<Student> details = selectSample(readFile(fileName), noOfRecords , sampleRecordCount);

        int percentage = (int) (samplePercentage * HUNDRED_PERCENTAGE);

        out.println(String.format("By Year %s at %s%%" ,
                (filterByYear(details,1993) /percentage)  * HUNDRED_PERCENTAGE , percentage) );

        out.println(String.format("By Course %s at %s%%" ,
                (filterByCourse(details,"Law") /percentage)  * HUNDRED_PERCENTAGE , percentage));

        out.println(String.format("By Year and Male %s at %s%%" ,
                (filterByYearAndGender(details,1993,"Males")/percentage) * HUNDRED_PERCENTAGE , percentage));

    }

    private static int filterByCourse(List<Student> details, String course) {
        return countRecord(details, row -> row.course.equals(course));
    }

    private static int filterByYearAndGender(List<Student> details, int year,String gender) {
        return countRecord(details, row -> row.year == year && row.sex.equals(gender));
    }

    private static int filterByYear(List<Student> details, int year) {
        return countRecord(details,row -> row.year == year);

    }

    private static int countRecord(List<Student> details, Predicate<Student> studentPredicate) {
        return details.stream().filter(studentPredicate)
                // .map(r ->  { System.out.println(r); return r; })
                .map(row -> row.count)
                .reduce(0,(acc,v) -> acc+v, (v1,v2) -> v1+v2);
    }




    static int calculateSampleRecordCount(int total, int sampleSize, int currentGroupCount)
    {
        float ratio = (currentGroupCount*1f)/(total*1f);
        return Math.round(sampleSize * ratio);
    }
    private static List<Student> selectSample(BufferedReader reader, int noOfRecords, int sampleRecordCount) {

        return reader.lines().skip(1)
                .map( line -> line.split(Pattern.quote(",")))
                .map( row -> parse(row))
                .map( row -> new Student(Integer.parseInt(row[0]),row[1],row[2],Integer.parseInt(row[3])))
                .map( row ->  {
                    row.count = calculateSampleRecordCount(noOfRecords,sampleRecordCount,row.count);
                    return row;
                }).collect(Collectors.toList());
    }

    private static BufferedReader readFile(String fileName) throws Exception {
        URL studentCourse = SamplingApp.class.getResource(fileName);
        return Files.newBufferedReader(Paths.get(studentCourse.toURI()));
    }

    private static int recordCount(BufferedReader reader) {
        int noOfRecords = reader.lines()
                .map( line -> line.split(Pattern.quote(",")))
                .map( row -> parse(row))
                .map( row -> Integer.parseInt(row[3]))
                .reduce(0 , (acc,recordCount) -> acc + recordCount );
        out.println(String.format("No of records %s" , noOfRecords));
        return noOfRecords;
    }

    private static String[] parse(String[] row) {
        int recordCountField = 3;
        row[recordCountField] = toInt(row[recordCountField]);
        return row;
    }

    private static String toInt(String value) {
        try {
            return valueOf(parseInt(value));
        }
        catch (Exception e) {
            return "0";
        }
    }


    static class Student {
        int year;
        String sex;
        String course;
        int count;

        public Student(int year,String sex,String course,int count) {
            this.year = year;
            this.sex = sex;
            this.count = count;
            this.course = course;
        }

        @Override
        public String toString() {
            return String.format("%s,%s,%s,%s",year,sex,course,count);
        }
    }

}

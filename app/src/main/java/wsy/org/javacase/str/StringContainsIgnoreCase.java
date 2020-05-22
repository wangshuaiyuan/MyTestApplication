package wsy.org.javacase.str;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wsy on 23/10/2018
 */
public class StringContainsIgnoreCase {
    //测试代码
    public static void main(String args[]) {


//        Pattern pattern = Pattern.compile("pa", Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher("PD1000");
//        if (matcher.find())
//            System.out.println("yeah......");
//        else
//            System.out.println("oh no!!!");
//
//
//
//        String a = "pd100";
//        System.out.print(a.substring(0,2));

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add("woshizifuzhuan" + i);
        }


        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String x = it.next();
            if (x.equals("del")) {
                it.remove();
            }
        }

        String param = "a|b|c";
        String[] params =  param.split("|");
        for(String paramstr : params){
            System.out.println(paramstr);
        }


    }

}

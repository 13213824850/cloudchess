package com.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 4:00 2019/3/12 0012
 * @Modifide by:
 */

public class test {
    @Test
    public void testStream(){
        List<st> list = new ArrayList<>();
        list.add(new st("1",1));
        list.add(new st("2",2));
        String collect = list.stream().map(s -> s.getId()).collect(Collectors.joining("|"));
        System.out.println(collect);


    }

}
@Data
@NoArgsConstructor
@AllArgsConstructor
class st{
    private String id;
    private int name;
}

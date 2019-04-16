package com.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        Map<String, Integer> map= new HashMap<>();
        map.put("1",1);
        map.put("3",3);
        for(String key: map.keySet()){
            System.out.println(key);
            System.out.println(map.get(key));
        }

    }

}
@Data
@NoArgsConstructor
@AllArgsConstructor
class st{
    private String id;
    private int name;
}

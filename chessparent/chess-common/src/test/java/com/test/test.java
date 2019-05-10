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
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("1","1","1"));
        books.add(new Book("2","2","2"));
        books.add(new Book("3","3","3"));
        books.add(new Book("4","4","4"));
        String category = "1";
        books.stream().forEach(book -> {
            if(book.getCategory().equals(category)){
                System.out.println(book);
            }
        });
    }

}
@Data
@NoArgsConstructor
@AllArgsConstructor
class Book{
    private String title;
    private String category;
    private String auth;
}

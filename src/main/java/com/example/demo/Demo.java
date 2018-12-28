package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: chendom
 * Date: 2018-12-26
 * Time: 17:18
 */
public class Demo {
    public static void main(String[] args) throws JsonProcessingException {
        Map<String,String> map = new HashMap<>();
        String[] arr = new String[2];
        arr[0]= "1ec45be3-7ccc-4aa4-8851-b1bdd977267d";
        arr[1] = "1EC45BE3-7CCC-4AA4-8851-B1BDD977267D";
        for (int i=0;i<arr.length;i++){
            if (!map.containsKey(arr[i])){
                map.put(arr[i],i+"");
            }else{
                System.out.println("contain"+arr[i]);
            }
            System.out.println(new ObjectMapper().writeValueAsString(map));
        }


    }
}

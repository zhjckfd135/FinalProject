package com.example.hobbyt;

import static org.junit.Assert.assertEquals;

import com.example.hobbyt.utils.UrlMap;

import org.junit.Test;

public class UrlMapTest {
    @Test
    public void get_and_put_map_url(){
        UrlMap map = new UrlMap("asds");
        map.put("a", "A");
        map.put("b", "B");
        map.put("c", "C");
        assertEquals("A", map.get("a"));
        assertEquals("B", map.get("b"));
        assertEquals("C", map.get("c"));
    }

    @Test
    public void generate_url(){
        UrlMap map = new UrlMap("https://aboba/soda");
        map.put("a", "A");
        map.put("b", "B");
        map.put("c", "C");
        assertEquals("https://aboba/soda?a=A&b=B&c=C", map.generateUrl());
    }
}

package jprobe

import groovy.json.internal.JsonFastParser

class JProbe {
    public static void main(String[] args) {
        println new JsonFastParser().parse('{"a":1}');
    }
}

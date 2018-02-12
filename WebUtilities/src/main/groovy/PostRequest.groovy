package main.groovy

// https://mvnrepository.com/artifact/org.codehaus.groovy.modules.http-builder/http-builder
@Grapes(
        @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
)

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator
import static groovyx.net.http.ContentType.*


def http = new HTTPBuilder('http://www.google.com')

http.get( path : '/search',
        contentType : TEXT,
        query : [q:'httpbuilder'] ) { HttpResponseDecorator resp, InputStreamReader reader ->

    println "response status: ${resp.statusLine}"
    println 'Headers: -----------'
    resp.headers.each { h ->
        println " ${h.name} : ${h.value}"
    }
    println 'Response data: -----'
    System.out << reader
    println '\n--------------------'
}
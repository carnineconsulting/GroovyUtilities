package main.groovy

// https://mvnrepository.com/artifact/org.codehaus.groovy.modules.http-builder/http-builder
@Grapes(
        @Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.1')
)

import groovyx.net.http.HTTPBuilder
import groovyx.net.http.HttpResponseDecorator

import javax.swing.JFrame
import javax.swing.JOptionPane

import static groovyx.net.http.ContentType.*

def prompt = {
    JFrame jframe = new JFrame()
    String answer = JOptionPane.showInputDialog(jframe, it)
    jframe.dispose()
    answer
}

def strUrl = prompt("Enter an URL to retrieve:")

def http = new HTTPBuilder(strUrl)
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
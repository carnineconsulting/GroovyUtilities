package main.groovy

import org.apache.commons.validator.routines.UrlValidator
import org.jsoup.HttpStatusException
@Grapes(
        @Grab(group='commons-validator', module='commons-validator', version='1.6')
)

//import org.codehaus.groovy.grails.validation.routines.UrlValidator
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.jsoup.Jsoup

import groovy.util.logging.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class BasicWebCrawler {
    private final boolean followExternalLinks = true
    private linksToCrawl = [] as ArrayDeque
    private visitedUrls = [] as HashSet
    private urlValidator = new UrlValidator()
    final static Pattern IGNORE_SUFFIX_PATTERN = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"+ "|png|tiff?|mid|mp2|mp3|mp4" +
            "|wav|avi|mov|mpeg|ram|m4v|pdf" +"|rm|smil|wmv|swf|wma|zip|rar|gz))\$")
    private final timeout = 3000
    private final userAgent = "Mozilla"
    private int i = 0
    private String alt,target

    def collectUrls(List seedURLs) {
        seedURLs.each {url ->
            println(url)
            linksToCrawl.add(url);
        }
        try {
            while(!linksToCrawl.isEmpty()){
                def urlToCrawl = linksToCrawl.poll() as String // "poll" removes and returns the first url in the"queue"
                try {
                    visitedUrls.add(urlToCrawl)
                    // extract URL from HTML using Jsoup
                    def doc = Jsoup.connect(urlToCrawl).userAgent(userAgent).timeout(timeout).get() as Document
                    def links = doc.select("a[href]") as Elements
                    links.each {Element link ->
                        // find absolute path
                        def absHref = link.attr("abs:href") as String
                        alt     = link.text()
                        target  = link.getElementsByAttribute("target")
                        if (shouldVisit(absHref)) {
                            // If this set already contains the element, the call leaves the set unchanged and returns false.
                            if(visitedUrls.add(absHref)){
                                if (!linksToCrawl.contains(absHref)) {
                                    linksToCrawl.add(absHref)
                                    //println("new link ${absHref} added to queue")
                                }
                            }
                        }
                    }
                } catch (HttpStatusException e) {
                    println(e.getMessage())
                } catch (SocketTimeoutException e) {
                    println(e.getMessage())
                } catch (IOException e) {
                    println(e.getMessage())
                }
            }
        } catch (Exception e){
            println(e.getMessage())
        }
    }

    private shouldVisit(String url) {
        // filter out invalid links
        boolean visitUrl = true
        File urlOutput = new File("urlOutput.txt")
        try {
            def match = IGNORE_SUFFIX_PATTERN.matcher(url) as Matcher
            def isUrlValid = urlValidator.isValid(url)
            boolean followUrl
            if(!isUrlValid){
                if (!url.startsWith('mailto:') || !url) {
                    println("Invalid Link: ${url}:${target}")
                }
            }

            if (!followExternalLinks) {
                // follow only urls which starts with any of the seed urls
                followUrl = seedURLs.any { seedUrl ->
                    if (url.startsWith(seedUrl)) {
                        return true // break
                    }
                }
            } else {
                // follow any url
                followUrl = false
            }
            visitUrl = (!match.matches() && isUrlValid && followUrl)
        } catch (Exception e) {
            // handle exception
        }

        i=i+1

        println(i + ": " + url + ": " + alt)
        urlOutput.append("${url}:${alt}\n")
        return visitUrl
    }
}
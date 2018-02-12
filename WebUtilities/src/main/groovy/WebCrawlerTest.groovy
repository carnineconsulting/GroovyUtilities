package main.groovy

def seedUrls = []
seedUrls.add("https://www.kcci.com/")
def crawler = new BasicWebCrawler()
crawler.collectUrls(seedUrls)
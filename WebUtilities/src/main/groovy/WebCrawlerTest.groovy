package main.groovy

import javax.swing.JFrame
import javax.swing.JOptionPane

def seedUrls = []

def prompt = {
    JFrame jframe = new JFrame()
    String answer = JOptionPane.showInputDialog(jframe, it)
    jframe.dispose()
    answer
}

def strUrl = prompt("Enter an URL to retrieve:")
seedUrls.add(strUrl)
def crawler = new BasicWebCrawler()
crawler.collectUrls(seedUrls)
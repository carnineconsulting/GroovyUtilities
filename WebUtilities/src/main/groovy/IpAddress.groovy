package main.groovy

import java.net.InetAddress;
localhost = InetAddress.getLocalHost();
println "My local machine name | IP address is : " + localhost
println "Only IP Address : " + localhost.getHostAddress()
println "ifconfig".execute().text

